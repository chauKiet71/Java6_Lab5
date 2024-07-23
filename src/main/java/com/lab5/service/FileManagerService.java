package com.lab5.service;

import com.lab5.dao.ImageDao;
import com.lab5.entity.Image;
import jakarta.servlet.ServletContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class FileManagerService {
//    private static final Logger logger = (Logger) LoggerFactory.getLogger(FileManagerService.class);

    private final ServletContext app;
    private final ImageDao imageDao;
    ImageDao fileInfoRepository;

    @Autowired
    public FileManagerService(ServletContext app, ImageDao fileInfoRepository, ImageDao imageDao) {
        this.app = app;
        this.fileInfoRepository = fileInfoRepository;
        this.imageDao = imageDao;
    }

    private Path getPath(String folder, String filename) {
        File dir = Paths.get(app.getRealPath("/files/"), folder).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return Paths.get(dir.getAbsolutePath(), filename);
    }

    public byte[] read(String folder, String filename) {
        Path path = getPath(folder, filename);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
//            logger.log("Failed to read file: " + filename, e);
            throw new RuntimeException("Failed to read file: " + filename, e);
        }
    }

    public List<byte[]> readAllFiles(String folder) {
        List<Image> filenames = imageDao.findAll();
        List<byte[]> filesContent = new ArrayList<>();

        for (Image filename : filenames) {
            byte[] content = read(folder, filename.getFilename());
            filesContent.add(content);
        }
        return filesContent;
    }

    public void delete(String folder, String filename) {
        Path path = getPath(folder, filename);
        Image img= new Image();
        boolean deleted = path.toFile().delete();
        if (deleted) {
            System.out.println(filename);
            fileInfoRepository.deleteByName(filename);
        } else {
//            logger.error("Failed to delete file: " + filename);
            throw new RuntimeException("Failed to delete file: " + filename);
        }
    }

    public List<Image> save(String folder, MultipartFile[] files) {
        List<Image> savedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String name = System.currentTimeMillis() + originalFilename;
            String filename = Integer.toHexString(name.hashCode()) + name.substring(name.lastIndexOf("."));
            Path path = this.getPath(folder, filename);
            try {
                file.transferTo(path);
                Image fileInfo = new Image();
                fileInfo.setFilename(filename);
                fileInfo.setFolder(folder);
                fileInfo.setOriginal_filename(filename);
                fileInfo.setSize(file.getSize());
                savedFiles.add(fileInfoRepository.save(fileInfo));
            } catch (IOException e) {
//                logger.error("Failed to save file: " + filename, e);
                throw new RuntimeException("Failed to save file: " + filename, e);
            }
        }
        return savedFiles;
    }

    public List<String> list(String folder) {
        List<String> list = new ArrayList<>();
        File dir = Paths.get(app.getRealPath("/files/"), folder).toFile();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    list.add(file.getName());
                }
            }
        }
        return list;
    }
}
