package com.yijia.yy.web.rest;


import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.domain.FileInfo;
import com.yijia.yy.exception.StorageException;
import com.yijia.yy.exception.StorageFileNotFoundException;
import com.yijia.yy.service.FileStorageService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api")
public class FileController {
    private final Logger log = LoggerFactory.getLogger(FileController.class);


    private final FileStorageService storageService;

    @Inject
    ApplicationContext appContext;

    @Inject
    private PictureController pictureController;

    @Autowired
    public FileController(@Qualifier("fileStorage") FileStorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value = "/files/{id}",
        method = RequestMethod.GET,
        produces = { MediaType.ALL_VALUE })
    public ResponseEntity<byte[]> serveFile(@PathVariable Long id) throws IOException {
        Resource resource = null;
        try {
            resource = storageService.loadAsResource(id);
        } catch (Exception e) {
            log.warn("Error while loding file " + id, e);
        }
        if (resource == null) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }

        InputStream in = resource.getInputStream();
        try {
            return new ResponseEntity<>(IOUtils.toByteArray(in), null,
                HttpStatus.OK);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    @RequestMapping(
        value = "/files",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<FileInfo> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam(value = "name", required = false) String originName) {
        if (StringUtils.isNotBlank(file.getContentType()) && file.getContentType().contains("image")) {
            return pictureController.handleFileUpload(file);
        }
        FileInfo fileInfo = storageService.store(file, originName);
        return ResponseEntity.ok(fileInfo);
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
