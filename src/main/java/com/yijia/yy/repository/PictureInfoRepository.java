package com.yijia.yy.repository;

import com.yijia.yy.domain.PictureInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PictureInfo entity.
 */
@SuppressWarnings("unused")
public interface PictureInfoRepository extends JpaRepository<PictureInfo,Long> {

}
