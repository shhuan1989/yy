package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Contract;
import com.yijia.yy.domain.ContractInstallment;
import com.yijia.yy.domain.converter.ContractLevelConverter;
import com.yijia.yy.domain.converter.ContractPaymentStatusConverter;
import com.yijia.yy.domain.enumeration.ContractLevel;
import com.yijia.yy.domain.enumeration.ContractPaymentStatus;
import com.yijia.yy.service.dto.ContractDTO;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.util.DomainObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class ContractMapperDecorator implements ContractMapper {

    @Inject
    @Qualifier("delegate")
    private ContractMapper delegate;


    @Override
    public ContractDTO contractToContractDTO(Contract contract) {
        if (contract == null) {
            return null;
        }

        ContractDTO dto = delegate.contractToContractDTO(contract);
        if (contract.getPaymentStatus() != null) {
            dto.setPaymentStatus(
                new DictionaryDTO()
                .withId(Long.valueOf(contract.getPaymentStatus().ordinal()))
                .withName(contract.getPaymentStatus().toString())
            );
        }

        if (contract.getPaymentStatus() != ContractPaymentStatus.DONE
            && contract.getInstallments() != null) {
            contract.getInstallments().stream()
                .filter(c -> c.getActualAmount() == null)
                .findFirst()
                .ifPresent(c -> {
                    dto.setNextInstallmentAmount(c.getAmount());
                    dto.setNextInstallmentETA(c.getEta());
                });
        }

        if (contract.getMoneyAmount() != null) {
            if (contract.getInvoicedAmount() == null) {
                dto.setInvoicedAmount(0.0);
                dto.setNotInvoicedAmount(contract.getMoneyAmount());
            } else {
                dto.setNotInvoicedAmount(contract.getMoneyAmount() - contract.getInvoicedAmount());
            }
        }

        dto.setLevel(
            new DictionaryDTO()
            .withId(Long.valueOf(contract.getLevel().ordinal()))
            .withName(contract.getLevel().toString())
        );

        return dto;
    }

    @Override
    public List<ContractDTO> contractsToContractDTOs(List<Contract> contracts) {
        if (contracts == null) {
            return null;
        }

        return contracts.stream()
            .map(c -> contractToContractDTO(c))
            .collect(Collectors.toList());
    }

    @Override
    public Contract contractDTOToContract(ContractDTO contractDTO) {
        if (contractDTO == null) {
            return null;
        }

        Contract contract = delegate.contractDTOToContract(contractDTO);
        DomainObjectUtils.setEnumFromDictionaryDTO(contractDTO.getPaymentStatus(), contract, "paymentStatus", ContractPaymentStatus.class, ContractPaymentStatusConverter.class);
        DomainObjectUtils.setEnumFromDictionaryDTO(contractDTO.getLevel(), contract, "level", ContractLevel.class,
            ContractLevelConverter.class);
        return contract;
    }

    @Override
    public List<Contract> contractDTOsToContracts(List<ContractDTO> contractDTOs) {
        if (contractDTOs == null) {
            return null;
        }

        return contractDTOs.stream()
            .map(c -> contractDTOToContract(c))
            .collect(Collectors.toList());
    }
}
