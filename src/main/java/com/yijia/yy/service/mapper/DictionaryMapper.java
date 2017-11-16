package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.DictionaryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Dictionary and its DTO DictionaryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
@DecoratedWith(DictionaryMapperDecorator.class)
public interface DictionaryMapper {

    @Mapping(source = "parent.id", target = "parentId")
    DictionaryDTO dictionaryToDictionaryDTO(Dictionary dictionary);

    List<DictionaryDTO> dictionariesToDictionaryDTOs(List<Dictionary> dictionaries);

    @Mapping(source = "parentId", target = "parent.id")
    @Mapping(target = "children", ignore = true)
    Dictionary dictionaryDTOToDictionary(DictionaryDTO dictionaryDTO);

    List<Dictionary> dictionaryDTOsToDictionaries(List<DictionaryDTO> dictionaryDTOs);

    default Dictionary dictionaryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        return dictionary;
    }
}
