package com.yijia.yy.service.impl;

import com.yijia.yy.domain.PictureInfo;
import com.yijia.yy.repository.PictureInfoRepository;
import com.yijia.yy.service.PictureInfoService;
import com.yijia.yy.service.dto.PictureInfoDTO;
import com.yijia.yy.service.mapper.PictureInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service Implementation for managing PictureInfoDTO.
 */
@Service
@Transactional
public class PictureInfoServiceImpl implements PictureInfoService{

    private final Logger log = LoggerFactory.getLogger(PictureInfoServiceImpl.class);

    @Inject
    private PictureInfoRepository pictureInfoRepository;

    @Inject
    private PictureInfoMapper mapper;

    /**
     * Save a pictureInfo.
     *
     * @param pictureInfo the entity to save
     * @return the persisted entity
     */
    public PictureInfoDTO save(PictureInfo pictureInfo) {
        log.debug("Request to save PictureInfoDTO : {}", pictureInfo);
        PictureInfo result = pictureInfoRepository.save(pictureInfo);
        return mapper.pictureInfoToPictureInfoDTO(result);
    }

    /**
     *  Get all the pictureInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PictureInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PictureInfos");
        Page<PictureInfo> result = pictureInfoRepository.findAll(pageable);
        return result.map(info -> mapper.pictureInfoToPictureInfoDTO(info));
    }

    /**
     *  Get one pictureInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PictureInfoDTO findOne(Long id) {
        log.debug("Request to get PictureInfoDTO : {}", id);
        PictureInfo pictureInfo = pictureInfoRepository.findOne(id);
        return mapper.pictureInfoToPictureInfoDTO(pictureInfo);
    }

    /**
     *  Delete the  pictureInfo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PictureInfoDTO : {}", id);
        pictureInfoRepository.delete(id);
    }
}
