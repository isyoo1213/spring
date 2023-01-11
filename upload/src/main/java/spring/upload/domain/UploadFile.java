package spring.upload.domain;

import lombok.Data;

@Data
public class UploadFile {

    private String uploadFileName; //사용자가 저장한 파일 이름
    private String storeFileName; //UUID 등을 사용하여 겹치지 않도록 시스템에 저장하는 파일 이름

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
