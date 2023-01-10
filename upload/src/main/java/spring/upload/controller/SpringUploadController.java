package spring.upload.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    //스프링이 제공하는 MultiPartFile 사용하기
    @PostMapping("/upload")
    public String saveFile(
            @RequestParam String itemName, //@ModelAttriute에서도 적용 가능 by ArgumentResolver
            @RequestParam MultipartFile file,
            HttpServletRequest request
            ) throws IOException {

        log.info("request = {}", request);
        //실제 로그 - request = org.springframework.web.multipart.support.StandardMultipartHttpServletRequest@2a91344d
        log.info("itemName = {}", itemName);
        log.info("multipartFile = {}", file);

        if (!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            log.info("파일 저장 fullPath = {}", fullPath);

            //파일 저장 메서드
            file.transferTo(new File(fullPath));
        }

        return "upload-form";
    }
}
