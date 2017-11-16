package com.yijia.yy.service.mapper;


import com.yijia.yy.domain.Actor;
import com.yijia.yy.domain.Dictionary;
import com.yijia.yy.domain.converter.GenderConverter;
import com.yijia.yy.service.dto.ActorDTO;
import com.yijia.yy.service.dto.DictionaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.stream.Collectors;

public class ActorMapperDecorator implements ActorMapper {

    @Autowired
    @Qualifier("delegate")
    private ActorMapper delegate;

    @Override
    public ActorDTO actorToActorDTO(Actor actor) {
        if (actor == null) {
            return null;
        }

        ActorDTO dto = delegate.actorToActorDTO(actor);
        dto.setGender(new DictionaryDTO().withId(Long.valueOf(actor.getGender().ordinal())).withName(actor.getGender().toString()));
        return dto;
    }

    @Override
    public List<ActorDTO> actorsToActorDTOs(List<Actor> actors) {
        if (actors == null) {
            return null;
        }
        return actors.stream().map(a -> actorToActorDTO(a))
        .collect(Collectors.toList());
    }

    @Override
    public Actor actorDTOToActor(ActorDTO actorDTO) {
        if (actorDTO == null) {
            return null;
        }

        Actor actor = delegate.actorDTOToActor(actorDTO);
        if (actorDTO.getSourceId() != null) {
            actor.setSource(new Dictionary().id(actorDTO.getId()));
        }
        if (actorDTO.getCountryId() != null) {
            actor.setCountry(new Dictionary().id(actorDTO.getCountryId()));
        }
        actor.setGender(new GenderConverter().convertToEntityAttribute(Math.toIntExact(actorDTO.getGender().getId())));

        return actor;
    }

    @Override
    public List<Actor> actorDTOsToActors(List<ActorDTO> actorDTOs) {
        if (actorDTOs == null) {
            return null;
        }
        return actorDTOs.stream().map(a -> actorDTOToActor(a))
            .collect(Collectors.toList());
    }
}
