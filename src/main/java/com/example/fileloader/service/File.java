package com.example.fileloader.service;

import org.springframework.core.io.Resource;

public class File {
    private String filename;
    private String fileType;
    private Resource file;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Resource getFile() {
        return file;
    }

    public void setFile(Resource file) {
        this.file = file;
    }
}
