package com.yijia.yy.service;

import com.yijia.yy.domain.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;


public interface FileStorageService {
    void init();

    FileInfo store(MultipartFile file, String originName);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(Long filename);

    void deleteAll();
}
