package com.yijia.yy.service.mapper;


import com.yijia.yy.domain.ShootConfig;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.dto.ShootConfigDTO;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ShootConfigMapperDecorator implements ShootConfigMapper {

    @Inject
    @Qualifier("delegate")
    private ShootConfigMapper delegate;

    @Override
    public ShootConfigDTO shootConfigToShootConfigDTO(ShootConfig shootConfig) {
        if (shootConfig == null) {
            return null;
        }

        ShootConfigDTO dto = delegate.shootConfigToShootConfigDTO(shootConfig);

        if (shootConfig.getApprovalRequest() != null) {
            dto.setApprovalStatus(
                new DictionaryDTO()
                    .withId(Long.valueOf(shootConfig.getApprovalRequest().getStatus().ordinal()))
                    .withName(shootConfig.getApprovalRequest().getStatus().toString())
            );
        }

        return dto;
    }

    @Override
    public List<ShootConfigDTO> shootConfigsToShootConfigsDTOs(List<ShootConfig> shootConfigs) {
        if (shootConfigs == null) {
            return null;
        }

        return shootConfigs.stream()
            .map(s -> shootConfigToShootConfigDTO(s))
            .collect(Collectors.toList());
    }

    @Override
    public ShootConfig shootConfigDTOToShootConfig(ShootConfigDTO shootConfigDTO) {
        return delegate.shootConfigDTOToShootConfig(shootConfigDTO);
    }

    @Override
    public List<ShootConfig> shootConfigsDTOsToShootConfigs(List<ShootConfigDTO> shootConfigDTOs) {
        return delegate.shootConfigsDTOsToShootConfigs(shootConfigDTOs);
    }
}
