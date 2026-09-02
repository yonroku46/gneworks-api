package com.gneworks.common.constants;

import lombok.NoArgsConstructor;

/**
 * 메시지 작성 키 정의
 *
 * @author y_ha
 */
@NoArgsConstructor
public class MessageIdConst {

    /**
     * ERROR : 일자가 부정확합니다
     */
    public static final String E_INVALID_TERM = "error.invalidterm";

    /**
     * ERROR : 해당 데이터가 없습니다
     */
    public static final String E_DATA_NOT_FOUND = "error.dataNotFound";

    /**
     * ERROR : SQL 발행
     */
    public static final String E_SQL_ISSUE = "error.sql.issue";

    /**
     * ERROR : 로그인 실패
     */
    public static final String E_LOGIN_USER_FAILED = "login.user.failed";

    /**
     * ERROR : 토큰 없음
     */
    public static final String E_NO_ACCESS_TOKEN = "error.noAccessToken";

    /**
     * ERROR : 토큰 만료
     */
    public static final String E_EXPIRED_TOKEN = "error.expiredJWT";

    /**
     * ERROR : 유효하지 않은 토큰
     */
    public static final String E_INVALID_TOKEN = "error.invalidJWT";

    /**
     * ERROR : 지원하지 않는 토큰
     */
    public static final String E_UNSUPPORTED_TOKEN = "error.unsupportedJWT";

    /**
     * ERROR : 대기 접수 불가
     */
    public static final String E_CANNOT_WAITING = "error.cannot.waiting";

    /**
     * ERROR : 최대 대기 수 초과
     */
    public static final String E_MAX_WAITING = "error.max.waiting";

    /**
     * INFO : 등록 성공
     */
    public static final String I_INSERT_SUCCESS = "info.insert.success";

    /**
     * INFO : 조회 성공
     */
    public static final String I_GETTING_SUCCESS = "info.getting.success";

    /**
     * INFO : 수정 성공
     */
    public static final String I_UPDATE_SUCCESS = "info.update.success";

    /**
     * INFO : 삭제 성공
     */
    public static final String I_DELETE_SUCCESS = "info.delete.success";

    /**
     * INFO : 저장 성공
     */
    public static final String I_SAVE_SUCCESS = "info.save.success";

    /**
     * INFO : 조회 실패
     */
    public static final String I_GETTING_FAILED = "info.getting.failed";

    /**
     * INFO : 등록 실패
     */
    public static final String I_INSERT_FAILED= "info.insert.failed";

    /**
     * INFO : 수정 실패
     */
    public static final String I_UPDATE_FAILED= "info.update.failed";

    /**
     * INFO : 삭제 실패
     */
    public static final String I_DELETE_FAILED= "info.delete.failed";

    /**
     * INFO : 저장 실패
     */
    public static final String I_SAVE_FAILED= "info.save.failed";

    /**
     * INFO : 서버 가동 중
     */
    public static final String I_SERVER_RUNNING = "info.server.running";

    /**
     * INFO : 로그인 성공
     */
    public static final String I_LOGIN = "info.loginIn";

    /**
     * INFO : 로그아웃 성공
     */
    public static final String I_LOGOUT = "info.loginOut";

    /**
     * INFO : 다운로드 성공
     */
    public static final String I_DOWNLOAD_SUCCESS = "info.download.success";

    /**
     * INFO : 유효한 토큰
     */
    public static final String I_VERIFY_TOKEN = "info.verifyToken";

    /**
     * INFO : 토큰 리프레시
     */
    public static final String I_REFRESH_TOKEN = "info.refreshToken";

    /**
     * ERROR : 사용자 검색 불가
     */
    public static final String E_USER_NOT_FOUND = "error.user.notFound";
}
