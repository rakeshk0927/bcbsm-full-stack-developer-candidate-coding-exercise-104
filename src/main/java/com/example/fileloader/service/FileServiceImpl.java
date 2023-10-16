package com.example.fileloader.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    public String addFile(MultipartFile uploadFile) throws IOException {
        byte[] compressedData = compressMultipartFile(uploadFile);

        DBObject metadata = new BasicDBObject();
        Object fileID = template.store(new ByteArrayInputStream(compressedData), uploadFile.getOriginalFilename()+".gz", uploadFile.getContentType());

        return fileID.toString();
    }


    public File downloadFile(String id) throws IOException {

        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        File file = new File();
        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            file.setFileType(gridFSFile.getMetadata().get("_contentType").toString());
            file.setFilename(gridFSFile.getFilename());
            file.setFile(operations.getResource(gridFSFile));
        }

        return file;
    }

    private byte[] compressMultipartFile(MultipartFile file) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getBytes());
             ByteArrayOutputStream compressedStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(compressedStream)) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                gzipOutputStream.write(buffer, 0, len);
            }

            gzipOutputStream.finish();
            gzipOutputStream.flush();
            return compressedStream.toByteArray();
        }
    }
}
