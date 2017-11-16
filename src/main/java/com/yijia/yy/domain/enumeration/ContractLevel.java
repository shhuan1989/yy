package com.yijia.yy.domain.enumeration;

/**
 * the level of contract
 */
public enum  ContractLevel {

    LEVEL_1 ("普通"),
    LEVEL_2 ("重要"),
    LEVEL_3 ("非常重要");

    private final String desc;

    private ContractLevel(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }
}
