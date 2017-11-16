package com.yijia.yy.service;

import com.yijia.yy.service.dto.FileInfoDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing FileInfo.
 */
public interface FileInfoService {

    /**
     * Save a fileInfo.
     *
     * @param fileInfoDTO the entity to save
     * @return the persisted entity
     */
    FileInfoDTO save(FileInfoDTO fileInfoDTO);

    /**
     *  Get all the fileInfos.
     *  
     *  @return the list of entities
     */
    List<FileInfoDTO> findAll();

    /**
     *  Get the "id" fileInfo.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FileInfoDTO findOne(Long id);

    /**
     *  Delete the "id" fileInfo.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
