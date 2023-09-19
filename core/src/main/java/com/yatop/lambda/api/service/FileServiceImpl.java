package com.yatop.lambda.api.service;


import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
        }
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = IdUtil.fastSimpleUUID() + suffixName;
        System.err.println(fileName);
        File dest = new File(fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            if (suffixName.equals(".gif")) {
                File src = FileUtil.file(fileName);
                File d = FileUtil.file("scale_" + fileName);
                FileUtil.copy(src, d, true);
            }
            suffixName = suffixName.toLowerCase();
            if (suffixName.equals(".jpg")
                    || suffixName.equals(".png")
                    || suffixName.equals(".bmp")
                    || suffixName.equals(".jpeg")) {
                System.err.println(suffixName);
                ImgUtil.scale(
                        FileUtil.file(fileName),
                        FileUtil.file("scale_" + fileName),
                        0.5f
                        //缩放比例
                );
            }
        } catch (IllegalStateException | IOException e) {
        }
        return fileName;
    }

    @Override
    public String jarUpload(MultipartFile file) {
        if (file.isEmpty()) {
        }
        String fileName = file.getOriginalFilename();
        try {
            FileUtil.writeBytes(file.getBytes(), fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;

    }
}
