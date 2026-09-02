package com.gneworks.common.utils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * AWS S3 관련 유틸리티
 *
 * @author y_ha
 */
@Component
public class S3Utils {

    @Value("${cloud.aws.s3.bucketName}")
    private String AWS_S3_BUCKET_NAME;

    @Value("${cloud.aws.s3.prefix.user}")
    private String AWS_S3_PREFIX_USER;

    private static String bucketName;
    private static String prefixUser;

    /**
     * 파일 서버 헬퍼 클래스
     */
    protected S3Utils() {
    }

    @PostConstruct
    public void init() {
        bucketName = AWS_S3_BUCKET_NAME;
        prefixUser = AWS_S3_PREFIX_USER;
    }


    /**
     * 파일 업로드
     *
     * @param fileName 파일명
     * @param fileType 파일 타입
     * @param is 업로드할 데이터 스트림
     * @param prefix 접두사명
     * @param amazonS3 S3 클래스
     * @return
    */
    public static String uploadFile(String fileName, String fileType, InputStream is, String prefix, AmazonS3 amazonS3) {
        try {
            TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(amazonS3).withMinimumUploadPartSize(1L * 1024 * 1024).build();
            byte[] bytes = IOUtils.toByteArray(is);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            metadata.setContentType(fileType);
            ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
            // 업로드 실행
            String uploadName = new StringBuilder(prefix).append("/").append(fileName).toString();
            Upload upload = transferManager.upload(bucketName, uploadName, byteArray, metadata);
            upload.waitForCompletion();
            return uploadName;
        } catch (AmazonClientException | IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String uploadFile(String fileName, byte[] fileData, String contentType, AmazonS3 amazonS3) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(fileData.length);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));

            return fileName;
        } catch (AmazonClientException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 파일 다운로드
     *
     * @param fileName
     * @param localFileName
     * @param amazonS3
     * @return
     * @throws AmazonClientException
     */
    public static boolean downloadFile(String fileName, String localFileName, AmazonS3 amazonS3) {
        try {
            TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(amazonS3).build();
            // 데이터 다운로드
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withPrefix(fileName)
                    .withMarker("");

            ObjectListing objectListing = amazonS3.listObjects(listObjectsRequest);
            if (objectListing.getObjectSummaries().isEmpty()) {
                // 지정된 파일이 존재하지 않는 경우 종료
                return false;
            }
            Download xfer = transferManager.download(bucketName, fileName, new File(localFileName));
            xfer.waitForCompletion();
        } catch (AmazonClientException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    /**
     * 파일 삭제
     *
     * @param fileName
     * @param amazonS3
     * @return
     * @throws AmazonClientException
     */
    public static boolean deleteFile(String fileName, AmazonS3 amazonS3) {
        try {
            TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(amazonS3).build();
            // 데이터 다운로드
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withPrefix(fileName)
                    .withMarker("");

            ObjectListing objectListing = amazonS3.listObjects(listObjectsRequest);
            if (!objectListing.getObjectSummaries().isEmpty()) {
                transferManager.getAmazonS3Client().deleteObject(new DeleteObjectRequest(bucketName, fileName));
            } else {
                return false;
            }

        } catch (AmazonClientException e) {
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    /**
     * 파일 목록 조회
     *
     * @param objectName
     * @param amazonS3
     * @return fileList
     * @throws AmazonClientException
     */
    public static List<String> getFileList(String objectName, AmazonS3 amazonS3) {
        List<String> fileList = new ArrayList<>();
        try {
            // 목록 조회
            ObjectListing objListing = amazonS3.listObjects(bucketName, objectName);
            List<S3ObjectSummary> objList = objListing.getObjectSummaries();
            if (!CollectionUtils.isEmpty(objList)) {
                objList.forEach((obj) -> {
                    String key = obj.getKey();
                    if (!key.equals(objectName)) {
                        fileList.add(obj.getKey());
                    }
                });
            }
        } catch (AmazonServiceException e) {
            throw new RuntimeException(e.getMessage());
        }

        // 파일명 내림차순 정렬
        fileList.sort(Comparator.reverseOrder());

        return fileList;
    }
}