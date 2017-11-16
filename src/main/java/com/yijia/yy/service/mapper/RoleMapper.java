package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.RoleDTO;

import org.mapstruct.*;
import java.util.List;
import java.util.Set;

/**
 * Mapper for the entity Role and its DTO RoleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "lastModifier.id", target = "lastModifierId")
    RoleDTO roleToRoleDTO(Role role);

    List<RoleDTO> rolesToRoleDTOs(List<Role> roles);

    Set<RoleDTO> rolesToRoleDTOs(Set<Role> roles);

    @Mapping(source = "creatorId", target = "creator")
    @Mapping(source = "lastModifierId", target = "lastModifier")
    Role roleDTOToRole(RoleDTO roleDTO);

    List<Role> roleDTOsToRoles(List<RoleDTO> roleDTOs);

    Set<Role> roleDTOsToRoles(Set<RoleDTO> roleDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
