package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Room entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoomDTO implements Serializable {

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

        RoomDTO roomDTO = (RoomDTO) o;

        if ( ! Objects.equals(id, roomDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RoomDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
