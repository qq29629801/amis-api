package com.yatop.lambda.api.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String save(MultipartFile file);
    String saveJar(MultipartFile file);
}
