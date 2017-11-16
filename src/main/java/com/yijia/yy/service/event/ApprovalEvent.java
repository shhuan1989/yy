package com.yijia.yy.service.event;

public class ApprovalEvent {
    private String correlationId;

    public ApprovalEvent correlationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
