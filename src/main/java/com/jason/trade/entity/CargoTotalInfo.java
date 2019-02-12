package com.jason.trade.entity;

public class CargoTotalInfo {
    private Float totalInvoiceMoney;
    private Float totalInvoiceWeight;
    private Float totalInvoiceBoxes;
    private Float expectStoreWeight;
    private Float realStoreWeight;
    private Float expectStoreBoxes;
    private Float realStoreBoxes;
    private Float realStoreMoney;
    private int totalContract;
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getTotalContract() {
        return totalContract;
    }

    public void setTotalContract(int totalContract) {
        this.totalContract = totalContract;
    }

    public Float getTotalInvoiceMoney() {
        return totalInvoiceMoney;
    }

    public void setTotalInvoiceMoney(Float totalInvoiceMoney) {
        this.totalInvoiceMoney = totalInvoiceMoney;
    }

    public Float getTotalInvoiceWeight() {
        return totalInvoiceWeight;
    }

    public void setTotalInvoiceWeight(Float totalInvoiceWeight) {
        this.totalInvoiceWeight = totalInvoiceWeight;
    }

    public Float getTotalInvoiceBoxes() {
        return totalInvoiceBoxes;
    }

    public void setTotalInvoiceBoxes(Float totalInvoiceBoxes) {
        this.totalInvoiceBoxes = totalInvoiceBoxes;
    }

    public Float getExpectStoreWeight() {
        return expectStoreWeight;
    }

    public void setExpectStoreWeight(Float expectStoreWeight) {
        this.expectStoreWeight = expectStoreWeight;
    }

    public Float getRealStoreWeight() {
        return realStoreWeight;
    }

    public void setRealStoreWeight(Float realStoreWeight) {
        this.realStoreWeight = realStoreWeight;
    }

    public Float getExpectStoreBoxes() {
        return expectStoreBoxes;
    }

    public void setExpectStoreBoxes(Float expectStoreBoxes) {
        this.expectStoreBoxes = expectStoreBoxes;
    }

    public Float getRealStoreBoxes() {
        return realStoreBoxes;
    }

    public void setRealStoreBoxes(Float realStoreBoxes) {
        this.realStoreBoxes = realStoreBoxes;
    }

    public Float getRealStoreMoney() {
        return realStoreMoney;
    }

    public void setRealStoreMoney(Float realStoreMoney) {
        this.realStoreMoney = realStoreMoney;
    }
}
