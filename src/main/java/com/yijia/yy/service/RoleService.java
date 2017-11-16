package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.RoleDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Role.
 */
public interface RoleService {

    /**
     * Save a role.
     *
     * @param roleDTO the entity to save
     * @return the persisted entity
     */
    RoleDTO save(RoleDTO roleDTO);

    /**
     *  Get all the roles.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<RoleDTO> findAll(Sort sort);

    /**
     *  Get all the roles.
     *
     *  @predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<RoleDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" role.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RoleDTO findOne(Long id);

    /**
     *  Delete the "id" role.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
