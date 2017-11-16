package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Asset;
import com.yijia.yy.service.dto.AssetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Asset and its DTO AssetDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AssetMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")
    AssetDTO assetToAssetDTO(Asset asset);

    List<AssetDTO> assetsToAssetDTOs(List<Asset> assets);

    @Mapping(source = "ownerId", target = "owner.id")
    Asset assetDTOToAsset(AssetDTO assetDTO);

    List<Asset> assetDTOsToAssets(List<AssetDTO> assetDTOs);
}
