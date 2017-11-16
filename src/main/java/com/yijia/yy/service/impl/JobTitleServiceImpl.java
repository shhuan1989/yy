package com.yijia.yy.service.impl;

import com.yijia.yy.service.JobTitleService;
import com.yijia.yy.domain.JobTitle;
import com.yijia.yy.repository.JobTitleRepository;
import com.yijia.yy.service.dto.JobTitleDTO;
import com.yijia.yy.service.mapper.JobTitleMapper;
import org.apache.commons.lang.BooleanUtils;
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
 * Service Implementation for managing JobTitle.
 */
@Service
@Transactional
public class JobTitleServiceImpl implements JobTitleService{

    private final Logger log = LoggerFactory.getLogger(JobTitleServiceImpl.class);

    @Inject
    private JobTitleRepository jobTitleRepository;

    @Inject
    private JobTitleMapper jobTitleMapper;

    /**
     * Save a jobTitle.
     *
     * @param jobTitleDTO the entity to save
     * @return the persisted entity
     */
    public JobTitleDTO save(JobTitleDTO jobTitleDTO) {
        log.debug("Request to save JobTitle : {}", jobTitleDTO);
        JobTitle jobTitle = jobTitleMapper.jobTitleDTOToJobTitle(jobTitleDTO);
        if (jobTitle.getLevel() == null) {
            jobTitle.setLevel(JobTitle.LEVEL_STAFF);
        }

        if (jobTitle.getLeader() != null && jobTitle.getLeader().getId() == null) {
            jobTitle.setLeader(null);
        }

        jobTitle.setImmutable(BooleanUtils.toBoolean(jobTitleDTO.getImmutable()));

        jobTitle = jobTitleRepository.save(jobTitle);
        JobTitleDTO result = jobTitleMapper.jobTitleToJobTitleDTO(jobTitle);
        return result;
    }

    /**
     *  Get all the jobTitles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<JobTitleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobTitles");
        Page<JobTitle> result = jobTitleRepository.findAll(pageable);
        return result.map(jobTitle -> jobTitleMapper.jobTitleToJobTitleDTO(jobTitle));
    }

    /**
     *  Get one jobTitle by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public JobTitleDTO findOne(Long id) {
        log.debug("Request to get JobTitle : {}", id);
        JobTitle jobTitle = jobTitleRepository.findOne(id);
        JobTitleDTO jobTitleDTO = jobTitleMapper.jobTitleToJobTitleDTO(jobTitle);
        return jobTitleDTO;
    }

    /**
     *  Delete the  jobTitle by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete JobTitle : {}", id);
        jobTitleRepository.delete(id);
    }
}
