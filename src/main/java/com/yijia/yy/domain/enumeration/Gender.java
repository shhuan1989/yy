package com.yijia.yy.domain.enumeration;

/**
 * The Gender enumeration.
 */
public enum Gender {
    UNKNOWN ("未知"),
    MALE ("男"),
    FEMALE ("女");
    private final String desc;

    private Gender(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }

    public String  getDesc() {
        return desc;
    }
}
