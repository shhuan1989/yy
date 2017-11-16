package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.ShootConfigs;
import com.yijia.yy.service.dto.ShootConfigsDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Created by palad on 2017/1/8.
 */
@Mapper(componentModel = "spring", uses = {ShootConfigMapper.class})
public interface ShootConfigsMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.idNumber", target = "projectIdNumber")
    @Mapping(source = "project.name", target = "projectName")
    ShootConfigsDTO shootConfigToShootConfigsDTO(ShootConfigs shootConfigs);

    List<ShootConfigsDTO> shootConfigsToShootConfigsDTOs(List<ShootConfigs> shootConfigs);

    @Mapping(source = "projectId", target = "project")
    ShootConfigs ShootConfigsDTOToShootConfig(ShootConfigsDTO shootConfigsDTO);

    List<ShootConfigs> shootConfigsDTOsToShootConfigs(List<ShootConfigsDTO> shootConfigsDTOs);

}
