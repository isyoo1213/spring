package spring.upload.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

    //application.properties에 설정한 경로 속성을 가져오기
    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {

        log.info("request = {}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName = {}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("parts = {}", parts);

        for (Part part : parts) {
            log.info("==== PARTS ====");
            log.info("name = {}", part.getName());

            // * Part 또한 Header와 Body로 구성 -> 각각 Part의 Header들을 로깅
            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("header = {} : {}", headerName, part.getHeader(headerName));
            }

            // + 제공하는 편의 메서드

            // content-disposition; filename - input태그의 name이 아닌, 실제 file의 Name ex) "img.png"
            log.info("submittedFilename = {}", part.getSubmittedFileName());

            // Part의 BodySize
            log.info("size = {}", part.getSize());

            // Data 읽기 - part는 getInputStream() 제공
            InputStream inputStream = part.getInputStream();

            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            //Binary Data와 String 변환에는 항상 CharacterSet 설정해주기

            log.info("body = {}", body);

            // 파일에 저장하기
            if (StringUtils.hasText(part.getSubmittedFileName())) { //실제 파일 존재하는지 파일 이름 확인
                String fullPath = fileDir + part.getSubmittedFileName();
                log.info("파일 저장 fullPath = {}", fullPath);

                part.write(fullPath); //실제 저장하는 메서드
            }
        }

        return "upload-form";
    }
}
