package com.fslvo.FileManagerFSLVO.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ActivityFederationService {

    @Value("${activityFederationGeneral.dir}")
    private String uploadDirectoryGeneral;

    @Value("${activityFederation3D.dir}")
    private String uploadDirectory3D;

    @Value("${activityFederationClassic.dir}")
    private String uploadDirectoryClassic;

    @Value("${activityFederationBiathlon.dir}")
    private String uploadDirectoryBiathlon;

    /**
     * Загрузка файла из дирректорию aboutFederation
     */
    public byte[] downloadFile(String flag, String fileName) throws IOException {
        String filePath = "";
        if (Objects.equals(flag, "General")) {
            filePath = uploadDirectoryGeneral + fileName;
        } else if (Objects.equals(flag, "3D")) {
            filePath = uploadDirectory3D + fileName;
        } else if (Objects.equals(flag, "Classic")) {
            filePath = uploadDirectoryClassic + fileName;
        } else if (Objects.equals(flag, "Biathlon")) {
            filePath = uploadDirectoryBiathlon + fileName;
        }
        return Files.readAllBytes(new File(filePath).toPath());
    }

    public ResponseEntity<String> uploadFiles(String flag, MultipartFile[] files) {
        String uploadDirectory = "";
        switch (flag) {
            case "General" -> uploadDirectory = uploadDirectoryGeneral;
            case "3D" -> uploadDirectory = uploadDirectory3D;
            case "Classic" -> uploadDirectory = uploadDirectoryClassic;
            case "Biathlon" -> uploadDirectory = uploadDirectoryBiathlon;
        }
        try {
            // Очищаем директорию
            cleanDirectory(uploadDirectory);

            // Сохраняем новые файлы
            if (files != null && files.length > 0) {
                List<String> savedFiles = new ArrayList<>();

                for (MultipartFile file : files) {
                    if (file != null && !file.isEmpty()) {
                        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

                        // Проверка на безопасность имени файла
                        if (fileName.contains("..")) {
                            throw new IllegalArgumentException("Недопустимое имя файла: " + fileName);
                        }

                        Path filePath = Paths.get(uploadDirectory, fileName);
                        Files.createDirectories(filePath.getParent());

                        // Сохраняем файл
                        try (InputStream inputStream = file.getInputStream()) {
                            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                        }

                        savedFiles.add(fileName);
                        System.out.println("Сохранен файл: " + fileName);
                    }
                }
                return ResponseEntity.ok("Успешно сохранено файлов: " + savedFiles.size());
            } else {
                return ResponseEntity.ok("Успешно сохранены изменения");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при обработке файлов: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void cleanDirectory(String directoryPath) throws IOException {
        Path dir = Paths.get(directoryPath);

        if (Files.exists(dir) && Files.isDirectory(dir)) {
            // Удаляем только файлы, не поддиректории
            try (Stream<Path> paths = Files.list(dir)) {
                List<Path> filesToDelete = paths
                        .filter(path -> !Files.isDirectory(path))
                        .collect(Collectors.toList());

                for (Path file : filesToDelete) {
                    Files.deleteIfExists(file);
                    System.out.println("Удален файл: " + file.getFileName());
                }
            }
        } else {
            // Создаем директорию если не существует
            Files.createDirectories(dir);
        }
    }
}
