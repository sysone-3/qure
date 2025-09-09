package app.snapshot.qure.mobile.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;

@Controller
@RequestMapping("/dev/s3")
public class S3DevController {

    @Value("${s3.bucket}") 
    private String bucket;

    @Value("${aws.region}") 
    private String region;

    private S3Client s3;
    private volatile boolean s3Available = false;

    @PostConstruct
    void init() {
        try {
            s3 = S3Client.builder()
                    .region(Region.of(region))
                    .build();
            // 버킷 존재 여부만 확인
            s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
            s3Available = true;
        } catch (Exception e) {
            // 실패하면 컨트롤러는 살리고, 로그만 남김
            System.err.println("[S3DevController] S3 init failed: " + e.getMessage());
            s3Available = false;
        }
    }

    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        return s3Available ? "S3 OK" : "S3 NOT AVAILABLE";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (!s3Available) {
            return "S3 not available";
        }
        String key = "dev/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3.putObject(
            PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build(),
            RequestBody.fromBytes(file.getBytes())
        );
        return key;
    }

    @GetMapping("/get")
    public void get(@RequestParam String key, HttpServletResponse res) throws IOException {
        if (!s3Available) {
            res.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "S3 not available");
            return;
        }
        try (var obj = s3.getObject(
                GetObjectRequest.builder().bucket(bucket).key(key).build())) {
            res.setHeader("Content-Disposition",
                "inline; filename=\"" + key.replaceAll(".*/", "") + "\"");
            obj.transferTo(res.getOutputStream());
            res.flushBuffer();
        }
    }
}
