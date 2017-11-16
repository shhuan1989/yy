package com.yijia.yy.service.event;


import com.yijia.yy.domain.Asset;
import com.yijia.yy.domain.AssetBorrowRecord;
import com.yijia.yy.domain.ShootConfig;
import com.yijia.yy.domain.enumeration.ApprovalStatus;
import com.yijia.yy.repository.AssetBorrowRecordRepository;
import com.yijia.yy.repository.AssetRepository;
import com.yijia.yy.repository.ShootConfigRepository;
import com.yijia.yy.service.ShootConfigService;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.dto.ShootConfigDTO;
import com.yijia.yy.service.mapper.ShootConfigMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.bus.Event;
import reactor.fn.Consumer;

import javax.inject.Inject;
import java.util.UUID;

@Service
@Qualifier("approval")
@Transactional
public class ApprovalEventReceiver implements Consumer<Event<ApprovalEvent>> {

    @Inject
    private ShootConfigRepository shootConfigRepository;

    @Inject
    private ShootConfigService shootConfigService;

    @Inject
    private ShootConfigMapper shootConfigMapper;

    @Inject
    private AssetBorrowRecordRepository borrowRecordRepository;

    @Inject
    private AssetRepository assetRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void accept(Event<ApprovalEvent> approvalEventEvent) {

        // if it's a shoot config
        ShootConfig shootConfig = shootConfigRepository.findOneByCorrelationId(approvalEventEvent.getData().getCorrelationId());
        if (shootConfig != null
            && (shootConfig.getType() == null || shootConfig.getType().equals(0) || shootConfig.getType().equals(1))
            && shootConfig.getApprovalRequest() != null
            && shootConfig.getApprovalRequest().getStatus() != null
            && shootConfig.getApprovalRequest().getStatus().equals(ApprovalStatus.APPROVED)) {
            Long ct = System.currentTimeMillis();
            String uuid = UUID.randomUUID().toString();

            ShootConfigDTO budgetConfig = shootConfigMapper.shootConfigToShootConfigDTO(shootConfig);
            if (shootConfig.getType().equals(1)) {
                budgetConfig.setType(2);
            } else {
                budgetConfig.setType(1);
            }
            budgetConfig.setApprovalRequest(null);
            budgetConfig.setStartTime(ct);
            budgetConfig.setAutoStart(false);
            budgetConfig.setCreateTime(ct);
            budgetConfig.setId(null);
            budgetConfig.setCorrelationId(uuid);
            budgetConfig.getConfigItems().forEach(c -> c.setId(null));
            budgetConfig.setApprovalStatus(new DictionaryDTO().withId(Long.valueOf(ApprovalStatus.NOT_START.ordinal())));

            shootConfigService.save(budgetConfig);
        }

        // if it's a goods application
        AssetBorrowRecord borrowRecord = borrowRecordRepository.findOneByCorrelationId(approvalEventEvent.getData().getCorrelationId());
        if (borrowRecord != null) {
            Asset asset = borrowRecord.getAsset();
            asset.setAmount(asset.getAmount() - borrowRecord.getAmount());
            assetRepository.saveAndFlush(asset);
        }

    }
}
