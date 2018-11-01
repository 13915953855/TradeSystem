package com.jason.trade.entity;

public class ContractTotalInfo {
    private Float totalContractMoney;
    private Float totalContractAmount;
    private Float totalInvoiceMoney;
    private Float totalInvoiceAmount;
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Float getTotalContractMoney() {
        return totalContractMoney;
    }

    public void setTotalContractMoney(Float totalContractMoney) {
        this.totalContractMoney = totalContractMoney;
    }

    public Float getTotalContractAmount() {
        return totalContractAmount;
    }

    public void setTotalContractAmount(Float totalContractAmount) {
        this.totalContractAmount = totalContractAmount;
    }

    public Float getTotalInvoiceMoney() {
        return totalInvoiceMoney;
    }

    public void setTotalInvoiceMoney(Float totalInvoiceMoney) {
        this.totalInvoiceMoney = totalInvoiceMoney;
    }

    public Float getTotalInvoiceAmount() {
        return totalInvoiceAmount;
    }

    public void setTotalInvoiceAmount(Float totalInvoiceAmount) {
        this.totalInvoiceAmount = totalInvoiceAmount;
    }
}
