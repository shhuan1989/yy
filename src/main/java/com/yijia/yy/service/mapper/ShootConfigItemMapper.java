package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Project;
import com.yijia.yy.domain.ShootConfigItem;
import com.yijia.yy.service.dto.ShootConfigItemDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity ShootConfigItem and its DTO ShootConfigItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShootConfigItemMapper {

    ShootConfigItemDTO shootConfigToShootConfigDTO(ShootConfigItem shootConfigItem);

    List<ShootConfigItemDTO> shootConfigsToShootConfigDTOs(List<ShootConfigItem> shootConfigItems);

    @Mapping(target = "shootConfig", ignore = true)
    ShootConfigItem shootConfigDTOToShootConfig(ShootConfigItemDTO shootConfigItemDTO);

    List<ShootConfigItem> shootConfigDTOsToShootConfigs(List<ShootConfigItemDTO> shootConfigItemDTOs);

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
