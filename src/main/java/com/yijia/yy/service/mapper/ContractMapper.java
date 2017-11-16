package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Contract;
import com.yijia.yy.service.dto.ContractDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Contract and its DTO ContractDTO.
 */
@Mapper(componentModel = "spring", uses = {FileInfoMapper.class, FileInfoMapper.class, EmployeeMapper.class, ProjectMapper.class, ContractInstallmentMapper.class})
@DecoratedWith(ContractMapperDecorator.class)
public interface ContractMapper {

    @Mapping(target = "lastModifierId", source = "lastModifier.id")
    @Mapping(target = "creatorId", source = "creator.id")
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "nextInstallmentAmount", ignore = true)
    @Mapping(target = "notInvoicedAmount", ignore = true)
    @Mapping(target = "level", ignore = true)
    ContractDTO contractToContractDTO(Contract contract);

    List<ContractDTO> contractsToContractDTOs(List<Contract> contracts);

    @Mapping(target = "creator.id", source = "creatorId")
    @Mapping(target = "lastModifier.id", source = "lastModifierId")
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "level", ignore = true)
    Contract contractDTOToContract(ContractDTO contractDTO);

    List<Contract> contractDTOsToContracts(List<ContractDTO> contractDTOs);
}
