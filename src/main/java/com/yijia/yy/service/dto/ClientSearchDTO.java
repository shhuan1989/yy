package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Object for searching client
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClientSearchDTO {
    private String name;
    private Long industryId;
    private String contactName;
    private String contactTel;
    private Long clientSourceId;
    private String clientOwner;
    private Boolean showAll;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Long industryId) {
        this.industryId = industryId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public Long getClientSourceId() {
        return clientSourceId;
    }

    public void setClientSourceId(Long clientSourceId) {
        this.clientSourceId = clientSourceId;
    }

    public String getClientOwner() {
        return clientOwner;
    }

    public void setClientOwner(String clientOwner) {
        this.clientOwner = clientOwner;
    }

    public Boolean getShowAll() {
        return showAll;
    }

    public void setShowAll(Boolean showAll) {
        this.showAll = showAll;
    }
}
