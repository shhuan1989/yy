package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.RoomDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Room and its DTO RoomDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoomMapper {

    RoomDTO roomToRoomDTO(Room room);

    List<RoomDTO> roomsToRoomDTOs(List<Room> rooms);

    Room roomDTOToRoom(RoomDTO roomDTO);

    List<Room> roomDTOsToRooms(List<RoomDTO> roomDTOs);
}
