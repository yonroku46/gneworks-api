package com.gneworks.api.service;

import com.gneworks.common.id.KsuidGenerator;
import com.gneworks.dao.SiteDao;
import com.gneworks.dao.entity.FireRegion;
import com.gneworks.dao.entity.Household;
import com.gneworks.dao.entity.Site;
import com.gneworks.dto.res.AdminImportResultRes;
import com.gneworks.dto.res.HouseholdRes;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ExcelImportService {

    // 기본 엑셀 컬럼 인덱스 (기본 00소방서 시트 폴백용)
    private static final int COL_DEFAULT_HEAD    = 1;  // 세대주 성명
    private static final int COL_DEFAULT_TYPE    = 3;  // 구분 (아동/노인/장애인)
    private static final int COL_DEFAULT_SIGUNGU = 4;  // 지역(시군구)
    private static final int COL_DEFAULT_ADDR    = 5;  // 도로명주소
    private static final int COL_DEFAULT_APT     = 6;  // 아파트 명칭
    private static final int COL_DEFAULT_DONG    = 7;  // 동
    private static final int COL_DEFAULT_HO      = 8;  // 호수
    private static final int COL_DEFAULT_INSTALL = 16; // 연기감지기 설치 유무 (O/X)
    private static final int COL_DEFAULT_REMARKS = 17; // 비고

    private static final int BATCH_SIZE          = 500; // 벌크 삽입 단위

    @Autowired
    private SiteDao siteDao;

    /**
     * 엑셀 파일을 파싱하여 site/household 테이블에 일괄(벌크) 삽입
     * regionId: 관리자가 선택한 소방관할 ID
     */
    @Transactional
    public AdminImportResultRes importExcel(MultipartFile file, String regionId) throws IOException {
        int siteInserted = 0, siteSkipped = 0;
        int householdInserted = 0, householdSkipped = 0;

        // 소방관할 조회 (regionId null이면 null로 처리)
        FireRegion fireRegion = null;
        String regionName = null;
        if (regionId != null && !regionId.trim().isEmpty()) {
            fireRegion = siteDao.selectFireRegionById(regionId.trim());
            regionName = fireRegion != null ? fireRegion.getName() : null;
        }
        final String finalRegionId = (fireRegion != null) ? fireRegion.getRegionId() : null;

        // 이미 처리된 site 캐시 (name+address → siteId)
        Map<String, String> siteCache = new HashMap<>();
        // siteId별 기존 세대 캐시 (siteId → Set of "dong||ho")
        Map<String, Set<String>> siteHouseholdCache = new HashMap<>();
        // 벌크 인서트용 버퍼
        List<Household> pendingHouseholds = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            // 1. 시트 탐색: '00소방서' 우선, 없으면 소방서/세대/원본/명단 포함 시트 탐색, 최종 fallback 첫 번째 시트
            Sheet sheet = workbook.getSheet("00소방서");
            if (sheet == null) {
                for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
                    String sName = workbook.getSheetName(s);
                    if (sName.contains("소방서") || sName.contains("세대") || sName.contains("원본") || sName.contains("명단")) {
                        sheet = workbook.getSheetAt(s);
                        break;
                    }
                }
            }
            if (sheet == null) {
                sheet = workbook.getSheetAt(0);
            }

            int lastRow = sheet.getLastRowNum();

            // 2. 동적 헤더 행 및 컬럼 감지 (다양한 관서별 엑셀 양식/컬럼 시프트 자동 대응)
            int headerRow = -1;
            for (int r = 0; r <= Math.min(6, lastRow); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                boolean hasDong = false;
                boolean hasHo = false;
                for (int c = 0; c < row.getLastCellNum(); c++) {
                    String s = getCellString(row, c).replaceAll("\\s+", "");
                    if (s.equals("동") || s.equals("동(호)")) hasDong = true;
                    if (s.equals("호") || s.equals("호수") || s.equals("호(수)")) hasHo = true;
                }
                if (hasDong && hasHo) {
                    headerRow = r;
                    break;
                }
            }
            if (headerRow == -1) {
                for (int r = 0; r <= Math.min(6, lastRow); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) continue;
                    for (int c = 0; c < row.getLastCellNum(); c++) {
                        String s = getCellString(row, c).replaceAll("\\s+", "");
                        if (s.contains("도로명주소") || s.contains("아파트") || s.contains("주소")) {
                            headerRow = r;
                            break;
                        }
                    }
                    if (headerRow != -1) break;
                }
            }

            int colSeq = -1, colHeadName = -1, colTargetType = -1, colSigungu = -1;
            int colAddress = -1, colApt = -1, colDong = -1, colHo = -1;
            int colInstall = -1, colRemarks = -1;

            int scanLimit = headerRow != -1 ? headerRow : Math.min(4, lastRow);
            for (int r = 0; r <= scanLimit; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                for (int c = 0; c < row.getLastCellNum(); c++) {
                    String s = getCellString(row, c).replaceAll("\\s+", "");
                    if (s.isEmpty()) continue;
                    if (s.equals("연번") || s.equals("순번") || s.equalsIgnoreCase("NO") || s.equalsIgnoreCase("NO.")) colSeq = c;
                    else if (s.contains("세대주") || (s.equals("성명") && colHeadName == -1)) colHeadName = c;
                    else if (s.equals("구분") || s.contains("대상구분")) colTargetType = c;
                    else if (s.contains("시군구") || s.startsWith("지역")) colSigungu = c;
                    else if (s.contains("도로명주소") || (s.contains("주소") && colAddress == -1)) colAddress = c;
                    else if (s.contains("아파트") || s.contains("단지명") || s.contains("건물명") || s.contains("시설명")) colApt = c;
                    else if (s.equals("동") || s.equals("동(호)")) colDong = c;
                    else if (s.equals("호") || s.equals("호수") || s.equals("호(수)")) colHo = c;
                    else if (s.contains("설치")) colInstall = c;
                    else if (s.contains("비고")) colRemarks = c;
                }
            }

            // 폴백 기본값 지정
            if (colDong == -1) colDong = COL_DEFAULT_DONG;
            if (colHo == -1) colHo = COL_DEFAULT_HO;
            if (colApt == -1) colApt = COL_DEFAULT_APT;
            if (colAddress == -1) colAddress = COL_DEFAULT_ADDR;
            if (colHeadName == -1) colHeadName = COL_DEFAULT_HEAD;
            if (colTargetType == -1) colTargetType = COL_DEFAULT_TYPE;
            if (colSigungu == -1) colSigungu = COL_DEFAULT_SIGUNGU;
            if (colInstall == -1) colInstall = COL_DEFAULT_INSTALL;
            if (colRemarks == -1) colRemarks = COL_DEFAULT_REMARKS;

            int startRow = headerRow != -1 ? headerRow + 1 : 3;

            // 3. 데이터 행 순회 및 파싱
            for (int i = startRow; i <= lastRow; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String aptName  = getCellString(row, colApt);
                String address  = getCellString(row, colAddress);
                String headName = colHeadName != -1 ? getCellString(row, colHeadName).replaceAll("\\s+", "") : "";
                String dongRaw  = getCellString(row, colDong);
                String hoRaw    = getCellString(row, colHo);
                String seqNo    = colSeq != -1 ? getCellString(row, colSeq) : "";

                // 합계/소계/헤더 잔여 행 스킵
                if (aptName.contains("합계") || aptName.contains("소계") || aptName.equals("아파트(명칭)") || address.contains("도로명주소")) {
                    continue;
                }

                // 필수 데이터 확인 (아파트명과 주소 둘 다 없으면 스킵)
                if (aptName.isEmpty() && address.isEmpty()) {
                    continue;
                }
                if (aptName.isEmpty()) aptName = address;
                if (address.isEmpty()) address = aptName;

                // 동/호수 미기재 세대 대응 (예: 평택 등 호수 공란 명단 유실 방지)
                if (dongRaw.isEmpty()) dongRaw = "-";
                if (hoRaw.isEmpty()) {
                    if (!seqNo.isEmpty()) {
                        hoRaw = "연번" + seqNo;
                    } else if (!headName.isEmpty()) {
                        hoRaw = headName;
                    } else {
                        hoRaw = (i + 1) + "호";
                    }
                }

                // ── 1. SITE 처리 ──────────────────────────────
                String cacheKey = aptName + "||" + address;
                String siteId = siteCache.get(cacheKey);

                if (siteId == null) {
                    // DB에서 중복 확인
                    Site existing = siteDao.selectSiteByNameAndAddress(aptName, address);
                    if (existing != null) {
                        siteId = existing.getSiteId();
                        siteSkipped++;
                    } else {
                        // 신규 삽입
                        String[] parsed = parseAddress(address, colSigungu != -1 ? getCellString(row, colSigungu) : null);
                        String sido       = parsed[0];
                        String sigungu    = parsed[1];
                        String eupmyeondong = parsed[2];

                        Site site = new Site();
                        site.setSiteId(KsuidGenerator.createId());
                        site.setName(aptName);
                        site.setAddress(address);
                        site.setSido(fireRegion != null && fireRegion.getSidoName() != null ? fireRegion.getSidoName() : sido);
                        site.setSigungu(sigungu);
                        site.setEupmyeondong(eupmyeondong);
                        site.setRegion(regionName != null ? regionName : (fireRegion != null ? fireRegion.getName() : sigungu));
                        site.setRegionId(finalRegionId);
                        site.setCreateTime(new Date());

                        siteDao.insertSite(site);
                        siteId = site.getSiteId();
                        siteInserted++;
                    }
                    siteCache.put(cacheKey, siteId);
                }

                // ── 2. HOUSEHOLD 중복 검사 (메모리 Set 기반 O(1)) ──
                String dong = normalizeDong(dongRaw);
                String ho   = normalizeHo(hoRaw);
                String hhKey = dong + "||" + ho;

                Set<String> existingHouseholds = siteHouseholdCache.computeIfAbsent(siteId, id -> {
                    Set<String> keys = new HashSet<>();
                    List<HouseholdRes> existingList = siteDao.selectHouseholdsBySiteId(id);
                    if (existingList != null) {
                        for (HouseholdRes hh : existingList) {
                            keys.add(hh.getDong() + "||" + hh.getHo());
                        }
                    }
                    return keys;
                });

                // 기존 DB 또는 엑셀 내 이전 행과 중복 시 스킵
                if (existingHouseholds.contains(hhKey)) {
                    householdSkipped++;
                    continue;
                }
                existingHouseholds.add(hhKey);

                String targetType    = mapTargetType(colTargetType != -1 ? getCellString(row, colTargetType) : "");
                String installStatus = mapInstallStatus(colInstall != -1 ? getCellString(row, colInstall) : "");
                String remarks       = colRemarks != -1 ? getCellString(row, colRemarks) : "";

                Household household = new Household();
                household.setHouseholdId(KsuidGenerator.createId());
                household.setSiteId(siteId);
                household.setDong(dong);
                household.setHo(ho);
                household.setHeadName(!headName.isEmpty() ? headName : "미정");
                household.setTargetType(targetType);
                household.setInstallStatus(installStatus);
                household.setRemarks(!remarks.isEmpty() ? remarks : null);
                household.setCreateTime(new Date());

                pendingHouseholds.add(household);

                // 버퍼가 BATCH_SIZE(500)에 도달하면 벌크 인서트 실행
                if (pendingHouseholds.size() >= BATCH_SIZE) {
                    siteDao.insertHouseholdBatch(pendingHouseholds);
                    householdInserted += pendingHouseholds.size();
                    pendingHouseholds.clear();
                }
            }

            // 남은 세대 잔여분 벌크 인서트 실행
            if (!pendingHouseholds.isEmpty()) {
                siteDao.insertHouseholdBatch(pendingHouseholds);
                householdInserted += pendingHouseholds.size();
                pendingHouseholds.clear();
            }
        }

        log.info("[ExcelImport] site: +{}(skip {}), household: +{}(skip {}), region: {}",
                siteInserted, siteSkipped, householdInserted, householdSkipped, regionName);

        return new AdminImportResultRes(siteInserted, siteSkipped, householdInserted, householdSkipped, regionName);
    }

    // ── 셀 값 추출 ─────────────────────────────────────────────

    private String getCellString(Row row, int colIdx) {
        Cell cell = row.getCell(colIdx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double d = cell.getNumericCellValue();
                // 정수처럼 보이면 정수로
                yield (d == Math.floor(d)) ? String.valueOf((long) d) : String.valueOf(d);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                try { yield cell.getStringCellValue().trim(); }
                catch (Exception e) {
                    double d = cell.getNumericCellValue();
                    yield (d == Math.floor(d)) ? String.valueOf((long) d) : String.valueOf(d);
                }
            }
            default -> "";
        };
    }

    // ── 주소 파싱 → [sido, sigungu, eupmyeondong] ──────────────

    private String[] parseAddress(String address, String sigunguRaw) {
        // 정규화: "경기 " → "경기도 "
        String addr = address
                .replaceAll("^경기 ", "경기도 ")
                .replaceAll("^서울 ", "서울특별시 ")
                .replaceAll("^부산 ", "부산광역시 ");

        String[] tokens = addr.split("\\s+");
        String sido = tokens.length > 0 ? tokens[0] : "";
        // 시군구: 두 번째 토큰 (구가 있으면 세 번째까지)
        String sigungu = "";
        if (tokens.length > 1) {
            sigungu = tokens[1];
            if (tokens.length > 2 && (tokens[2].endsWith("구") || tokens[2].endsWith("군"))) {
                sigungu = tokens[1] + " " + tokens[2];
            }
        }
        // sigunguRaw 우선 사용 (예: "수원시 장안구")
        if (sigunguRaw != null && !sigunguRaw.trim().isEmpty()) {
            sigungu = sigunguRaw.trim();
        }

        // 읍면동: 주소에서 "로", "길", "대로" 직전 행정구역
        String eupmyeondong = extractEupmyeondong(tokens);

        return new String[]{ sido, sigungu, eupmyeondong };
    }

    private String extractEupmyeondong(String[] tokens) {
        for (int i = tokens.length - 1; i >= 0; i--) {
            String t = tokens[i];
            if (t.endsWith("읍") || t.endsWith("면") || t.endsWith("동") || t.endsWith("리")) {
                return t;
            }
        }
        return "";
    }

    // ── 동/호 정규화 ───────────────────────────────────────────

    private String normalizeDong(String raw) {
        if (raw == null) return "";
        // 1. 모든 종류의 공백(일반 스페이스, 탭, NBSP 등) 제거 (예: " 101 동 " → "101동")
        String s = raw.replaceAll("[\\s\\u00A0]+", "");
        // 2. 접두사 '제' 제거 (예: "제101동" → "101동")
        s = s.replaceAll("^제", "");
        // 3. 괄호 및 부가 설명 제거 (예: "101동(지하)" → "101동")
        s = s.replaceAll("\\([^)]*\\)", "");
        // 4. 끝의 '동' 제거 (단, 문자열이 오직 "동" 한 글자인 경우는 제외)
        if (s.endsWith("동") && s.length() > 1) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    private String normalizeHo(String raw) {
        if (raw == null) return "";
        // 1. 모든 종류의 공백(일반 스페이스, 탭, NBSP 등) 제거 (예: " 101 호 " → "101호")
        String s = raw.replaceAll("[\\s\\u00A0]+", "");
        // 2. 접두사 '제' 제거 (예: "제101호" → "101호")
        s = s.replaceAll("^제", "");
        // 3. 괄호 및 부가 설명 제거 (예: "101호(상가)" → "101호")
        s = s.replaceAll("\\([^)]*\\)", "");
        // 4. 끝의 '호' 제거 (단, 문자열이 오직 "호" 한 글자인 경우는 제외)
        if (s.endsWith("호") && s.length() > 1) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    // ── 코드 매핑 ──────────────────────────────────────────────

    private String mapTargetType(String raw) {
        return switch (raw.trim()) {
            case "아동"  -> "CHILD";
            case "노인"  -> "ELDERLY";
            case "장애인" -> "DISABLED";
            default     -> "GENERAL";
        };
    }

    private String mapInstallStatus(String raw) {
        // 소방서 보급 대상 명단의 표기와 무관하게 신규 등록 세대는 항상 미설치(UNINSTALLED)로 시작하며 작업 보고서 승인 시 완료로 전환됨
        return "UNINSTALLED";
    }
}
