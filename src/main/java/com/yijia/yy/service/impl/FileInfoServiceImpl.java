package com.yijia.yy.service.impl;

import com.yijia.yy.service.FileInfoService;
import com.yijia.yy.domain.FileInfo;
import com.yijia.yy.repository.FileInfoRepository;
import com.yijia.yy.service.dto.FileInfoDTO;
import com.yijia.yy.service.mapper.FileInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing FileInfo.
 */
@Service
@Transactional
public class FileInfoServiceImpl implements FileInfoService{

    private final Logger log = LoggerFactory.getLogger(FileInfoServiceImpl.class);

    @Inject
    private FileInfoRepository fileInfoRepository;

    @Inject
    private FileInfoMapper fileInfoMapper;

    /**
     * Save a fileInfo.
     *
     * @param fileInfoDTO the entity to save
     * @return the persisted entity
     */
    public FileInfoDTO save(FileInfoDTO fileInfoDTO) {
        log.debug("Request to save FileInfo : {}", fileInfoDTO);
        FileInfo fileInfo = fileInfoMapper.fileInfoDTOToFileInfo(fileInfoDTO);
        fileInfo = fileInfoRepository.save(fileInfo);
        FileInfoDTO result = fileInfoMapper.fileInfoToFileInfoDTO(fileInfo);
        result.setType(FileInfo.TYPE_FILE);
        return result;
    }

    /**
     *  Get all the fileInfos.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FileInfoDTO> findAll() {
        log.debug("Request to get all FileInfos");
        List<FileInfoDTO> result = fileInfoRepository.findAll().stream()
            .map(fileInfoMapper::fileInfoToFileInfoDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one fileInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public FileInfoDTO findOne(Long id) {
        log.debug("Request to get FileInfo : {}", id);
        FileInfo fileInfo = fileInfoRepository.findOne(id);
        FileInfoDTO fileInfoDTO = fileInfoMapper.fileInfoToFileInfoDTO(fileInfo);
        return fileInfoDTO;
    }

    /**
     *  Delete the  fileInfo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FileInfo : {}", id);
        fileInfoRepository.delete(id);
    }
}
