package com.yijia.yy.service.impl;

import com.yijia.yy.config.StorageProperties;
import com.yijia.yy.domain.FileInfo;
import com.yijia.yy.exception.StorageException;
import com.yijia.yy.exception.StorageFileNotFoundException;
import com.yijia.yy.repository.FileInfoRepository;
import com.yijia.yy.security.SecurityUtils;
import com.yijia.yy.service.FileStorageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
@Qualifier("fileStorage")
public class FileStorageServiceImpl implements FileStorageService {
    private final Logger log = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    @Inject
    private FileInfoRepository fileRepository;

    private final Path rootLocation;

    @Autowired
    public FileStorageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getFileLocation());
        if (!Files.exists(this.rootLocation)) {
            try {
                Files.createDirectories(this.rootLocation);
            } catch (IOException e) {
                log.warn("Error while creating file directory " + this.rootLocation.toAbsolutePath(), e);
            }
        }
    }

    @Override
    public FileInfo store(MultipartFile file, String originName) {
        Long cms = System.currentTimeMillis();
        while (true) {
            if (Files.exists(this.rootLocation.resolve(cms+""))) {
                cms += 1;
            } else {
                break;
            }
        }

        String extension = com.google.common.io.Files.getFileExtension(file.getOriginalFilename());
        String fileName = String.valueOf(cms)+"."+extension;
        if (StringUtils.isBlank(originName)) {
            try {
                originName = new String(file.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                originName = file.getOriginalFilename();
            }
        }
        FileInfo fileInfo = new FileInfo().createTime(cms)
            .creator(SecurityUtils.getCurrentUserLogin())
            .name(fileName)
            .originName(originName);

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Path path = this.rootLocation.resolve(fileName);
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }

        return fileRepository.save(fileInfo);
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(Long id) {
        String filename = fileRepository.findOne(id).getName();
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + file.toAbsolutePath());

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize file storage", e);
        }
    }

    public FileInfoRepository getFileRepository() {
        return fileRepository;
    }

    public void setFileRepository(FileInfoRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Path getRootLocation() {
        return rootLocation;
    }
}

