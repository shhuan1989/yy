package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A DTO for the Client entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClientDTO implements Serializable {

    private Long id;

    private String name;

    private String contact;

    private String contactTel;

    private Long createTime;

    private String telCorp;

    private String websiteCorp;

    private String addressCorp;

    private String qq;

    private String wechat;

    private String email;

    private String owner;

    private String contactJobTitle;

    private String contactBirthDate;

    private String contactHobby;

    private String comment;

    private Long lastModifiedTime;

    private String inputOperator;

    private String lastModifier;

    private DictionaryDTO status;

    private DictionaryDTO industry;

    private DictionaryDTO source;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public String getTelCorp() {
        return telCorp;
    }

    public void setTelCorp(String telCorp) {
        this.telCorp = telCorp;
    }
    public String getWebsiteCorp() {
        return websiteCorp;
    }

    public void setWebsiteCorp(String websiteCorp) {
        this.websiteCorp = websiteCorp;
    }
    public String getAddressCorp() {
        return addressCorp;
    }

    public void setAddressCorp(String addressCorp) {
        this.addressCorp = addressCorp;
    }
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getContactJobTitle() {
        return contactJobTitle;
    }

    public void setContactJobTitle(String contactJobTitle) {
        this.contactJobTitle = contactJobTitle;
    }
    public String getContactBirthDate() {
        return contactBirthDate;
    }

    public void setContactBirthDate(String contactBirthDate) {
        this.contactBirthDate = contactBirthDate;
    }
    public String getContactHobby() {
        return contactHobby;
    }

    public void setContactHobby(String contactHobby) {
        this.contactHobby = contactHobby;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
    public String getInputOperator() {
        return inputOperator;
    }

    public void setInputOperator(String inputOperator) {
        this.inputOperator = inputOperator;
    }
    public String getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public DictionaryDTO getIndustry() {
        return industry;
    }

    public void setIndustry(DictionaryDTO industry) {
        this.industry = industry;
    }

    public DictionaryDTO getSource() {
        return source;
    }

    public void setSource(DictionaryDTO source) {
        this.source = source;
    }

    public DictionaryDTO getStatus() {
        return status;
    }

    public void setStatus(DictionaryDTO status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClientDTO clientDTO = (ClientDTO) o;

        if ( ! Objects.equals(id, clientDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", contact='" + contact + "'" +
            ", contactTel='" + contactTel + "'" +
            ", createTime='" + createTime + "'" +
            ", telCorp='" + telCorp + "'" +
            ", websiteCorp='" + websiteCorp + "'" +
            ", addressCorp='" + addressCorp + "'" +
            ", qq='" + qq + "'" +
            ", wechat='" + wechat + "'" +
            ", email='" + email + "'" +
            ", owner='" + owner + "'" +
            ", contactJobTitle='" + contactJobTitle + "'" +
            ", contactBirthDate='" + contactBirthDate + "'" +
            ", contactHobby='" + contactHobby + "'" +
            ", comment='" + comment + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            ", inputOperator='" + inputOperator + "'" +
            ", lastModifier='" + lastModifier + "'" +
            '}';
    }
}
