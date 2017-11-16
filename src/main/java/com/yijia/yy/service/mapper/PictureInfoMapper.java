package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.PictureInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PictureInfo and its DTO PictureInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
@DecoratedWith(PictureInfoMapperDecorator.class)
public interface PictureInfoMapper {

    PictureInfoDTO pictureInfoToPictureInfoDTO(PictureInfo pictureInfo);

    List<PictureInfoDTO> pictureInfosToPictureInfoDTOs(List<PictureInfo> pictureInfos);

    PictureInfo pictureInfoDTOToPictureInfo(PictureInfoDTO pictureInfoDTO);

    List<PictureInfo> pictureInfoDTOsToPictureInfos(List<PictureInfoDTO> pictureInfoDTOs);
}
