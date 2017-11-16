package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.FileInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity FileInfo and its DTO FileInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FileInfoMapper {

    FileInfoDTO fileInfoToFileInfoDTO(FileInfo fileInfo);

    List<FileInfoDTO> fileInfosToFileInfoDTOs(List<FileInfo> fileInfos);

    FileInfo fileInfoDTOToFileInfo(FileInfoDTO fileInfoDTO);

    List<FileInfo> fileInfoDTOsToFileInfos(List<FileInfoDTO> fileInfoDTOs);
}
