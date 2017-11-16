package com.yijia.yy.repository;

import com.yijia.yy.domain.FileInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FileInfo entity.
 */
@SuppressWarnings("unused")
public interface FileInfoRepository extends JpaRepository<FileInfo,Long> {

}
