package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Asset;
import com.yijia.yy.repository.AssetRepository;
import com.yijia.yy.service.AssetService;
import com.yijia.yy.service.dto.AssetDTO;
import com.yijia.yy.service.mapper.AssetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Asset.
 */
@Service
@Transactional
public class AssetServiceImpl implements AssetService{

    private final Logger log = LoggerFactory.getLogger(AssetServiceImpl.class);

    @Inject
    private AssetRepository assetRepository;

    @Inject
    private AssetMapper assetMapper;

    /**
     * Save a asset.
     *
     * @param assetDTO the entity to save
     * @return the persisted entity
     */
    public AssetDTO save(AssetDTO assetDTO) {
        log.debug("Request to save Asset : {}", assetDTO);
        Asset asset = assetMapper.assetDTOToAsset(assetDTO);
        if (asset.getId() == null) {
            asset.setCreateTime(System.currentTimeMillis());
        } else {
            asset.setLastModifiedTime(System.currentTimeMillis());
        }

        if (asset.getOwner() != null && asset.getOwner().getId() == null) {
            asset.setOwner(null);
        }

        asset = assetRepository.save(asset);
        AssetDTO result = assetMapper.assetToAssetDTO(asset);
        return result;
    }

    /**
     *  Get all the assets.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AssetDTO> findAll(Sort sort) {
        log.debug("Request to get all Assets");
        List<AssetDTO> result = assetRepository.findAll(sort).stream()
            .map(assetMapper::assetToAssetDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the assets.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AssetDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all Assets");
        List<AssetDTO> result = StreamSupport.stream(assetRepository.findAll(predicate, sort).spliterator(), false)
            .map(assetMapper::assetToAssetDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one asset by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AssetDTO findOne(Long id) {
        log.debug("Request to get Asset : {}", id);
        Asset asset = assetRepository.findOne(id);
        AssetDTO assetDTO = assetMapper.assetToAssetDTO(asset);
        return assetDTO;
    }

    /**
     *  Delete the  asset by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Asset : {}", id);
        assetRepository.delete(id);
    }
}
