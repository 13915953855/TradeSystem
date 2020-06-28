package com.jason.trade.entity;

import com.jason.trade.model.CargoInfo;
import lombok.Data;

@Data
public class CargoManageInfo extends CargoInfo {
    private String contractNo;
    private String externalCompany;
    private String externalContract;
    private String insideContract;
    private String importContractNo;
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
}
