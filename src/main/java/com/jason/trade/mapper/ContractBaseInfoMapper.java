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
    void updateStatusByField(ContractParam contractParam);
    ContractTotalInfo getTotalInfo(ContractParam contractParam);
    List<ContractForCharts> getTotalNumPerDay();
}