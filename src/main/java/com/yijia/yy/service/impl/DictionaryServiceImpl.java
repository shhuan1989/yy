package com.yijia.yy.service.impl;

import com.yijia.yy.security.SecurityUtils;
import com.yijia.yy.service.DictionaryService;
import com.yijia.yy.domain.Dictionary;
import com.yijia.yy.repository.DictionaryRepository;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.mapper.DictionaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Dictionary.
 */
@Service
@Transactional
public class DictionaryServiceImpl implements DictionaryService{

    private final Logger log = LoggerFactory.getLogger(DictionaryServiceImpl.class);

    @Inject
    private DictionaryRepository dictionaryRepository;

    @Inject
    private DictionaryMapper dictionaryMapper;

    /**
     * Save a dictionary.
     *
     * @param dictionaryDTO the entity to save
     * @return the persisted entity
     */
    public DictionaryDTO save(DictionaryDTO dictionaryDTO) {
        log.debug("Request to save Dictionary : {}", dictionaryDTO);
        Dictionary dictionary = dictionaryMapper.dictionaryDTOToDictionary(dictionaryDTO);
        dictionary.setLastModifiedTime(System.currentTimeMillis());
        dictionary.setIsSystem(false);
        dictionary.setLastModifier(SecurityUtils.getCurrentUserLogin());

        if (dictionary.getId() != null) {
            dictionary.setCreator(SecurityUtils.getCurrentUserLogin());
            dictionary.setCreateTime(System.currentTimeMillis());

            Dictionary ex = dictionaryRepository.findOne(dictionary.getId());
            dictionary.setIsSystem(ex.isIsSystem());
            dictionary.setCreator(ex.getCreator());
            if (ex.getCreateTime() != null) {
                dictionary.setCreateTime(System.currentTimeMillis());
            } else {
                dictionary.setCreateTime(ex.getCreateTime());
            }
        }

        if (dictionary.getParent() != null && dictionary.getParent().getId() == null) {
            dictionary.setParent(null);
        }
        if (dictionary.getChildren() != null) {
            final Dictionary fd = dictionary;
            dictionary.getChildren().forEach(d -> d.setParent(fd));
        }

        dictionary = dictionaryRepository.save(dictionary);
        DictionaryDTO result = dictionaryMapper.dictionaryToDictionaryDTO(dictionary);
        return result;
    }

    /**
     *  Get all the dictionaries.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DictionaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dictionaries");
        Page<Dictionary> result = dictionaryRepository.findAll(pageable);
        return result.map(dictionary -> dictionaryMapper.dictionaryToDictionaryDTO(dictionary));
    }

    /**
     *  Get one dictionary by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public DictionaryDTO findOne(Long id) {
        log.debug("Request to get Dictionary : {}", id);
        Dictionary dictionary = dictionaryRepository.findOne(id);
        DictionaryDTO dictionaryDTO = dictionaryMapper.dictionaryToDictionaryDTO(dictionary);
        return dictionaryDTO;
    }

    /**
     *  Delete the  dictionary by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Dictionary : {}", id);
        dictionaryRepository.delete(id);
    }

    @Override
    public List<DictionaryDTO> findByCategory(String category) {
        Dictionary root = dictionaryRepository.findTopByNameAndParentIsNull(category);
        DictionaryDTO dto = dictionaryMapper.dictionaryToDictionaryDTO(root);
        return dto != null ? Arrays.asList(dto) : null;
    }
}
