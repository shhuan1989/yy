package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Authority;
import com.yijia.yy.domain.Client;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.JobTitle;
import com.yijia.yy.repository.ClientRepository;
import com.yijia.yy.security.SecurityUtils;
import com.yijia.yy.service.ClientService;
import com.yijia.yy.service.EmployeeService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.ClientDTO;
import com.yijia.yy.service.dto.EmployeeDTO;
import com.yijia.yy.service.mapper.ClientMapper;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Client.
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService{

    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Inject
    private ClientRepository clientRepository;

    @Inject
    private ClientMapper clientMapper;

    @Inject
    private UserService userService;

    @Inject
    private EmployeeService employeeService;

    /**
     * Save a client.
     *
     * @param clientDTO the entity to save
     * @return the persisted entity
     */
    public ClientDTO save(ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO);
        Client client = clientMapper.clientDTOToClient(clientDTO);
        client.setCreateTime(System.currentTimeMillis());
        client.setLastModifiedTime(System.currentTimeMillis());
        client.setLastModifier(SecurityUtils.getCurrentUserLogin());
        client.setInputOperator(SecurityUtils.getCurrentUserLogin());
        client.setOwner(SecurityUtils.getCurrentUserLogin());
        if (client.getId() != null) {
            Client ex = clientRepository.findOne(client.getId());
            if (ex != null) {
                client.setCreateTime(ex.getCreateTime());
            }
        }
        client = clientRepository.save(client);
        ClientDTO result = clientMapper.clientToClientDTO(client);
        return result;
    }

    /**
     *  Get all the clients.
     *
     *  @param sort
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ClientDTO> findAll(Sort sort, Boolean showAll) {
        log.debug("Request to get all Clients");
        return filter(
            clientRepository.findAll(sort).stream()
                .collect(Collectors.toList())
            , showAll
        );
    }

    /**
     * Get all client that match predicate
     * @param predicate
     * @param sort
     * @return
     */
    @Override
    public List<ClientDTO> findAll(Predicate predicate, Sort sort, Boolean showAll) {
        log.debug("Request to get all Clients by predicate");
        return filter(
            StreamSupport.stream(clientRepository.findAll(predicate, sort).spliterator(), false)
                .collect(Collectors.toList())
            , showAll
        );
    }

    private List<ClientDTO> filter(List<Client> clients, Boolean showAll) {
        if (BooleanUtils.toBoolean(showAll)) {
            return clients.stream()
                .map(client -> clientMapper.clientToClientDTO(client))
                .collect(Collectors.toList());
        }

        Employee ce = userService.currentLoginEmployee();
        boolean isPresident = ce.getJobTitles() != null &&
            ce.getJobTitles().stream()
                .filter(j -> j.getLevel() == 1)
                .count() > 1;

        if (!isPresident && (ce.getUser() == null || ce.getUser().getAuthorities() == null
            || !ce.getUser().getAuthorities().contains(Authority.ADMIN))) {
            List<EmployeeDTO> employees = employeeService.findAll(null);
            Set<Long> jobTitleIds = new HashSet<>();
            Queue<JobTitle> q = new ArrayDeque<>();
            ce.getJobTitles().forEach(j -> q.add(j));
            while (!q.isEmpty()) {
                JobTitle jb = q.poll();
                jobTitleIds.add(jb.getId());
                jb.getSubordinates().forEach(j -> q.add(j));
            }

            Set<String> logins =
                employees.stream()
                    .filter(e -> e.getDept() != null && e.getJobTitles() != null)
                    .filter(e -> (e.getDept() == null && ce.getDept() == null) || (e.getDept() != null && ce.getDept() != null && e.getDept().getId() == ce.getDept().getId()))
                    .filter(e -> {
                        Set<Long> jids = e.getJobTitles().stream().map(j -> j.getId()).collect(Collectors.toSet());
                        jids.retainAll(jobTitleIds);
                        return !jids.isEmpty();
                    })
                    .map(e -> e.getLogin())
                    .collect(Collectors.toSet());

            return clients.stream()
                .filter(client ->  logins.contains(client.getOwner()))
                .map(client -> clientMapper.clientToClientDTO(client))
                .collect(Collectors.toList());
        }

        return clients.stream()
            .map(client -> clientMapper.clientToClientDTO(client))
            .collect(Collectors.toList());
    }

    /**
     *  Get one client by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ClientDTO findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        Client client = clientRepository.findOne(id);
        ClientDTO clientDTO = clientMapper.clientToClientDTO(client);
        return clientDTO;
    }

    /**
     *  Delete the  client by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.delete(id);
    }
}
