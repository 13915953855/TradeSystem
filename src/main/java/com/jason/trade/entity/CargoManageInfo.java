package com.jason.trade.entity;

import com.jason.trade.model.CargoInfo;

public class CargoManageInfo extends CargoInfo {
    private String contractNo;
    private String insideContract;

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getInsideContract() {
        return insideContract;
    }

    public void setInsideContract(String insideContract) {
        this.insideContract = insideContract;
    }
}
