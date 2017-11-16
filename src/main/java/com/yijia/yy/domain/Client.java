package com.yijia.yy.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "contact")
    private String contact;

    @Column(name = "contact_tel")
    private String contactTel;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "tel_corp")
    private String telCorp;

    @Column(name = "website_corp")
    private String websiteCorp;

    @Column(name = "address_corp")
    private String addressCorp;

    @Column(name = "qq")
    private String qq;

    @Column(name = "wechat")
    private String wechat;

    @Column(name = "email")
    private String email;

    @Column(name = "owner")
    private String owner;

    @Column(name = "contact_job_title")
    private String contactJobTitle;

    @Column(name = "contact_birth_date")
    private String contactBirthDate;

    @Column(name = "contact_hobby")
    private String contactHobby;

    @Column(name = "comment", length = 4000)
    private String comment;

    @Column(name = "last_modified_time")
    private Long lastModifiedTime;

    @Column(name = "input_operator")
    private String inputOperator;

    @Column(name = "last_modifier")
    private String lastModifier;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Dictionary industry;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Dictionary source;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Dictionary status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Client name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public Client contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactTel() {
        return contactTel;
    }

    public Client contactTel(String contactTel) {
        this.contactTel = contactTel;
        return this;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Client createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getTelCorp() {
        return telCorp;
    }

    public Client telCorp(String telCorp) {
        this.telCorp = telCorp;
        return this;
    }

    public void setTelCorp(String telCorp) {
        this.telCorp = telCorp;
    }

    public String getWebsiteCorp() {
        return websiteCorp;
    }

    public Client websiteCorp(String websiteCorp) {
        this.websiteCorp = websiteCorp;
        return this;
    }

    public void setWebsiteCorp(String websiteCorp) {
        this.websiteCorp = websiteCorp;
    }

    public String getAddressCorp() {
        return addressCorp;
    }

    public Client addressCorp(String addressCorp) {
        this.addressCorp = addressCorp;
        return this;
    }

    public void setAddressCorp(String addressCorp) {
        this.addressCorp = addressCorp;
    }

    public String getQq() {
        return qq;
    }

    public Client qq(String qq) {
        this.qq = qq;
        return this;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public Client wechat(String wechat) {
        this.wechat = wechat;
        return this;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getEmail() {
        return email;
    }

    public Client email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwner() {
        return owner;
    }

    public Client owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getContactJobTitle() {
        return contactJobTitle;
    }

    public Client contactJobTitle(String contactJobTitle) {
        this.contactJobTitle = contactJobTitle;
        return this;
    }

    public void setContactJobTitle(String contactJobTitle) {
        this.contactJobTitle = contactJobTitle;
    }

    public String getContactBirthDate() {
        return contactBirthDate;
    }

    public Client contactBirthDate(String contactBirthDate) {
        this.contactBirthDate = contactBirthDate;
        return this;
    }

    public void setContactBirthDate(String contactBirthDate) {
        this.contactBirthDate = contactBirthDate;
    }

    public String getContactHobby() {
        return contactHobby;
    }

    public Client contactHobby(String contactHobby) {
        this.contactHobby = contactHobby;
        return this;
    }

    public void setContactHobby(String contactHobby) {
        this.contactHobby = contactHobby;
    }

    public String getComment() {
        return comment;
    }

    public Client comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public Client lastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getInputOperator() {
        return inputOperator;
    }

    public Client inputOperator(String inputOperator) {
        this.inputOperator = inputOperator;
        return this;
    }

    public void setInputOperator(String inputOperator) {
        this.inputOperator = inputOperator;
    }

    public String getLastModifier() {
        return lastModifier;
    }

    public Client lastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
        return this;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public Dictionary getIndustry() {
        return industry;
    }

    public Client industry(Dictionary dictionary) {
        this.industry = dictionary;
        return this;
    }

    public void setIndustry(Dictionary dictionary) {
        this.industry = dictionary;
    }

    public Dictionary getSource() {
        return source;
    }

    public Client source(Dictionary dictionary) {
        this.source = dictionary;
        return this;
    }

    public void setSource(Dictionary dictionary) {
        this.source = dictionary;
    }

    public Dictionary getStatus() {
        return status;
    }

    public void setStatus(Dictionary status) {
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
        Client client = (Client) o;
        if(client.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Client{" +
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
