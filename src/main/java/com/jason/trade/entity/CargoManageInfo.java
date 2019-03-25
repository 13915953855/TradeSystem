package com.jason.trade.entity;

import com.jason.trade.model.CargoInfo;

public class CargoManageInfo extends CargoInfo {
    private String contractNo;
    private String insideContract;
    private String warehouse;
    private String storeDate;
    private String containerNo;
    private String yysWeight;
    private String ladingbillNo;
    private String status;
    private String expectSailingDate;

    public String getExpectSailingDate() {
        return expectSailingDate;
    }

    public void setExpectSailingDate(String expectSailingDate) {
        this.expectSailingDate = expectSailingDate;
    }

    public String getYysWeight() {
        return yysWeight;
    }

    public void setYysWeight(String yysWeight) {
        this.yysWeight = yysWeight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getLadingbillNo() {
        return ladingbillNo;
    }

    public void setLadingbillNo(String ladingbillNo) {
        this.ladingbillNo = ladingbillNo;
    }

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
