package com.example.fileloader.controller;

import com.example.fileloader.service.File;
import com.example.fileloader.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class FileLoaderController {



    private final FileService fileService;

    public FileLoaderController(FileService fileService) {
        this.fileService = fileService;
    }


    @PostMapping("/upload")
    public String upload(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        System.out.println("reached here");
        fileService.addFile(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/fileUploaderForm";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) throws IOException, IOException {
        File file = fileService.downloadFile(id);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body( file.getFile());
    }



}
