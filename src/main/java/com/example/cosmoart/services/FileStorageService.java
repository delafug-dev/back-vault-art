package com.example.cosmoart.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    @Value("${upload.dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }
        try {
            // Asegúrate de que el directorio exista
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Genera un nombre único para evitar conflictos (opcionalmente con UUID o timestamp)
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, file.getBytes());
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al almacenar el archivo " + file.getOriginalFilename());
        }
    }

}
