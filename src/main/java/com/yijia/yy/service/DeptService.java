package com.yijia.yy.service;

import com.yijia.yy.service.dto.DeptDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Dept.
 */
public interface DeptService {

    /**
     * Save a dept.
     *
     * @param deptDTO the entity to save
     * @return the persisted entity
     */
    DeptDTO save(DeptDTO deptDTO);

    /**
     *  Get all the depts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DeptDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" dept.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DeptDTO findOne(Long id);

    /**
     *  Delete the "id" dept.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
