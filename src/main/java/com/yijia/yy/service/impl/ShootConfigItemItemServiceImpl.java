package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.ShootConfigItem;
import com.yijia.yy.service.ShootConfigItemService;
import com.yijia.yy.repository.ShootConfigItemRepository;
import com.yijia.yy.service.dto.ShootConfigItemDTO;
import com.yijia.yy.service.mapper.ShootConfigItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing ShootConfigItem.
 */
@Service
@Transactional
public class ShootConfigItemItemServiceImpl implements ShootConfigItemService {

    private final Logger log = LoggerFactory.getLogger(ShootConfigItemItemServiceImpl.class);

    @Inject
    private ShootConfigItemRepository shootConfigItemRepository;

    @Inject
    private ShootConfigItemMapper shootConfigItemMapper;

    /**
     * Save a shootConfig.
     *
     * @param shootConfigItemDTO the entity to save
     * @return the persisted entity
     */
    public ShootConfigItemDTO save(ShootConfigItemDTO shootConfigItemDTO) {
        log.debug("Request to save ShootConfigItem : {}", shootConfigItemDTO);
        ShootConfigItem shootConfigItem = shootConfigItemMapper.shootConfigDTOToShootConfig(shootConfigItemDTO);
        shootConfigItem = shootConfigItemRepository.save(shootConfigItem);
        ShootConfigItemDTO result = shootConfigItemMapper.shootConfigToShootConfigDTO(shootConfigItem);
        return result;
    }

    /**
     *  Get all the shootConfigs.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ShootConfigItemDTO> findAll(Sort sort) {
        log.debug("Request to get all ShootConfig");
        List<ShootConfigItemDTO> result = shootConfigItemRepository.findAll(sort).stream()
            .map(shootConfigItemMapper::shootConfigToShootConfigDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the shootConfigs.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ShootConfigItemDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all ShootConfig");
        List<ShootConfigItemDTO> result = StreamSupport.stream(shootConfigItemRepository.findAll(predicate, sort).spliterator(), false)
            .map(shootConfigItemMapper::shootConfigToShootConfigDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one shootConfig by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ShootConfigItemDTO findOne(Long id) {
        log.debug("Request to get ShootConfigItem : {}", id);
        ShootConfigItem shootConfigItem = shootConfigItemRepository.findOne(id);
        ShootConfigItemDTO shootConfigItemDTO = shootConfigItemMapper.shootConfigToShootConfigDTO(shootConfigItem);
        return shootConfigItemDTO;
    }

    /**
     *  Delete the  shootConfig by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ShootConfigItem : {}", id);
        shootConfigItemRepository.delete(id);
    }
}
