package com.yijia.yy.web.rest;


import com.codahale.metrics.annotation.Timed;
import com.google.common.io.Files;
import com.yijia.yy.domain.FileInfo;
import com.yijia.yy.domain.PictureInfo;
import com.yijia.yy.exception.StorageException;
import com.yijia.yy.exception.StorageFileNotFoundException;
import com.yijia.yy.service.FileStorageService;
import com.yijia.yy.service.FileStorageService;
import com.yijia.yy.service.dto.FileInfoDTO;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/resource")
public class PictureController {
    private final Logger log = LoggerFactory.getLogger(PictureController.class);

    private final FileStorageService storageService;

    @Inject
    ApplicationContext appContext;

    @Autowired
    public PictureController(@Qualifier("pictureStorage") FileStorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value = "/pictures/{id}",
        method = RequestMethod.GET,
        produces = {
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE
        })
    public ResponseEntity<byte[]> servePicture(@PathVariable Long id) throws IOException {
        Resource resource = null;
        try {
            resource = storageService.loadAsResource(id);
        } catch (Exception e) {
            resource = appContext.getResource("classpath:placeholder_avatar.jpg");
        }
        if (resource == null) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }

        InputStream in = resource.getInputStream();
        try {
            HttpHeaders headers = new HttpHeaders();
            String extension = Files.getFileExtension(resource.getFilename());
            switch (extension) {
                case "jpg":
                    headers.setContentType(MediaType.IMAGE_JPEG);
                    break;
                case "gif":
                    headers.setContentType(MediaType.IMAGE_GIF);
                    break;
                default:
                    headers.setContentType(MediaType.IMAGE_PNG);
            }

            return new ResponseEntity<>(IOUtils.toByteArray(in), headers,
                HttpStatus.OK);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    @RequestMapping(
        value = "/pictures",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<FileInfo> handleFileUpload(@RequestParam("file") MultipartFile file) {
        FileInfo pictureInfo = storageService.store(file, null);
        return ResponseEntity.ok(pictureInfo);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity handleStorageException(StorageException exec) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exec.getLocalizedMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception exec) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exec.getLocalizedMessage());
    }

}
