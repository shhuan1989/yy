package com.yijia.yy.domain.enumeration;

/**
 * BooleanEnum enumeration
 */
public enum BooleanEnum {
    NO ("否"),
    YES ("是"),
    UNKNOWN ("未知");

    private final String desc;

    private BooleanEnum(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }
}
