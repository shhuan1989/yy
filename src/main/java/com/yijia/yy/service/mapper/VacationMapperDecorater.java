package com.yijia.yy.service.mapper;


import com.yijia.yy.domain.Vacation;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.dto.VacationDTO;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class VacationMapperDecorater implements VacationMapper{

    @Inject
    @Qualifier("delegate")
    private VacationMapper delegate;

    @Override
    public VacationDTO vacationToVacationDTO(Vacation vacation) {
        if (vacation == null) {
            return null;
        }

        VacationDTO dto = delegate.vacationToVacationDTO(vacation);
        if (vacation.getApprovalRequest() != null) {
            dto.setApprovalStatus(
                new DictionaryDTO()
                .withId(Long.valueOf(vacation.getApprovalRequest().getStatus().ordinal()))
                .withName(vacation.getApprovalRequest().getStatus().toString())
            );
        }

        return dto;
    }

    @Override
    public List<VacationDTO> vacationsToVacationDTOs(List<Vacation> vacations) {
        if (vacations == null) {
            return null;
        }

        return vacations.stream()
            .map(v -> vacationToVacationDTO(v))
            .collect(Collectors.toList());
    }

    @Override
    public Vacation vacationDTOToVacation(VacationDTO vacationDTO) {
        return delegate.vacationDTOToVacation(vacationDTO);
    }

    @Override
    public List<Vacation> vacationDTOsToVacations(List<VacationDTO> vacationDTOs) {
        return delegate.vacationDTOsToVacations(vacationDTOs);
    }
}
