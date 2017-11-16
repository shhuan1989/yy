package com.yijia.yy.domain.enumeration;

/**
 * The JobPositionStatus enumeration.
 */
public enum JobPositionStatus {
    UNKNOWN ("未知"),
    TEMP("临时员工"),
    PROBATION ("试用期"),
    STAFF("正式员工"),
    RESIGNED("离职"),
    DISMISS("辞退"),
    FIRED("开除");

    private final String desc;

    private JobPositionStatus(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }

    public String getDesc() {
        return desc;
    }
}
