package com.jason.trade.entity;

public class ContractTotalInfo {
    private Float totalContractMoney;
    private Float totalContractAmount;
    private Float totalInvoiceMoney;
    private Float totalInvoiceAmount;
    private Float totalFinancingMoney;
    private Float totalYahuiMoney;
    private Float totalPrePayment;
    private Float totalFinalPayment;
    private String currency;

    public Float getTotalPrePayment() {
        return totalPrePayment;
    }

    public void setTotalPrePayment(Float totalPrePayment) {
        this.totalPrePayment = totalPrePayment;
    }

    public Float getTotalFinalPayment() {
        return totalFinalPayment;
    }

    public void setTotalFinalPayment(Float totalFinalPayment) {
        this.totalFinalPayment = totalFinalPayment;
    }

    public Float getTotalYahuiMoney() {
        return totalYahuiMoney;
    }

    public void setTotalYahuiMoney(Float totalYahuiMoney) {
        this.totalYahuiMoney = totalYahuiMoney;
    }

    public Float getTotalFinancingMoney() {
        return totalFinancingMoney;
    }

    public void setTotalFinancingMoney(Float totalFinancingMoney) {
        this.totalFinancingMoney = totalFinancingMoney;
    }

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
