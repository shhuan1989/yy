package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ClientDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Client and its DTO ClientDTO.
 */
@Mapper(componentModel = "spring", uses = {DictionaryMapper.class})
public interface ClientMapper {
    ClientDTO clientToClientDTO(Client client);

    List<ClientDTO> clientsToClientDTOs(List<Client> clients);

    Client clientDTOToClient(ClientDTO clientDTO);

    List<Client> clientDTOsToClients(List<ClientDTO> clientDTOs);

    default Dictionary dictionaryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        return dictionary;
    }
}
