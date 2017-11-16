package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.ShootAgendaService;
import com.yijia.yy.domain.ShootAgenda;
import com.yijia.yy.repository.ShootAgendaRepository;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.ShootAgendaDTO;
import com.yijia.yy.service.mapper.ShootAgendaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing ShootAgenda.
 */
@Service
@Transactional
public class ShootAgendaServiceImpl implements ShootAgendaService{

    private final Logger log = LoggerFactory.getLogger(ShootAgendaServiceImpl.class);

    @Inject
    private ShootAgendaRepository shootAgendaRepository;

    @Inject
    private ShootAgendaMapper shootAgendaMapper;

    @Inject
    private UserService userService;

    /**
     * Save a shootAgenda.
     *
     * @param shootAgendaDTO the entity to save
     * @return the persisted entity
     */
    public ShootAgendaDTO save(ShootAgendaDTO shootAgendaDTO) {
        log.debug("Request to save ShootAgenda : {}", shootAgendaDTO);
        ShootAgenda shootAgenda = shootAgendaMapper.shootAgendaDTOToShootAgenda(shootAgendaDTO);

        if (shootAgenda.getCreator() == null || shootAgenda.getCreator().getId() == null) {
            shootAgenda.setCreator(userService.currentLoginEmployee());
        }

        shootAgenda = shootAgendaRepository.save(shootAgenda);
        ShootAgendaDTO result = shootAgendaMapper.shootAgendaToShootAgendaDTO(shootAgenda);
        return result;
    }

    /**
     *  Get all the shootAgenda.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ShootAgendaDTO> findAll(Sort sort) {
        log.debug("Request to get all ShootAgenda");
        List<ShootAgendaDTO> result = shootAgendaRepository.findAll().stream()
            .map(shootAgendaMapper::shootAgendaToShootAgendaDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the shootAgenda.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShootAgendaDTO> findAll(Predicate predicate, Sort sort) {
        return StreamSupport.stream(shootAgendaRepository.findAll(predicate, sort).spliterator(), false)
            .map(shootAgendaMapper::shootAgendaToShootAgendaDTO)
            .collect(Collectors.toList());
    }

    /**
     *  Get one shootAgenda by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ShootAgendaDTO findOne(Long id) {
        log.debug("Request to get ShootAgenda : {}", id);
        ShootAgenda shootAgenda = shootAgendaRepository.findOne(id);
        ShootAgendaDTO shootAgendaDTO = shootAgendaMapper.shootAgendaToShootAgendaDTO(shootAgenda);
        return shootAgendaDTO;
    }

    /**
     *  Delete the  shootAgenda by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ShootAgenda : {}", id);
        shootAgendaRepository.delete(id);
    }
}
