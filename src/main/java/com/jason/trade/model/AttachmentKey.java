package com.jason.trade.model;

import java.io.Serializable;

public class AttachmentKey implements Serializable {
    private Integer id;

    private String contractId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId == null ? null : contractId.trim();
    }
}