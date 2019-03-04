package com.jason.trade.entity;

/**
 * 查询Bean
 */
public class InternalContractParam {
    private String contractNo;
    private String importContractNo;
    private String contractStartDate;
    private String contractEndDate;
    private String status;
    private String fieldName;
    private String today;
    private String cargoName;
    private String warehouse;
    private String storeStartDate;
    private String storeEndDate;
    private Integer start;
    private Integer limit;

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
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

    public String getImportContractNo() {
        return importContractNo;
    }

    public void setImportContractNo(String importContractNo) {
        this.importContractNo = importContractNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(String contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public String getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
