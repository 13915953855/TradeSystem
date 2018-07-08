package com.jason.trade.entity;

public class CargoManageInfo extends CargoInfo{
    private String contractNo;
    private String insideContrade;

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getInsideContrade() {
        return insideContrade;
    }

    public void setInsideContrade(String insideContrade) {
        this.insideContrade = insideContrade;
    }
}
