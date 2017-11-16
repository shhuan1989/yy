package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.ApprovalRequest;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.OvertimeWork;
import com.yijia.yy.service.dto.OvertimeWorkDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity OvertimeWork and its DTO OvertimeWorkDTO.
 */
@Mapper(componentModel = "spring", uses = {ApprovalRequestMapper.class, DictionaryMapper.class, EmployeeMapper.class})
@DecoratedWith(OvertimeWorkMapperDecorator.class)
public interface OvertimeWorkMapper {

    @Mapping(target = "approvalStatus", ignore = true)
    OvertimeWorkDTO overtimeWorkToOvertimeWorkDTO(OvertimeWork overtimeWork);

    List<OvertimeWorkDTO> overtimeWorksToOvertimeWorkDTOs(List<OvertimeWork> overtimeWorks);

    OvertimeWork overtimeWorkDTOToOvertimeWork(OvertimeWorkDTO overtimeWorkDTO);

    List<OvertimeWork> overtimeWorkDTOsToOvertimeWorks(List<OvertimeWorkDTO> overtimeWorkDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }

    default ApprovalRequest approvalRequestFromId(Long id) {
        if (id == null) {
            return null;
        }
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setId(id);
        return approvalRequest;
    }
}
