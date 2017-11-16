package com.yijia.yy.domain.enumeration;


public enum AssetReturnStatus {

    NO ("未归还"),
    RETURNED ("已归还"),
    NO_NEED ("无需归还");

    private final String desc;

    private AssetReturnStatus(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }
}
