package com.yijia.yy.domain.enumeration;


public enum  MeetingStatus {
    NOT_START ("未开始"),
    IN_PROGRESS("进行中"),
    FINISHED ("已结束"),
    CANCELLED ("已取消");

    private final String desc;

    private MeetingStatus(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }
}
