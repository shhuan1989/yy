package com.yijia.yy.service.impl;

import com.yijia.yy.domain.City;
import com.yijia.yy.service.ProvinceService;
import com.yijia.yy.domain.Province;
import com.yijia.yy.repository.ProvinceRepository;
import com.yijia.yy.service.dto.ProvinceDTO;
import com.yijia.yy.service.mapper.ProvinceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Province.
 */
@Service
@Transactional
public class ProvinceServiceImpl implements ProvinceService{

    private final Logger log = LoggerFactory.getLogger(ProvinceServiceImpl.class);

    @Inject
    private ProvinceRepository provinceRepository;

    @Inject
    private ProvinceMapper provinceMapper;

    /**
     * Save a province.
     *
     * @param provinceDTO the entity to save
     * @return the persisted entity
     */
    public ProvinceDTO save(ProvinceDTO provinceDTO) {
        log.debug("Request to save Province : {}", provinceDTO);
        Province province = provinceMapper.provinceDTOToProvince(provinceDTO);
        province = provinceRepository.save(province);
        ProvinceDTO result = provinceMapper.provinceToProvinceDTO(province);
        return result;
    }

    /**
     *  Get all the provinces.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProvinceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Provinces");
        Page<Province> result = provinceRepository.findAll(pageable);
        return result.map(province -> provinceMapper.provinceToProvinceDTO(province));
    }

    /**
     *  Get one province by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProvinceDTO findOne(Long id) {
        log.debug("Request to get Province : {}", id);
        Province province = provinceRepository.findOne(id);
        ProvinceDTO provinceDTO = provinceMapper.provinceToProvinceDTO(province);
        return provinceDTO;
    }

    /**
     *  Delete the  province by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Province : {}", id);
        provinceRepository.delete(id);
    }
}
