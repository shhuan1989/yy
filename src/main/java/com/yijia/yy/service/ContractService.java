package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.ContractDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Service Interface for managing Contract.
 */
public interface ContractService {

    /**
     * Save a contract.
     *
     * @param contractDTO the entity to save
     * @return the persisted entity
     */
    ContractDTO save(ContractDTO contractDTO);

    /**
     *  Get all the contracts.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<ContractDTO> findAll(Sort sort);

    List<ContractDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" contract.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ContractDTO findOne(Long id);

    /**
     *  Delete the "id" contract.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *  Pay next installment of contract "id"
     *
     *  @param id the id of the entity
     *  @param amount the actual amount
     *  @param payTime the pay time
     */
    ContractDTO payNextInstallment(Long id, Double amount, Long payTime);
}
