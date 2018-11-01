package com.jason.trade.mapper;

import com.jason.trade.entity.*;
import com.jason.trade.model.ContractBaseInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ContractBaseInfoMapper {
    int updateByPrimaryKeySelective(ContractBaseInfo record);
    List<ContractBaseInfo> selectByExample(ContractParam contractParam);
    Integer selectCountByExample(ContractParam contractParam);
    List<ContractBaseInfo> queryContractListByExample(ContractParam contractParam);
    Integer queryContractTotalByExample(ContractParam contractParam);
    void updateStatusByField(ContractParam contractParam);
    List<ContractTotalInfo> getTotalInfo(ContractParam contractParam);
    ContractTotalInfo getTotalInfoForQueryContract(ContractParam contractParam);
    List<ContractForCharts> getTotalNumPerDay();
}