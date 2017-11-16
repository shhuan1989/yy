package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.StaffService;
import com.yijia.yy.domain.Staff;
import com.yijia.yy.repository.StaffRepository;
import com.yijia.yy.service.dto.StaffDTO;
import com.yijia.yy.service.mapper.StaffMapper;
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
 * Service Implementation for managing Staff.
 */
@Service
@Transactional
public class StaffServiceImpl implements StaffService{

    private final Logger log = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Inject
    private StaffRepository staffRepository;

    @Inject
    private StaffMapper staffMapper;

    /**
     * Save a staff.
     *
     * @param staffDTO the entity to save
     * @return the persisted entity
     */
    public StaffDTO save(StaffDTO staffDTO) {
        log.debug("Request to save Staff : {}", staffDTO);
        Staff staff = staffMapper.staffDTOToStaff(staffDTO);
        staff = staffRepository.save(staff);
        StaffDTO result = staffMapper.staffToStaffDTO(staff);
        return result;
    }

    /**
     *  Get all the staff.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StaffDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Staff");
        Page<Staff> result = staffRepository.findAll(pageable);
        return result.map(staff -> staffMapper.staffToStaffDTO(staff));
    }

    /**
     *  Get all the staff by predicate.
     *
     *  @param predicate the predicate
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    @Override
    public Page<StaffDTO> findAll(Predicate predicate, Pageable pageable) {
        log.debug("Request to get all Staff by predicate");
        Page<Staff> result = staffRepository.findAll(predicate, pageable);
        return result.map(staff -> staffMapper.staffToStaffDTO(staff));
    }

    /**
     *  Get one staff by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public StaffDTO findOne(Long id) {
        log.debug("Request to get Staff : {}", id);
        Staff staff = staffRepository.findOne(id);
        StaffDTO staffDTO = staffMapper.staffToStaffDTO(staff);
        return staffDTO;
    }

    /**
     *  Delete the  staff by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Staff : {}", id);
        staffRepository.delete(id);
    }
}
