package com.gneworks.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import javax.naming.ConfigurationException;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    /**
     * AccessKey
     */
    @Value("${cloud.aws.credentials.accessKey}")
    private String AWS_CREDENTIALS_ACCESS_KEY;

    /**
     * SecretKey
     */
    @Value("${cloud.aws.credentials.secretKey}")
    private String AWS_CREDENTIALS_SECRET_KEY;

    /**
     * Region
     */
    @Value("${cloud.aws.region.static}")
    private String AWS_REGION_STATIC;

    @Bean
    public BasicAWSCredentials basicAWSCredentials() throws ConfigurationException {
        return new BasicAWSCredentials(AWS_CREDENTIALS_ACCESS_KEY, AWS_CREDENTIALS_SECRET_KEY);
    }

    @Bean
    public AmazonS3 amazonS3Client(AWSCredentials awsCredentials) throws ConfigurationException {
        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(Regions.fromName(AWS_REGION_STATIC)).build();
    }

    @Bean
    public AmazonSimpleEmailService amazonSESClient(AWSCredentials awsCredentials) throws ConfigurationException {
        return AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.fromName(AWS_REGION_STATIC)).build();
    }

    @Bean
    public AWSSecretsManager amazonSecretsManagerClient(AWSCredentials awsCredentials) throws ConfigurationException {
        return AWSSecretsManagerClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(Regions.fromName(AWS_REGION_STATIC)).build();
    }

    @Bean
    public AWSSimpleSystemsManagement amazonSSMClient(AWSCredentials awsCredentials) throws ConfigurationException {
        return AWSSimpleSystemsManagementClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(Regions.fromName(AWS_REGION_STATIC)).build();
    }
}
