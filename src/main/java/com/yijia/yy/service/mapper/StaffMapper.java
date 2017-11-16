package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.StaffDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Staff and its DTO StaffDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StaffMapper {

    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "type.name", target = "typeName")
    StaffDTO staffToStaffDTO(Staff staff);

    List<StaffDTO> staffToStaffDTOs(List<Staff> staff);

    @Mapping(source = "typeId", target = "type.id")
    Staff staffDTOToStaff(StaffDTO staffDTO);

    List<Staff> staffDTOsToStaff(List<StaffDTO> staffDTOs);

    default Dictionary dictionaryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        return dictionary;
    }
}
