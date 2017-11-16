package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AssetSearchDTO {
    private String name;

    private Long ownerId;

    private Boolean onlyPublicAsset;

    private String memo;

    private Boolean needReturn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Boolean getNeedReturn() {
        return needReturn;
    }

    public void setNeedReturn(Boolean needReturn) {
        this.needReturn = needReturn;
    }

    public Boolean getOnlyPublicAsset() {
        return onlyPublicAsset;
    }

    public void setOnlyPublicAsset(Boolean onlyPublicAsset) {
        this.onlyPublicAsset = onlyPublicAsset;
    }
}
