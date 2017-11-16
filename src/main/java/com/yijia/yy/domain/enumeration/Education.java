package com.yijia.yy.domain.enumeration;

/**
 * The Education enumeration.
 */
public enum Education {
    UNKNOWN ("未知"),
    UNDER_HIGH_SCHOOL ("高中及以下"),
    JUNIOR_COLLEGE ("大专"),
    BACHELER ("本科"),
    MASTER ("硕士"),
    PHD ("博士及以上");

    private final String desc;

    private Education(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }
}
