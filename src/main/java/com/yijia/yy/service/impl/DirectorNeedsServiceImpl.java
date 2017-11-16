package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.DirectorNeedsService;
import com.yijia.yy.domain.DirectorNeeds;
import com.yijia.yy.repository.DirectorNeedsRepository;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.DirectorNeedsDTO;
import com.yijia.yy.service.mapper.DirectorNeedsMapper;
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
 * Service Implementation for managing DirectorNeeds.
 */
@Service
@Transactional
public class DirectorNeedsServiceImpl implements DirectorNeedsService{

    private final Logger log = LoggerFactory.getLogger(DirectorNeedsServiceImpl.class);

    @Inject
    private DirectorNeedsRepository directorNeedsRepository;

    @Inject
    private DirectorNeedsMapper directorNeedsMapper;

    @Inject
    private UserService userService;

    /**
     * Save a directorNeeds.
     *
     * @param directorNeedsDTO the entity to save
     * @return the persisted entity
     */
    public DirectorNeedsDTO save(DirectorNeedsDTO directorNeedsDTO) {
        log.debug("Request to save DirectorNeeds : {}", directorNeedsDTO);
        DirectorNeeds directorNeeds = directorNeedsMapper.directorNeedsDTOToDirectorNeeds(directorNeedsDTO);

        if (directorNeeds.getCreateTime() == null) {
            directorNeeds.setCreateTime(System.currentTimeMillis());
        }

        if (directorNeeds.getCreator() == null || directorNeeds.getCreator().getId() == null) {
            directorNeeds.setCreator(userService.currentLoginEmployee());
        }

        final DirectorNeeds d = directorNeeds;
        if (directorNeeds.getItems() != null) {
            directorNeeds.getItems().forEach(i -> i.setDirectorNeeds(d));
        }
        if (directorNeeds.getComments() != null) {
            directorNeeds.getComments().forEach(c -> {
                if (c.getCreator() == null || c.getCreator().getId() == null) {
                    c.setCreator(userService.currentLoginEmployee());
                }
                if (c.getCreateTime() == null) {
                    c.setCreateTime(System.currentTimeMillis());
                }
                c.setDirectorNeeds(d);
            });
        }

        directorNeeds = directorNeedsRepository.save(directorNeeds);
        DirectorNeedsDTO result = directorNeedsMapper.directorNeedsToDirectorNeedsDTO(directorNeeds);
        return result;
    }

    /**
     *  Get all the directorNeeds.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<DirectorNeedsDTO> findAll(Sort sort) {
        log.debug("Request to get all DirectorNeeds by order {}", sort);
        List<DirectorNeedsDTO> result = directorNeedsRepository.findAll(sort).stream()
            .map(directorNeedsMapper::directorNeedsToDirectorNeedsDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the directorNeeds.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    public List<DirectorNeedsDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all DirectorNeeds predicate {} and by order {}", predicate, sort);

        return StreamSupport.stream(directorNeedsRepository.findAll(predicate, sort).spliterator(), false)
            .map(directorNeedsMapper::directorNeedsToDirectorNeedsDTO)
            .collect(Collectors.toList());
    }

    /**
     *  Get one directorNeeds by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public DirectorNeedsDTO findOne(Long id) {
        log.debug("Request to get DirectorNeeds : {}", id);
        DirectorNeeds directorNeeds = directorNeedsRepository.findOne(id);
        DirectorNeedsDTO directorNeedsDTO = directorNeedsMapper.directorNeedsToDirectorNeedsDTO(directorNeeds);
        return directorNeedsDTO;
    }

    /**
     *  Delete the  directorNeeds by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DirectorNeeds : {}", id);
        directorNeedsRepository.delete(id);
    }
}
