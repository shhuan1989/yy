package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ProvinceDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Province and its DTO ProvinceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProvinceMapper {

    ProvinceDTO provinceToProvinceDTO(Province province);

    List<ProvinceDTO> provincesToProvinceDTOs(List<Province> provinces);

    @Mapping(target = "cities", ignore = true)
    Province provinceDTOToProvince(ProvinceDTO provinceDTO);

    List<Province> provinceDTOsToProvinces(List<ProvinceDTO> provinceDTOs);
}
