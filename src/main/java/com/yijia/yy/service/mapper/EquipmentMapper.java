package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.EquipmentDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Equipment and its DTO EquipmentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EquipmentMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    EquipmentDTO equipmentToEquipmentDTO(Equipment equipment);

    List<EquipmentDTO> equipmentToEquipmentDTOs(List<Equipment> equipment);

    @Mapping(source = "categoryId", target = "category")
    Equipment equipmentDTOToEquipment(EquipmentDTO equipmentDTO);

    List<Equipment> equipmentDTOsToEquipment(List<EquipmentDTO> equipmentDTOs);

    default Dictionary dictionaryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        return dictionary;
    }
}
