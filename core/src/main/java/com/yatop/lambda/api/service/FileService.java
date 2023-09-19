package com.yatop.lambda.api.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String upload(MultipartFile file);
    String jarUpload(MultipartFile file);
}
