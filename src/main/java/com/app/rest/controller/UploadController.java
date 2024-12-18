package com.app.rest.controller;


import com.app.utils.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/presigned-url")
    public Map<String, String> getPresignedUrl(@RequestBody Map<String, String> request) {
        String fileName = request.get("fileName");
        String fileType = request.get("fileType");

        URL presignedUrl = s3Service.generatePresignedUrl(fileName, fileType);

        Map<String, String> response = new HashMap<>();
        response.put("url", presignedUrl.toString());
        return response;
    }
}
