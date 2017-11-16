package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.EquipmentService;
import com.yijia.yy.domain.Equipment;
import com.yijia.yy.repository.EquipmentRepository;
import com.yijia.yy.service.dto.EquipmentDTO;
import com.yijia.yy.service.mapper.EquipmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Equipment.
 */
@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService{

    private final Logger log = LoggerFactory.getLogger(EquipmentServiceImpl.class);

    @Inject
    private EquipmentRepository equipmentRepository;

    @Inject
    private EquipmentMapper equipmentMapper;

    /**
     * Save a equipment.
     *
     * @param equipmentDTO the entity to save
     * @return the persisted entity
     */
    public EquipmentDTO save(EquipmentDTO equipmentDTO) {
        log.debug("Request to save Equipment : {}", equipmentDTO);
        Equipment equipment = equipmentMapper.equipmentDTOToEquipment(equipmentDTO);
        equipment = equipmentRepository.save(equipment);
        EquipmentDTO result = equipmentMapper.equipmentToEquipmentDTO(equipment);
        return result;
    }

    /**
     *  Get all the equipment.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EquipmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Equipment");
        Page<Equipment> result = equipmentRepository.findAll(pageable);
        return result.map(equipment -> equipmentMapper.equipmentToEquipmentDTO(equipment));
    }

    /**
     *  Get one equipment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EquipmentDTO findOne(Long id) {
        log.debug("Request to get Equipment : {}", id);
        Equipment equipment = equipmentRepository.findOne(id);
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);
        return equipmentDTO;
    }

    /**
     *  Delete the  equipment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Equipment : {}", id);
        equipmentRepository.delete(id);
    }

    @Override
    public Page<EquipmentDTO> findAll(Predicate predicate, Pageable pageable) {
        log.debug("Request to get all Equipment by predicate ");
        Page<Equipment> result = equipmentRepository.findAll(predicate, pageable);
        return result.map(equipment -> equipmentMapper.equipmentToEquipmentDTO(equipment));
    }
}
