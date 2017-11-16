package com.yijia.yy.service.mapper;


import com.yijia.yy.domain.ContractInstallment;
import com.yijia.yy.service.dto.ContractInstallmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.List;
import java.util.stream.Collectors;

public class ContractInstallmentMapperDecorator implements ContractInstallmentMapper{
    @Autowired
    @Qualifier("delegate")
    private ContractInstallmentMapper delegate;

    @Override
    public ContractInstallmentDTO contractInstallmentToContractInstallmentDTO(ContractInstallment contractInstallment) {
        return delegate.contractInstallmentToContractInstallmentDTO(contractInstallment);
    }

    @Override
    public List<ContractInstallmentDTO> contractInstallmentsToContractInstallmentDTOs(List<ContractInstallment> contracts) {
        return delegate.contractInstallmentsToContractInstallmentDTOs(contracts);
    }

    @Override
    public ContractInstallment contractInstallmentDTOToContractInstallment(ContractInstallmentDTO contractDTO) {
        if (contractDTO == null) {
            return null;
        }

        ContractInstallment contractInstallment = delegate.contractInstallmentDTOToContractInstallment(contractDTO);
        if (contractDTO.getContractId() == null) {
            contractInstallment.setContract(null);
        }

        return contractInstallment;
    }

    @Override
    public List<ContractInstallment> contractInstallmentDTOsToContractInstallments(List<ContractInstallmentDTO> contractDTOs) {
        if (contractDTOs == null) {
            return null;
        }

        return contractDTOs.stream()
            .map(c -> contractInstallmentDTOToContractInstallment(c))
            .collect(Collectors.toList());
    }
}
