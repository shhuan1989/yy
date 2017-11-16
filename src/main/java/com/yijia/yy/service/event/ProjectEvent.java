package com.yijia.yy.service.event;

import com.yijia.yy.domain.enumeration.TaskStatus;

/**
 * Project related event
 */

public class ProjectEvent {
    public static enum Category{
        TASK,
        PROJECT,
        COMMENT,
        RATE
    }

    private TaskStatus taskStatus;

    private Long projectId;

    private Long commentId;

    private Long taskId;

    private Long rateId;

    private Category category;

    public static ProjectEvent taskEvent() {
        return new ProjectEvent().category(Category.TASK);
    }

    public static ProjectEvent projectEvent() {
        return new ProjectEvent().category(Category.PROJECT);
    }

    public static ProjectEvent commentEvent() {
        return new ProjectEvent().category(Category.COMMENT);
    }

    public static ProjectEvent rateEvent() {
        return new ProjectEvent().category(Category.RATE);
    }

    public ProjectEvent category(Category category) {
        this.category = category;
        return this;
    }

    public ProjectEvent projectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public ProjectEvent commentId(Long commentId) {
        this.commentId = commentId;
        return this;
    }

    public ProjectEvent taskId(Long taskId) {
        this.taskId = taskId;
        return this;
    }


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }
}
