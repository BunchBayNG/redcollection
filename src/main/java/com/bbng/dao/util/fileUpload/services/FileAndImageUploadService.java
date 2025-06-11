package com.bbng.dao.util.fileUpload.services;


import com.bbng.dao.util.exceptions.customExceptions.InternalServerException;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileAndImageUploadService {

    private Cloudinary cloudinary;

    public FileAndImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;

    }

    public String uploadFile(MultipartFile file, String doc) {
        if (file == null) {
            return "";
        }
        Map<String, Object> options = new HashMap<>();
        options.put("folder", "redtech_folder/" + doc);

        options.put("overwrite", true);
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            throw new InternalServerException("error occurred while uploading image to cloundinary: " + e.getMessage());
        }

    }
}
