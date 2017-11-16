package com.yijia.yy.service.impl;

import com.codepoetics.protonpack.StreamUtils;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Contract;
import com.yijia.yy.domain.ContractInstallment;
import com.yijia.yy.domain.enumeration.ContractPaymentStatus;
import com.yijia.yy.repository.ContractRepository;
import com.yijia.yy.service.ContractService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.ContractDTO;
import com.yijia.yy.service.mapper.ContractMapper;
import com.yijia.yy.service.util.DomainObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Contract.
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService{

    private final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

    @Inject
    private ContractRepository contractRepository;

    @Inject
    private ContractMapper contractMapper;

    @Inject
    private UserService userService;

    /**
     * Save a contract.
     *
     * @param contractDTO the entity to save
     * @return the persisted entity
     */
    public synchronized ContractDTO save(ContractDTO contractDTO) {
        log.debug("Request to save Contract : {}", contractDTO);

        Contract contract = contractMapper.contractDTOToContract(contractDTO);
        if (contract.getId() == null) {
            if (contractRepository.findTopByIdNumber(contractDTO.getIdNumber()) != null) {
                throw new IllegalArgumentException(contractDTO.getIdNumber() + "已存在，合同编号不能重复");
            }

            contract.setCreateTime(System.currentTimeMillis());
            contract.setCreator(userService.currentLoginEmployee());
        }
        contract.setLastModifiedTime(System.currentTimeMillis());
        contract.setLastModifier(userService.currentLoginEmployee());

        // only update non-null fields
        if (contract.getId() != null) {
            Contract oldContract = contractRepository.findOne(contract.getId());
            DomainObjectUtils.copyNonNullFields(contract, oldContract);
            contract = oldContract;
        }

        if (contract.getInstallments() != null) {
            final Contract c = contract;

            c.getInstallments().forEach(i -> {
                if (i.getPayMethod() != null && i.getPayMethod().getId() == null) {
                    i.setPayMethod(null);
                }
            });

            Stream<ContractInstallment> installmentStream = c.getInstallments().stream()
                .sorted();
            StreamUtils.zipWithIndex(installmentStream)
                .forEach(i -> i.getValue().setOrderNumber(Math.toIntExact(i.getIndex())));

            contract.getInstallments().forEach(i -> i.setContract(c));
            boolean paidOff = contract.getInstallments().stream()
                .noneMatch(i -> i.getActualAmount() == null || Math.abs(i.getActualAmount() - i.getAmount()) > 0.0001);

            // may add new installment later
            contract.setPaymentStatus(paidOff ? ContractPaymentStatus.DONE : ContractPaymentStatus.IN_PROGRESS);
            if (!paidOff) {
                contract.setNextInstallmentETA(contract.getInstallments().get(0).getEta());
            }

        } else {
            contract.setPaymentStatus(ContractPaymentStatus.DONE);
        }

        contract = contractRepository.save(contract);
        ContractDTO result = contractMapper.contractToContractDTO(contract);
        return result;
    }

    /**
     *  Get all the contracts.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ContractDTO> findAll(Sort sort) {
        log.debug("Request to get all Contracts");
        List<Contract> result = contractRepository.findAll(sort);
        result.forEach(c -> updateContractPaymentStatus(c));
        return result.stream()
            .map(contract -> contractMapper.contractToContractDTO(contract))
            .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<ContractDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all Contracts");

        List<Contract> contracts = StreamSupport.stream(contractRepository.findAll(predicate, sort).spliterator(), false)
            .collect(Collectors.toList());
        contracts.forEach(c -> {
            sortInstallments(c);
            updateContractPaymentStatus(c);
        });

        return contracts.stream()
            .map(contract -> contractMapper.contractToContractDTO(contract))
            .collect(Collectors.toList());
    }


    /**
     *  Get one contract by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ContractDTO findOne(Long id) {
        log.debug("Request to get Contract : {}", id);
        Contract contract = contractRepository.findOne(id);
        sortInstallments(contract);
        ContractDTO contractDTO = contractMapper.contractToContractDTO(contract);
        return contractDTO;
    }

    /**
     *  Delete the  contract by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Contract : {}", id);
        contractRepository.delete(id);
    }

    /**
     *  Pay next installment of contract "id"
     *
     *  @param id the id of the entity
     *  @param amount the actual amount
     *  @param payTime the pay time
     */
    @Override
    public ContractDTO payNextInstallment(Long id, Double amount, Long payTime) {
        Contract contract = contractRepository.findOne(id);
        if (contract == null) {
            throw new IllegalArgumentException("合同不存在");
        }

        if (contract.getInstallments() == null) {
            throw new IllegalArgumentException("没有合同的分期信息");
        }

        sortInstallments(contract);
        List<ContractInstallment> installments = contract.getInstallments();

        int lastPaidIndex = -1;
        for (int i = 0; i < installments.size(); i++) {
            ContractInstallment installment = installments.get(i);
            if (installment.getActualAmount() == null) {
                installment.setActualAmount(amount);
                installment.setActualPayTime(payTime);
                lastPaidIndex = i;
                break;
            }
        }

        if (lastPaidIndex >= 0) {
            if (lastPaidIndex < installments.size() - 1) {
                ContractInstallment nextInstallment = installments.get(lastPaidIndex + 1);
                contract.setNextInstallmentETA(nextInstallment.getEta());
                contract.setPaymentStatus(ContractPaymentStatus.IN_PROGRESS);
            } else {
                contract.setPaymentStatus(ContractPaymentStatus.DONE);
            }

            ContractDTO dto = contractMapper.contractToContractDTO(contractRepository.save(contract));
            return dto;
        } else {
            throw new IllegalArgumentException("没有未付款的分期");
        }
    }

    private void sortInstallments(Contract contract) {
        if (contract.getInstallments() != null) {
            contract.setInstallments(contract.getInstallments().stream().sorted().collect(Collectors.toList()));
        }
    }

    private void updateContractPaymentStatus(Contract contract) {
        if (contract == null) {
            return;
        }
        if (contract.getPaymentStatus() == ContractPaymentStatus.IN_PROGRESS && contract.getInstallments() != null) {
            contract.getInstallments().stream()
                .filter(c -> c.getActualAmount() == null)
                .findFirst()
                .ifPresent(c -> {
                    if (c.getEta() < System.currentTimeMillis()) {
                        contract.setPaymentStatus(ContractPaymentStatus.OVERDUE);
                    }
                });
        }
    }
}
