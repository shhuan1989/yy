package com.yijia.yy.domain.enumeration;


public enum  ProjectRateStatus {
    NOT_START ("未开始"),
    IN_PROGRESS ("进行中"),
    MEMBER_DONE("成员已评级"),
    LEAD_DONE("主管已经评级");

    private final String desc;

    private ProjectRateStatus(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }
}
