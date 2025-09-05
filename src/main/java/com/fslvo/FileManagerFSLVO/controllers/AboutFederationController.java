package com.fslvo.FileManagerFSLVO.controllers;

import com.fslvo.FileManagerFSLVO.service.AboutFederationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/aboutFederation")
public class AboutFederationController {

    private final AboutFederationService aboutFederationService;

    @Autowired
    public AboutFederationController(AboutFederationService aboutFederationService) {
        this.aboutFederationService = aboutFederationService;
    }

    /**
     * Загрузка файлов из articleImage
     */
    @GetMapping("download")
    public ResponseEntity<?> downloadFileAboutFederation(@RequestParam String fileName) throws IOException {
        if (fileName != null) {
            byte[] imageData = aboutFederationService.downloadFile(fileName);
            HttpHeaders headers = new HttpHeaders();
            if (fileName.endsWith("pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            } else if (fileName.endsWith("jpg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (fileName.endsWith("png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            }
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Вы не передали название файла для загрузки");
        }
    }

    @PostMapping(value = "uploadFiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFilesAboutFederation(@RequestParam(name = "files", required = false) MultipartFile[] files) throws IOException {
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                System.out.println("Файл: " + file.getOriginalFilename() + ", размер: " + file.getSize());
            }
        }
        ResponseEntity<String> response = aboutFederationService.uploadFiles(files);
        return response;
    }

}
