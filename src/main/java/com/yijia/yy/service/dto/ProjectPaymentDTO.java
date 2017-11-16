package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ProjectPayment entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectPaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private Long appointedTime;

    @NotNull
    @DecimalMin(value = "0")
    private Float amount;

    private Long payTime;


    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getAppointedTime() {
        return appointedTime;
    }

    public void setAppointedTime(Long appointedTime) {
        this.appointedTime = appointedTime;
    }
    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectPaymentDTO projectPaymentDTO = (ProjectPaymentDTO) o;

        if ( ! Objects.equals(id, projectPaymentDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectPaymentDTO{" +
            "id=" + id +
            ", appointedTime='" + appointedTime + "'" +
            ", amount='" + amount + "'" +
            ", payTime='" + payTime + "'" +
            '}';
    }
}
