package com.jason.trade.entity;

import com.jason.trade.model.CargoInfo;

public class CargoManageInfo extends CargoInfo {
    private String contractNo;
    private String insideContract;
    private String warehouse;
    private String storeDate;

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getStoreDate() {
        return storeDate;
    }

    public void setStoreDate(String storeDate) {
        this.storeDate = storeDate;
    }

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
