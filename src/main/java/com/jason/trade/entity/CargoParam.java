package com.jason.trade.entity;

/**
 * 查询Bean
 */
public class CargoParam {
    private String contractNo;
    private String insideContract;
    private String level;
    private String cargoName;
    private String cargoNo;
    private String warehouse;
    private String storeStartDate;
    private String storeEndDate;
    private String customerName;
    private String containerNo;
    private String ladingbillNo;
    private String status;
    private int start;
    private int limit;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getStoreStartDate() {
        return storeStartDate;
    }

    public void setStoreStartDate(String storeStartDate) {
        this.storeStartDate = storeStartDate;
    }

    public String getStoreEndDate() {
        return storeEndDate;
    }

    public void setStoreEndDate(String storeEndDate) {
        this.storeEndDate = storeEndDate;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getInsideContract() {
        return insideContract;
    }

    public void setInsideContract(String insideContract) {
        this.insideContract = insideContract;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getCargoNo() {
        return cargoNo;
    }

    public void setCargoNo(String cargoNo) {
        this.cargoNo = cargoNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
}
