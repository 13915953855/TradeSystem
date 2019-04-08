package com.jason.trade.entity;

import com.jason.trade.model.CargoInfo;

public class CargoManageInfo extends CargoInfo {
    private String contractNo;
    private String externalCompany;
    private String externalContract;
    private String insideContract;
    private String warehouse;
    private String storeDate;
    private String containerNo;
    private String yysWeight;
    private String ladingbillNo;
    private String status;
    private String expectSailingDate;
    private String eta;
    private String etd;
    private String storageCondition;
    private String ownerCompany;
    private String destinationPort;
    private String originCountry;

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getExternalCompany() {
        return externalCompany;
    }

    public void setExternalCompany(String externalCompany) {
        this.externalCompany = externalCompany;
    }

    public String getExternalContract() {
        return externalContract;
    }

    public void setExternalContract(String externalContract) {
        this.externalContract = externalContract;
    }

    public String getInsideContract() {
        return insideContract;
    }

    public void setInsideContract(String insideContract) {
        this.insideContract = insideContract;
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

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getYysWeight() {
        return yysWeight;
    }

    public void setYysWeight(String yysWeight) {
        this.yysWeight = yysWeight;
    }

    public String getLadingbillNo() {
        return ladingbillNo;
    }

    public void setLadingbillNo(String ladingbillNo) {
        this.ladingbillNo = ladingbillNo;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpectSailingDate() {
        return expectSailingDate;
    }

    public void setExpectSailingDate(String expectSailingDate) {
        this.expectSailingDate = expectSailingDate;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getStorageCondition() {
        return storageCondition;
    }

    public void setStorageCondition(String storageCondition) {
        this.storageCondition = storageCondition;
    }

    public String getOwnerCompany() {
        return ownerCompany;
    }

    public void setOwnerCompany(String ownerCompany) {
        this.ownerCompany = ownerCompany;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }
}
