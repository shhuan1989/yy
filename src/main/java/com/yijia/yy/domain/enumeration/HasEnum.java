package com.yijia.yy.domain.enumeration;


public enum  HasEnum {
    NO ("无"),
    YES ("有"),
    UNKNOWN ("未知");

    private final String desc;

    private HasEnum(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }
}
