package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.domain.enumeration.Gender;
import com.yijia.yy.service.dto.ActorDTO;

import com.yijia.yy.service.dto.DictionaryDTO;
import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Actor and its DTO ActorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
@DecoratedWith(ActorMapperDecorator.class)
public interface ActorMapper {

    @Mapping(source = "source.id", target = "sourceId")
    @Mapping(source = "source.name", target = "sourceName")
    @Mapping(target = "gender", ignore = true)
    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.name", target = "countryName")
    ActorDTO actorToActorDTO(Actor actor);

    List<ActorDTO> actorsToActorDTOs(List<Actor> actors);

    @Mapping(target = "source", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "country", ignore = true)
    Actor actorDTOToActor(ActorDTO actorDTO);

    List<Actor> actorDTOsToActors(List<ActorDTO> actorDTOs);
}
