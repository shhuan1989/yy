package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A DTO for the Dept entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DeptDTO implements Serializable {

    private Long id;

    private String name;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeptDTO deptDTO = (DeptDTO) o;

        if ( ! Objects.equals(id, deptDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DeptDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
