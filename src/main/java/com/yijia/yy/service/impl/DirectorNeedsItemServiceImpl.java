package com.yijia.yy.service.impl;

import com.yijia.yy.service.DirectorNeedsItemService;
import com.yijia.yy.domain.DirectorNeedsItem;
import com.yijia.yy.repository.DirectorNeedsItemRepository;
import com.yijia.yy.service.dto.DirectorNeedsItemDTO;
import com.yijia.yy.service.mapper.DirectorNeedsItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing DirectorNeedsItem.
 */
@Service
@Transactional
public class DirectorNeedsItemServiceImpl implements DirectorNeedsItemService{

    private final Logger log = LoggerFactory.getLogger(DirectorNeedsItemServiceImpl.class);
    
    @Inject
    private DirectorNeedsItemRepository directorNeedsItemRepository;

    @Inject
    private DirectorNeedsItemMapper directorNeedsItemMapper;

    /**
     * Save a directorNeedsItem.
     *
     * @param directorNeedsItemDTO the entity to save
     * @return the persisted entity
     */
    public DirectorNeedsItemDTO save(DirectorNeedsItemDTO directorNeedsItemDTO) {
        log.debug("Request to save DirectorNeedsItem : {}", directorNeedsItemDTO);
        DirectorNeedsItem directorNeedsItem = directorNeedsItemMapper.directorNeedsItemDTOToDirectorNeedsItem(directorNeedsItemDTO);
        directorNeedsItem = directorNeedsItemRepository.save(directorNeedsItem);
        DirectorNeedsItemDTO result = directorNeedsItemMapper.directorNeedsItemToDirectorNeedsItemDTO(directorNeedsItem);
        return result;
    }

    /**
     *  Get all the directorNeedsItems.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<DirectorNeedsItemDTO> findAll() {
        log.debug("Request to get all DirectorNeedsItems");
        List<DirectorNeedsItemDTO> result = directorNeedsItemRepository.findAll().stream()
            .map(directorNeedsItemMapper::directorNeedsItemToDirectorNeedsItemDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one directorNeedsItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DirectorNeedsItemDTO findOne(Long id) {
        log.debug("Request to get DirectorNeedsItem : {}", id);
        DirectorNeedsItem directorNeedsItem = directorNeedsItemRepository.findOne(id);
        DirectorNeedsItemDTO directorNeedsItemDTO = directorNeedsItemMapper.directorNeedsItemToDirectorNeedsItemDTO(directorNeedsItem);
        return directorNeedsItemDTO;
    }

    /**
     *  Delete the  directorNeedsItem by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DirectorNeedsItem : {}", id);
        directorNeedsItemRepository.delete(id);
    }
}
