package com.yijia.yy.service;

import com.yijia.yy.domain.PictureInfo;
import com.yijia.yy.service.dto.PictureInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PictureInfo.
 */
public interface PictureInfoService {

    /**
     * Save a pictureInfo.
     *
     * @param pictureInfo the entity to save
     * @return the persisted entity
     */
    PictureInfoDTO save(PictureInfo pictureInfo);

    /**
     *  Get all the pictureInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PictureInfoDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" pictureInfo.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PictureInfoDTO findOne(Long id);

    /**
     *  Delete the "id" pictureInfo.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
