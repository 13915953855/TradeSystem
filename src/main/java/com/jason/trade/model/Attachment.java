package com.jason.trade.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="attachment")
public class Attachment {
    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;
    @Column(name="contract_id")
    private String contractId;
    @Column(name="file_name")
    private String fileName;
    @Column(name="file_type")
    private String fileType;
    @Column(name="file_size")
    private Integer fileSize;
    @Column(name="file_path")
    private String filePath;
    @Column(name="file_ref")
    private String fileRef;
    @Column(name="status")
    private String status;
    @Column(name="create_time")
    private Date createtime;

    public String getFileRef() {
        return fileRef;
    }

    public void setFileRef(String fileRef) {
        this.fileRef = fileRef;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
