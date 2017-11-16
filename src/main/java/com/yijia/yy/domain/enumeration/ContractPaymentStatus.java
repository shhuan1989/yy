package com.yijia.yy.domain.enumeration;

public enum ContractPaymentStatus {
    OVERDUE ("逾期"),
    IN_PROGRESS ("待回款"),
    DONE ("已完结");

    private final String desc;

    private ContractPaymentStatus(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return this.desc;
    }
}
