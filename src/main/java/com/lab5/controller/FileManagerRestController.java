package com.lab5.controller;

import com.lab5.entity.Image;
import com.lab5.service.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/files")
public class FileManagerRestController {

    @Autowired
    FileManagerService fileService;

    @GetMapping("/{folder}/{file}")
    public byte[] download(@PathVariable("folder") String folder, @PathVariable("file") String file) {
        return fileService.read(folder, file);
    }

    @PostMapping("/{folder}")
    public List<Image> upload(@PathVariable("folder") String folder, @RequestParam("files") MultipartFile[] files) {
        return fileService.save(folder, files);
    }

    @DeleteMapping("/{folder}/{file}")
    public void delete(@PathVariable("folder") String folder, @PathVariable("file") String file) {
        fileService.delete(folder, file);
    }

    @GetMapping("/{folder}")
    public List<String> list(@PathVariable("folder") String folder) {
        return fileService.list(folder);
    }
}
