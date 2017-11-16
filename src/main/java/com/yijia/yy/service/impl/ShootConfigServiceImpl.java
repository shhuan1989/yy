package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.ShootConfig;
import com.yijia.yy.service.ShootConfigService;
import com.yijia.yy.repository.ShootConfigRepository;
import com.yijia.yy.service.UtilService;
import com.yijia.yy.service.dto.ShootConfigDTO;
import com.yijia.yy.service.mapper.ShootConfigMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing ShootConfig.
 */
@Service
@Transactional
public class ShootConfigServiceImpl implements ShootConfigService {

    private final Logger log = LoggerFactory.getLogger(ShootConfigServiceImpl.class);

    @Inject
    private ShootConfigRepository shootConfigRepository;

    @Inject
    private ShootConfigMapper shootConfigMapper;

    @Inject
    private UtilService utilService;

    /**
     * Save a shootConfigs.
     *
     * @param shootConfigDTO the entity to save
     * @return the persisted entity
     */
    public ShootConfigDTO save(ShootConfigDTO shootConfigDTO) {
        log.debug("Request to save ShootConfig : {}", shootConfigDTO);
        ShootConfig shootConfig = shootConfigMapper.shootConfigDTOToShootConfig(shootConfigDTO);
        utilService.initApprovalItem(shootConfig);
        if (shootConfig.getType() == null) {
            shootConfig.setType(0);
        }
        if (StringUtils.isBlank(shootConfig.getConfigCorrelationId())) {
            shootConfig.setConfigCorrelationId(UUID.randomUUID().toString());
        }
        shootConfig.setLastModifiedTime(System.currentTimeMillis());

        if (shootConfig.getConfigItems() != null) {
            final ShootConfig s = shootConfig;
            shootConfig.getConfigItems().forEach(c -> {
                c.setShootConfig(s);
                if (c.getActualAmount() == null) {
                    c.setActualAmount(c.getAmount());
                }
                if (c.getUnitPrice() == null) {
                    c.setUnitPrice(c.getDefaultUnitPrice());
                }
                if (c.getActualDays() == null) {
                    c.setActualDays(c.getDays());
                }

                if (c.getActualCost() == null) {
                    Double cost = 1.0;
                    if (c.getActualDays() != null) {
                        cost *= c.getActualDays();
                    }
                    if (c.getActualAmount() != null) {
                        cost *= c.getActualAmount();
                    }
                    if (c.getUnitPrice() != null) {
                        cost *= c.getUnitPrice();
                    }
                    c.setActualCost(cost);
                }
                if (c.getCost() == null) {
                    Double cost = 1.0;
                    if (c.getDays() != null) {
                        cost *= c.getDays();
                    } else if (c.getActualDays() != null) {
                        cost *= c.getActualDays();
                    }
                    if (c.getDefaultUnitPrice() != null) {
                        cost *= c.getDefaultUnitPrice();
                    } else if (c.getUnitPrice() != null) {
                        cost *= c.getUnitPrice();
                    }
                    if (c.getAmount() != null) {
                        cost *= c.getAmount();
                    } else if (c.getActualAmount() != null) {
                        cost *= c.getActualAmount();
                    }
                    c.setCost(cost);
                }
            });

            if (shootConfig.getActualCost() == null) {
                shootConfig.setActualCost(
                    shootConfig.getConfigItems().stream()
                        .filter(c -> c.getActualCost() != null)
                        .mapToDouble(c -> c.getActualCost())
                        .sum()
                );
            }

            if (shootConfig.getBudget() == null || shootConfig.getBudget().doubleValue() < 0.0001) {
                shootConfig.setBudget(
                    shootConfig.getConfigItems().stream()
                    .filter(c -> c.getCost() != null)
                    .mapToDouble(c -> c.getCost())
                    .sum()
                );
            }
        }

        shootConfig = shootConfigRepository.save(shootConfig);
        ShootConfigDTO result = shootConfigMapper.shootConfigToShootConfigDTO(shootConfig);
        return result;
    }

    /**
     *  Get all the shootConfigs.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ShootConfigDTO> findAll(Sort sort) {
        log.debug("Request to get all ShootConfig");
        List<ShootConfigDTO> result = shootConfigRepository.findAll(sort).stream()
            .map(shootConfigMapper::shootConfigToShootConfigDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the shootConfigs.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ShootConfigDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all ShootConfig");
        List<ShootConfigDTO> result = StreamSupport.stream(shootConfigRepository.findAll(predicate, sort).spliterator(), false)
            .map(shootConfigMapper::shootConfigToShootConfigDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one shootConfigs by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ShootConfigDTO findOne(Long id) {
        log.debug("Request to get ShootConfig : {}", id);
        ShootConfig shootConfig = shootConfigRepository.findOne(id);
        ShootConfigDTO shootConfigDTO = shootConfigMapper.shootConfigToShootConfigDTO(shootConfig);
        return shootConfigDTO;
    }

    /**
     *  Delete the  shootConfigs by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ShootConfig : {}", id);
        shootConfigRepository.delete(id);
    }
}
