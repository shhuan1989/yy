package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ContractInvoiceDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ContractInvoice and its DTO ContractInvoiceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContractInvoiceMapper {

    @Mapping(source = "contract.id", target = "contractId")
    @Mapping(source = "creator.id", target = "creatorId")
    ContractInvoiceDTO contractInvoiceToContractInvoiceDTO(ContractInvoice contractInvoice);

    List<ContractInvoiceDTO> contractInvoicesToContractInvoiceDTOs(List<ContractInvoice> contractInvoices);

    @Mapping(source = "contractId", target = "contract")
    @Mapping(source = "creatorId", target = "creator")
    ContractInvoice contractInvoiceDTOToContractInvoice(ContractInvoiceDTO contractInvoiceDTO);

    List<ContractInvoice> contractInvoiceDTOsToContractInvoices(List<ContractInvoiceDTO> contractInvoiceDTOs);

    default Contract contractFromId(Long id) {
        if (id == null) {
            return null;
        }
        Contract contract = new Contract();
        contract.setId(id);
        return contract;
    }

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
