package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * dto for searching comments
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentSearchDTO {

    private Long taskId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
