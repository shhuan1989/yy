package com.yijia.yy.service;

import com.yijia.yy.domain.PictureInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PictureInfo.
 */
public interface PictureService {

    /**
     * Save a pictureInfo.
     *
     * @param pictureInfo the entity to save
     * @return the persisted entity
     */
    PictureInfo save(PictureInfo pictureInfo);

    /**
     *  Get all the pictures.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PictureInfo> findAll(Pageable pageable);

    /**
     *  Get the "id" picture.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PictureInfo findOne(Long id);

    /**
     *  Delete the "id" picture.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
