package com.yijia.yy.schedule;

import com.yijia.yy.config.StorageProperties;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.FileInfo;
import com.yijia.yy.domain.PictureInfo;
import com.yijia.yy.repository.EmployeeRepository;
import com.yijia.yy.repository.FileInfoRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class UserPhotoNomolizer {
    private final Logger log = LoggerFactory.getLogger(UserPhotoNomolizer.class);

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private StorageProperties storageProperties;

    @Inject
    private FileInfoRepository fileInfoRepository;

    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void normalizePhotos() {
        Path rootLocation = Paths.get(storageProperties.getFileLocation());
        List<Employee> employeeList = employeeRepository.findAll();
        if (employeeList != null) {
            employeeList.stream()
                .forEach(m -> {
                    if (m.getPhoto() == null) {
                        return;
                    }
                    FileInfo photo = fileInfoRepository.findOne(m.getPhoto().getId());
                    if (photo == null) {
                        return;
                    }
                    String filename = photo.getName();
                    Path path = rootLocation.resolve(filename);
                    try {
                        Thumbnails.of(path.toFile())
                            .size(100, 100)
                            .keepAspectRatio(false)
                            .toFile(path.toFile());
                    } catch (IOException e) {
                        log.warn("Error while normalize user photo", e);
                    }
                });
        }
    }
}
