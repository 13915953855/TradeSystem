package com.jason.trade.entity;

import java.io.Serializable;

public class CargoStoreInfo implements Serializable {
    private String cargo_name;
    private String level;
    private String company_no;
    private String cargo_no;
    private String external_contract;
    private String inside_contract;
    private String container_no;
    private String ladingbill_no;
    private String store_date;
    private String warehouse;
    private String invoice_amount;
    private String boxes;
    private String baoguandan;
    private String qacertificate;

    public String getCargo_name() {
        return cargo_name;
    }

    public void setCargo_name(String cargo_name) {
        this.cargo_name = cargo_name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCompany_no() {
        return company_no;
    }

    public void setCompany_no(String company_no) {
        this.company_no = company_no;
    }

    public String getCargo_no() {
        return cargo_no;
    }

    public void setCargo_no(String cargo_no) {
        this.cargo_no = cargo_no;
    }

    public String getExternal_contract() {
        return external_contract;
    }

    public void setExternal_contract(String external_contract) {
        this.external_contract = external_contract;
    }

    public String getInside_contract() {
        return inside_contract;
    }

    public void setInside_contract(String inside_contract) {
        this.inside_contract = inside_contract;
    }

    public String getContainer_no() {
        return container_no;
    }

    public void setContainer_no(String container_no) {
        this.container_no = container_no;
    }

    public String getLadingbill_no() {
        return ladingbill_no;
    }

    public void setLadingbill_no(String ladingbill_no) {
        this.ladingbill_no = ladingbill_no;
    }

    public String getStore_date() {
        return store_date;
    }

    public void setStore_date(String store_date) {
        this.store_date = store_date;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(String invoice_amount) {
        this.invoice_amount = invoice_amount;
    }

    public String getBoxes() {
        return boxes;
    }

    public void setBoxes(String boxes) {
        this.boxes = boxes;
    }

    public String getBaoguandan() {
        return baoguandan;
    }

    public void setBaoguandan(String baoguandan) {
        this.baoguandan = baoguandan;
    }

    public String getQacertificate() {
        return qacertificate;
    }

    public void setQacertificate(String qacertificate) {
        this.qacertificate = qacertificate;
    }
}
