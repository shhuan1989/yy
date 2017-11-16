package com.yijia.yy.service;

import com.yijia.yy.service.dto.DictionaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Dictionary.
 */
public interface DictionaryService {

    /**
     * Save a dictionary.
     *
     * @param dictionaryDTO the entity to save
     * @return the persisted entity
     */
    DictionaryDTO save(DictionaryDTO dictionaryDTO);

    /**
     *  Get all the dictionaries.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DictionaryDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" dictionary.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DictionaryDTO findOne(Long id);

    /**
     *  Delete the "id" dictionary.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<DictionaryDTO> findByCategory(String category);
}
