package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Contract;
import com.yijia.yy.repository.ContractRepository;
import com.yijia.yy.service.ContractInvoiceService;
import com.yijia.yy.domain.ContractInvoice;
import com.yijia.yy.repository.ContractInvoiceRepository;
import com.yijia.yy.service.dto.ContractInvoiceDTO;
import com.yijia.yy.service.mapper.ContractInvoiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing ContractInvoice.
 */
@Service
@Transactional
public class ContractInvoiceServiceImpl implements ContractInvoiceService{

    private final Logger log = LoggerFactory.getLogger(ContractInvoiceServiceImpl.class);

    @Inject
    private ContractInvoiceRepository contractInvoiceRepository;

    @Inject
    private ContractInvoiceMapper contractInvoiceMapper;

    @Inject
    private ContractRepository contractRepository;

    /**
     * Save a contractInvoice.
     *
     * @param contractInvoiceDTO the entity to save
     * @return the persisted entity
     */
    public synchronized ContractInvoiceDTO save(ContractInvoiceDTO contractInvoiceDTO) {
        log.debug("Request to save ContractInvoice : {}", contractInvoiceDTO);

        if (contractInvoiceDTO == null) {
            throw new NullPointerException();
        }
        if (contractInvoiceDTO.getContractId() == null) {
            throw new IllegalArgumentException("合同ID不能为空");
        }

        if (contractInvoiceDTO.getAmount() == null) {
            throw new IllegalArgumentException("发票金额不能为空");
        }

        Contract contract = contractRepository.findOne(contractInvoiceDTO.getContractId());
        if (contract == null) {
            throw new IllegalArgumentException("未找到指定的合同");
        }

        contract.setInvoicedAmount((contract.getInvoicedAmount() != null ? contract.getInvoicedAmount() : 0)
            + contractInvoiceDTO.getAmount());
        contractRepository.save(contract);

        ContractInvoice contractInvoice = contractInvoiceMapper.contractInvoiceDTOToContractInvoice(contractInvoiceDTO);
        contractInvoice = contractInvoiceRepository.save(contractInvoice);
        ContractInvoiceDTO result = contractInvoiceMapper.contractInvoiceToContractInvoiceDTO(contractInvoice);
        return result;
    }

    /**
     *  Get all the contractInvoices.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ContractInvoiceDTO> findAll(Sort sort) {
        log.debug("Request to get all ContractInvoices");
        List<ContractInvoiceDTO> result = contractInvoiceRepository.findAll(sort).stream()
            .map(contractInvoiceMapper::contractInvoiceToContractInvoiceDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the contractInvoices.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ContractInvoiceDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all ContractInvoices");
        List<ContractInvoiceDTO> result = StreamSupport.stream(contractInvoiceRepository.findAll(predicate, sort).spliterator(), false)
            .map(contractInvoiceMapper::contractInvoiceToContractInvoiceDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one contractInvoice by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ContractInvoiceDTO findOne(Long id) {
        log.debug("Request to get ContractInvoice : {}", id);
        ContractInvoice contractInvoice = contractInvoiceRepository.findOne(id);
        ContractInvoiceDTO contractInvoiceDTO = contractInvoiceMapper.contractInvoiceToContractInvoiceDTO(contractInvoice);
        return contractInvoiceDTO;
    }

    /**
     *  Delete the  contractInvoice by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ContractInvoice : {}", id);
        contractInvoiceRepository.delete(id);
    }
}
