package com.yijia.yy.service.impl;

import com.yijia.yy.config.StorageProperties;
import com.yijia.yy.domain.FileInfo;
import com.yijia.yy.domain.PictureInfo;
import com.yijia.yy.exception.StorageException;
import com.yijia.yy.repository.PictureRepository;
import com.yijia.yy.security.SecurityUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Qualifier("pictureStorage")
public class PictureStorageServiceImpl extends FileStorageServiceImpl {
    private final Logger log = LoggerFactory.getLogger(PictureStorageServiceImpl.class);

    @Inject
    private PictureRepository pictureRepository;

    @Autowired
    public PictureStorageServiceImpl(StorageProperties properties) {
        super(properties);
    }

    @Override
    public PictureInfo store(MultipartFile file, String originName) {
        Long cms = System.currentTimeMillis();
        while (true) {
            if (Files.exists(this.getRootLocation().resolve(cms+""))) {
                cms += 1;
            } else {
                break;
            }
        }

        String extension = com.google.common.io.Files.getFileExtension(file.getOriginalFilename());
        if (StringUtils.isBlank(extension)) {
            extension = "jpg";
        }
        String fileName = String.valueOf(cms)+"."+extension;
        if (StringUtils.isBlank(originName)) {
            try {
                originName = new String(file.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                originName = file.getOriginalFilename();
            }
        }
        String thumbnailName = String.valueOf(cms)+"_thumbnail"+"."+extension;
        PictureInfo pictureInfo = new PictureInfo().createTime(cms)
            .creator(SecurityUtils.getCurrentUserLogin())
            .name(fileName)
            .originName(originName)
            .thumbnail(
                new PictureInfo()
                .name(thumbnailName)
            );

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty picture " + file.getOriginalFilename());
            }
            Path path = this.getRootLocation().resolve(fileName);
            Files.copy(file.getInputStream(), path);
            Thumbnails.of(path.toFile()).size(100, 100).toFile(this.getRootLocation().resolve(thumbnailName).toFile());
        } catch (IOException e) {
            throw new StorageException("Failed to store picture " + file.getOriginalFilename(), e);
        }

        PictureInfo result = pictureRepository.save(pictureInfo);
        return result;
    }
}

