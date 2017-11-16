package com.yijia.yy.service.impl;

import com.yijia.yy.domain.FileInfo;
import com.yijia.yy.domain.PictureInfo;
import com.yijia.yy.service.PictureService;
import com.yijia.yy.repository.PictureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Service Implementation for managing PictureInfo.
 */
@Service
@Transactional
public class PictureServiceImpl implements PictureService{

    private final Logger log = LoggerFactory.getLogger(PictureServiceImpl.class);

    @Inject
    private PictureRepository pictureRepository;

    /**
     * Save a pictureInfo.
     *
     * @param pictureInfo the entity to save
     * @return the persisted entity
     */
    public PictureInfo save(PictureInfo pictureInfo) {
        log.debug("Request to save PictureInfo : {}", pictureInfo);
        return pictureRepository.save(pictureInfo);
    }

    /**
     *  Get all the pictures.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PictureInfo> findAll(Pageable pageable) {
        log.debug("Request to get all Pictures");
        Page<PictureInfo> result = pictureRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one picture by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PictureInfo findOne(Long id) {
        log.debug("Request to get PictureInfo : {}", id);
        PictureInfo pictureInfo = pictureRepository.findOne(id);
        return pictureInfo;
    }

    /**
     *  Delete the  picture by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PictureInfo : {}", id);
        pictureRepository.delete(id);
    }
}
