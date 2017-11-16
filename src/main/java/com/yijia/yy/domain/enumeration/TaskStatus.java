package com.yijia.yy.domain.enumeration;


public enum  TaskStatus {
    UNKNOWN (-2),
    NEW (-1),
    INPROGRESS (0),
    DONE (1);

    private final String desc;
    private final int value;

    private TaskStatus(int value) {
        this.value = value;
        switch (this.value) {
            case 0:
                this.desc = "进行中";
                break;
            case 1:
                this.desc = "已完成";
                break;
            case -1:
                this.desc = "新建";
                break;
            default:
                this.desc = "未知";
        }
    }

    public int value() {
        return this.value;
    }
    public String toString() {
        return this.desc;
    }
}
