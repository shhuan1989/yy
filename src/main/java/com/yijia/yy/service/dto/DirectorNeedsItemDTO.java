package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the DirectorNeedsItem entity.
 */
public class DirectorNeedsItemDTO implements Serializable {

    private Long id;

    private String name;

    private Integer amount;

    private String memo;

    private Long directorNeedsId;

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
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getDirectorNeedsId() {
        return directorNeedsId;
    }

    public void setDirectorNeedsId(Long directorNeedsId) {
        this.directorNeedsId = directorNeedsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DirectorNeedsItemDTO directorNeedsItemDTO = (DirectorNeedsItemDTO) o;

        if ( ! Objects.equals(id, directorNeedsItemDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DirectorNeedsItemDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", amount='" + amount + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
