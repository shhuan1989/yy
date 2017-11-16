package com.yijia.yy.service.mapper;


import com.yijia.yy.domain.OvertimeWork;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.dto.OvertimeWorkDTO;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class OvertimeWorkMapperDecorator implements OvertimeWorkMapper{

    @Inject
    @Qualifier("delegate")
    private OvertimeWorkMapper delegate;

    @Override
    public OvertimeWorkDTO overtimeWorkToOvertimeWorkDTO(OvertimeWork overtimeWork) {
        if (overtimeWork == null) {
            return null;
        }

        OvertimeWorkDTO dto = delegate.overtimeWorkToOvertimeWorkDTO(overtimeWork);
        if (overtimeWork.getApprovalRequest() != null) {
            dto.setApprovalStatus(
                new DictionaryDTO()
                    .withId(Long.valueOf(overtimeWork.getApprovalRequest().getStatus().ordinal()))
                    .withName(overtimeWork.getApprovalRequest().getStatus().toString())
            );
        }

        return dto;
    }

    @Override
    public List<OvertimeWorkDTO> overtimeWorksToOvertimeWorkDTOs(List<OvertimeWork> overtimeWorks) {
        if (overtimeWorks == null) {
            return null;
        }

        return overtimeWorks.stream()
            .map(v -> overtimeWorkToOvertimeWorkDTO(v))
            .collect(Collectors.toList());
    }

    @Override
    public OvertimeWork overtimeWorkDTOToOvertimeWork(OvertimeWorkDTO overtimeWorkDTO) {
        return delegate.overtimeWorkDTOToOvertimeWork(overtimeWorkDTO);
    }

    @Override
    public List<OvertimeWork> overtimeWorkDTOsToOvertimeWorks(List<OvertimeWorkDTO> overtimeWorkDTOs) {
        return delegate.overtimeWorkDTOsToOvertimeWorks(overtimeWorkDTOs);
    }
}
