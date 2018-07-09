package com.jason.trade.model;

import javax.persistence.*;

@Entity
@Table(name="sys_log")
public class SysLog {
    @Id
    @GeneratedValue
    @Column(name="log_id")
    private Integer logId;
    @Column(name="create_date")
    private String createDate;
    @Column(name="detail")
    private String detail;
    @Column(name="operation")
    private String operation;
    @Column(name="remark")
    private String remark;
    @Column(name="user")
    private String user;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation == null ? null : operation.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user == null ? null : user.trim();
    }
}