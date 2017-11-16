package com.yijia.yy.domain.enumeration;


public enum ProjectBonusStatus {
    NOT_STARTED ("未开始"),
    IN_PROGRESS ("进行中"),
    DONE("已完成");

    private final String desc;

    private ProjectBonusStatus(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }
}
