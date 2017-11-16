package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.CityDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity City and its DTO CityDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CityMapper {

    @Mapping(source = "province.id", target = "provinceId")
    CityDTO cityToCityDTO(City city);

    List<CityDTO> citiesToCityDTOs(List<City> cities);

    @Mapping(source = "provinceId", target = "province")
    City cityDTOToCity(CityDTO cityDTO);

    List<City> cityDTOsToCities(List<CityDTO> cityDTOs);

    default Province provinceFromId(Long id) {
        if (id == null) {
            return null;
        }
        Province province = new Province();
        province.setId(id);
        return province;
    }
}
