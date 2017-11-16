package com.yijia.yy.domain.enumeration;

public enum ProjectStatus {
    INCOMPLETE ("进行中"),
    PAUSED("已暂停"),
    ARCHIVED("已归档"),
    CLOSED("已关闭");

    private final String desc;

    private ProjectStatus(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }
}
