package com.yijia.yy.service.mapper;


import com.yijia.yy.domain.FileInfo;
import com.yijia.yy.domain.PictureInfo;
import com.yijia.yy.service.dto.PictureInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.stream.Collectors;

public class PictureInfoMapperDecorator implements PictureInfoMapper{
    @Autowired
    @Qualifier("delegate")
    private PictureInfoMapper delegate;

    @Override
    public PictureInfoDTO pictureInfoToPictureInfoDTO(PictureInfo pictureInfo) {
        if (pictureInfo == null) {
            return null;
        }
        PictureInfoDTO dto = delegate.pictureInfoToPictureInfoDTO(pictureInfo);
        dto.setType(FileInfo.TYPE_IMAGE);
        return dto;
    }

    @Override
    public List<PictureInfoDTO> pictureInfosToPictureInfoDTOs(List<PictureInfo> pictureInfos) {
        if (pictureInfos == null) {
            return null;
        }

        return pictureInfos.stream()
            .map(p -> pictureInfoToPictureInfoDTO(p))
            .collect(Collectors.toList());
    }

    @Override
    public PictureInfo pictureInfoDTOToPictureInfo(PictureInfoDTO pictureInfoDTO) {
        if (pictureInfoDTO == null) {
            return null;
        }
        return delegate.pictureInfoDTOToPictureInfo(pictureInfoDTO);
    }

    @Override
    public List<PictureInfo> pictureInfoDTOsToPictureInfos(List<PictureInfoDTO> pictureInfoDTOs) {
        if (pictureInfoDTOs == null) {
            return null;
        }

        return delegate.pictureInfoDTOsToPictureInfos(pictureInfoDTOs);
    }
}
