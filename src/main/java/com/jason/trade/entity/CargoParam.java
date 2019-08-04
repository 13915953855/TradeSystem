package com.jason.trade.entity;

import lombok.Data;

/**
 * 查询Bean
 */
@Data
public class CargoParam {
    private String contractId;
    private String contractNo;
    private String expectSaleDateStart;
    private String expectSaleDateEnd;
    private String importContractNo;
    private String externalCompany;
    private String insideContract;
    private String level;
    private String cargoName;
    private String cmpRel;
    private String cargoNo;
    private String cargoType;
    private String warehouse;
    private String storeStartDate;
    private String storeEndDate;
    private String contractStartDate;
    private String contractEndDate;
    private String pickupDateStart;
    private String pickupDateEnd;
    private String realSaleDateStart;
    private String realSaleDateEnd;
    private String etaStartDate;
    private String etaEndDate;
    private String etdStartDate;
    private String etdEndDate;
    private String customerName;
    private String containerNo;
    private String ladingbillNo;
    private String status;
    private String businessMode;
    private String minBox;
    private String maxBox;
    private String companyNo;
    private String minWeight;
    private String maxWeight;
    private String baoguandan;
    private String qacertificate;
    private String ownerCompany;
    private String kaifapiao;
    private String storageCondition;
    private String originCountry;
    private String destinationPort;
    private int start;
    private int limit;
    private int expectStoreBoxes;

    
}
