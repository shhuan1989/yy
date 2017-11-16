package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Contract;
import com.yijia.yy.domain.ContractInstallment;
import com.yijia.yy.service.dto.ContractInstallmentDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DictionaryMapper.class})
@DecoratedWith(ContractInstallmentMapperDecorator.class)
public interface ContractInstallmentMapper {

    @Mapping(target = "contractId", source = "contract.id")
    ContractInstallmentDTO contractInstallmentToContractInstallmentDTO(ContractInstallment contractInstallment);

    List<ContractInstallmentDTO> contractInstallmentsToContractInstallmentDTOs(List<ContractInstallment> contracts);

    @Mapping(target = "contract.id", source = "contractId")
    ContractInstallment contractInstallmentDTOToContractInstallment(ContractInstallmentDTO contractDTO);

    List<ContractInstallment> contractInstallmentDTOsToContractInstallments(List<ContractInstallmentDTO> contractDTOs);

    default Contract contractFromId(Long id) {
        if (id == null) {
            return null;
        }

        Contract contract = new Contract();
        contract.setId(id);

        return contract;
    }
}
