package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.EquipmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Equipment.
 */
public interface EquipmentService {

    /**
     * Save a equipment.
     *
     * @param equipmentDTO the entity to save
     * @return the persisted entity
     */
    EquipmentDTO save(EquipmentDTO equipmentDTO);

    /**
     *  Get all the equipment.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EquipmentDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" equipment.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EquipmentDTO findOne(Long id);

    /**
     *  Delete the "id" equipment.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    Page<EquipmentDTO> findAll(Predicate predicate, Pageable pageable);
}
