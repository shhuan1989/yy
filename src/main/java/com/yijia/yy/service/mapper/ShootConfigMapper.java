package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ShootConfigDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ShootConfig and its DTO ShootConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {ShootConfigItemMapper.class, EmployeeMapper.class, ApprovalRequestMapper.class})
@DecoratedWith(ShootConfigMapperDecorator.class)
public interface ShootConfigMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.idNumber", target = "projectIdNumber")
    @Mapping(source = "project.name", target = "projectName")
    @Mapping(source = "project.contract.idNumber", target = "contractIdNumber")
    @Mapping(source = "project.client.name", target = "clientName")
    @Mapping(source = "project.contract.moneyAmount", target = "contractMoney")
    @Mapping(target = "approvalStatus", ignore = true)
    ShootConfigDTO shootConfigToShootConfigDTO(ShootConfig shootConfig);

    List<ShootConfigDTO> shootConfigsToShootConfigsDTOs(List<ShootConfig> shootConfigs);

    @Mapping(source = "projectId", target = "project.id")
    ShootConfig shootConfigDTOToShootConfig(ShootConfigDTO shootConfigDTO);

    List<ShootConfig> shootConfigsDTOsToShootConfigs(List<ShootConfigDTO> shootConfigDTOs);

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
