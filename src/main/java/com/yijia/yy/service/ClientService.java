package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.ClientDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Service Interface for managing Client.
 */
public interface ClientService {

    /**
     * Save a client.
     *
     * @param clientDTO the entity to save
     * @return the persisted entity
     */
    ClientDTO save(ClientDTO clientDTO);

    /**
     *  Get all the clients.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<ClientDTO> findAll(Sort sort, Boolean showAll);

    /**
     *  Get the "id" client.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ClientDTO findOne(Long id);

    /**
     *  Delete the "id" client.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<ClientDTO> findAll(Predicate predicate, Sort sort, Boolean showAll);
}
