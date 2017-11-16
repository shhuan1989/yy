package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Vacation;
import com.yijia.yy.service.dto.VacationDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Vacation and its DTO VacationDTO.
 */
@Mapper(componentModel = "spring", uses = {ApprovalRequestMapper.class, DictionaryMapper.class, EmployeeMapper.class})
@DecoratedWith(VacationMapperDecorater.class)
public interface VacationMapper {

    @Mapping(target = "approvalStatus", ignore = true)
    VacationDTO vacationToVacationDTO(Vacation vacation);

    List<VacationDTO> vacationsToVacationDTOs(List<Vacation> vacations);

    Vacation vacationDTOToVacation(VacationDTO vacationDTO);

    List<Vacation> vacationDTOsToVacations(List<VacationDTO> vacationDTOs);
}
