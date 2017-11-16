package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.ContractInvoiceDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ContractInvoice.
 */
public interface ContractInvoiceService {

    /**
     * Save a contractInvoice.
     *
     * @param contractInvoiceDTO the entity to save
     * @return the persisted entity
     */
    ContractInvoiceDTO save(ContractInvoiceDTO contractInvoiceDTO);

    /**
     *  Get all the contractInvoices.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<ContractInvoiceDTO> findAll(Sort sort);

    /**
     *  Get all the contractInvoices.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<ContractInvoiceDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" contractInvoice.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ContractInvoiceDTO findOne(Long id);

    /**
     *  Delete the "id" contractInvoice.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
