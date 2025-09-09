package app.snapshot.qure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@PropertySource("classpath:application-db.properties") // 여길 읽게 함
public class AwsConfig {

  // 서블릿 컨텍스트에도 플레이스홀더 해석기를 등록
  @Bean
  public static PropertySourcesPlaceholderConfigurer props() {
    PropertySourcesPlaceholderConfigurer p = new PropertySourcesPlaceholderConfigurer();
    p.setIgnoreUnresolvablePlaceholders(true);
    return p;
  }

  @Value("${aws.region}")
  private String region;

  @Bean
  public S3Client s3Client(
        @Value("${aws.accessKeyId}") String accessKey,
        @Value("${aws.secretAccessKey}") String secretKey
  ) {
    return S3Client.builder()
      .region(Region.of(region))
        .credentialsProvider(
          StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
        )
    .build();
  }
}
