package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.RoleService;
import com.yijia.yy.domain.Role;
import com.yijia.yy.repository.RoleRepository;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.RoleDTO;
import com.yijia.yy.service.mapper.RoleMapper;
import com.yijia.yy.service.util.DomainObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Role.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService{

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private RoleMapper roleMapper;

    @Inject
    private UserService userService;

    /**
     * Save a role.
     *
     * @param roleDTO the entity to save
     * @return the persisted entity
     */
    public RoleDTO save(RoleDTO roleDTO) {
        log.debug("Request to save Role : {}", roleDTO);
        Role role = roleMapper.roleDTOToRole(roleDTO);

        if (role.getId() == null) {
            role.setCreateTime(System.currentTimeMillis());
            role.setCreator(userService.currentLoginEmployee());
        }
        role.setLastModifiedTime(System.currentTimeMillis());
        role.setLastModifier(userService.currentLoginEmployee());

        role.setAuths(DomainObjectUtils.uniqueItemInStringSplitByComma(role.getAuths()));

        role = roleRepository.save(role);
        RoleDTO result = roleMapper.roleToRoleDTO(role);
        return result;
    }

    /**
     *  Get all the roles.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<RoleDTO> findAll(Sort sort) {
        log.debug("Request to get all Roles");
        List<RoleDTO> result = roleRepository.findAll(sort).stream()
            .map(roleMapper::roleToRoleDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }


    /**
     *  Get all the roles.
     *
     *  @predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    public List<RoleDTO> findAll(Predicate predicate, Sort sort) {
        if (predicate == null) {
            return findAll(sort);
        }

        return StreamSupport.stream(roleRepository.findAll(predicate, sort).spliterator(), false)
            .map(roleMapper::roleToRoleDTO)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one role by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RoleDTO findOne(Long id) {
        log.debug("Request to get Role : {}", id);
        Role role = roleRepository.findOne(id);
        RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);
        return roleDTO;
    }

    /**
     *  Delete the  role by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Role : {}", id);
        roleRepository.delete(id);
    }
}
