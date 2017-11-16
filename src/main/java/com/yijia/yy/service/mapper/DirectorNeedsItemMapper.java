package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.DirectorNeedsItemDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity DirectorNeedsItem and its DTO DirectorNeedsItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DirectorNeedsItemMapper {

    @Mapping(source = "directorNeeds.id", target = "directorNeedsId")
    DirectorNeedsItemDTO directorNeedsItemToDirectorNeedsItemDTO(DirectorNeedsItem directorNeedsItem);

    List<DirectorNeedsItemDTO> directorNeedsItemsToDirectorNeedsItemDTOs(List<DirectorNeedsItem> directorNeedsItems);

    @Mapping(source = "directorNeedsId", target = "directorNeeds")
    DirectorNeedsItem directorNeedsItemDTOToDirectorNeedsItem(DirectorNeedsItemDTO directorNeedsItemDTO);

    List<DirectorNeedsItem> directorNeedsItemDTOsToDirectorNeedsItems(List<DirectorNeedsItemDTO> directorNeedsItemDTOs);

    default DirectorNeeds directorNeedsFromId(Long id) {
        if (id == null) {
            return null;
        }
        DirectorNeeds directorNeeds = new DirectorNeeds();
        directorNeeds.setId(id);
        return directorNeeds;
    }
}
