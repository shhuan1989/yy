package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ContractInvoice entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContractInvoiceDTO implements Serializable {

    private Long id;

    @NotNull
    private Double amount;

    @NotNull
    private Long createTime;


    private Long contractId;

    private Long creatorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long employeeId) {
        this.creatorId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContractInvoiceDTO contractInvoiceDTO = (ContractInvoiceDTO) o;

        if ( ! Objects.equals(id, contractInvoiceDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ContractInvoiceDTO{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            ", createTime='" + createTime + "'" +
            '}';
    }
}
