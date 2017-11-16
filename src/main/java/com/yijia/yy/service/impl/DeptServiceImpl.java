package com.yijia.yy.service.impl;

import com.yijia.yy.service.DeptService;
import com.yijia.yy.domain.Dept;
import com.yijia.yy.repository.DeptRepository;
import com.yijia.yy.service.dto.DeptDTO;
import com.yijia.yy.service.mapper.DeptMapper;
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
 * Service Implementation for managing Dept.
 */
@Service
@Transactional
public class DeptServiceImpl implements DeptService{

    private final Logger log = LoggerFactory.getLogger(DeptServiceImpl.class);
    
    @Inject
    private DeptRepository deptRepository;

    @Inject
    private DeptMapper deptMapper;

    /**
     * Save a dept.
     *
     * @param deptDTO the entity to save
     * @return the persisted entity
     */
    public DeptDTO save(DeptDTO deptDTO) {
        log.debug("Request to save Dept : {}", deptDTO);
        Dept dept = deptMapper.deptDTOToDept(deptDTO);
        dept = deptRepository.save(dept);
        DeptDTO result = deptMapper.deptToDeptDTO(dept);
        return result;
    }

    /**
     *  Get all the depts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<DeptDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Depts");
        Page<Dept> result = deptRepository.findAll(pageable);
        return result.map(dept -> deptMapper.deptToDeptDTO(dept));
    }

    /**
     *  Get one dept by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DeptDTO findOne(Long id) {
        log.debug("Request to get Dept : {}", id);
        Dept dept = deptRepository.findOne(id);
        DeptDTO deptDTO = deptMapper.deptToDeptDTO(dept);
        return deptDTO;
    }

    /**
     *  Delete the  dept by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Dept : {}", id);
        deptRepository.delete(id);
    }
}
