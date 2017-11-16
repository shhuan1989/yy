package com.yijia.yy.repository;

import com.yijia.yy.domain.PictureInfo;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the PictureInfo entity.
 */
@SuppressWarnings("unused")
public interface PictureRepository extends JpaRepository<PictureInfo,Long> {

}
