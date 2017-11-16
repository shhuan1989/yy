package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.ShootAgendaDTO;
import org.springframework.data.domain.Sort;
import java.util.List;

/**
 * Service Interface for managing ShootAgenda.
 */
public interface ShootAgendaService {

    /**
     * Save a shootAgenda.
     *
     * @param shootAgendaDTO the entity to save
     * @return the persisted entity
     */
    ShootAgendaDTO save(ShootAgendaDTO shootAgendaDTO);

    /**
     *  Get all the shootAgenda.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<ShootAgendaDTO> findAll(Sort sort);

    /**
     *  Get all the shootAgenda.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<ShootAgendaDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" shootAgenda.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ShootAgendaDTO findOne(Long id);

    /**
     *  Delete the "id" shootAgenda.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
