package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Dictionary;
import com.yijia.yy.service.dto.DictionaryDTO;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class DictionaryMapperDecorator implements DictionaryMapper{

    @Inject
    @Qualifier("delegate")
    private DictionaryMapper delegate;

    @Override
    public DictionaryDTO dictionaryToDictionaryDTO(Dictionary dictionary) {
        if (dictionary == null) {
            return null;
        }

        DictionaryDTO dto = delegate.dictionaryToDictionaryDTO(dictionary);
        if (dictionary.getChildren() != null) {
            dto.setChildren(
                dictionary.getChildren().stream()
                    .map(d -> dictionaryToDictionaryDTO(d))
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }

    @Override
    public List<DictionaryDTO> dictionariesToDictionaryDTOs(List<Dictionary> dictionaries) {
        if (dictionaries == null) {
            return null;
        }

        return dictionaries.stream()
            .map(d -> dictionaryToDictionaryDTO(d))
            .collect(Collectors.toList());
    }

    @Override
    public Dictionary dictionaryDTOToDictionary(DictionaryDTO dictionaryDTO) {
        if (dictionaryDTO == null) {
            return null;
        }

        Dictionary dictionary = delegate.dictionaryDTOToDictionary(dictionaryDTO);
        if (dictionaryDTO.getChildren() != null) {
            dictionary.setChildren(
                dictionaryDTO.getChildren().stream()
                .map(d -> dictionaryDTOToDictionary(d))
                .collect(Collectors.toList())
            );
        }

        return dictionary;
    }

    @Override
    public List<Dictionary> dictionaryDTOsToDictionaries(List<DictionaryDTO> dictionaryDTOs) {
        if (dictionaryDTOs == null) {
            return null;
        }

        return dictionaryDTOs.stream()
            .map(d -> dictionaryDTOToDictionary(d))
            .collect(Collectors.toList());
    }
}
