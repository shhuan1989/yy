package com.yijia.yy.domain.enumeration;

public enum ApprovalStatus {
    NOT_START ("未开始"),
    IN_PROGRESS ("进行中"),
    APPROVED ("已通过"),
    REJECTED ("已驳回"),
    CANCELLED ("已取消");

    private final String desc;

    private ApprovalStatus(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }
}
