package com.example.fileloader.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String addFile(MultipartFile upload) throws IOException;
    File downloadFile(String id) throws IOException;

}
