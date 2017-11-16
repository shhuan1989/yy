package com.yijia.yy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String fileLocation = "upload-files";

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String location) {
        this.fileLocation = location;
    }

}
