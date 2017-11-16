package com.yijia.yy.domain.enumeration;

/**
 * BooleanEnum enumeration
 */
public enum ProjectRateEnum {
    UNKNOWN("_"),
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E");
    
    private final String desc;

    private ProjectRateEnum(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }
}
