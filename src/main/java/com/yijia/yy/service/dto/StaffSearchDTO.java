package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO for searching staff
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StaffSearchDTO {
    private String name;
    private Long typeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
}
