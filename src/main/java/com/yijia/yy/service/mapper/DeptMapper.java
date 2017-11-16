package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.DeptDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Dept and its DTO DeptDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeptMapper {

    DeptDTO deptToDeptDTO(Dept dept);

    List<DeptDTO> deptsToDeptDTOs(List<Dept> depts);

    Dept deptDTOToDept(DeptDTO deptDTO);

    List<Dept> deptDTOsToDepts(List<DeptDTO> deptDTOs);
}
