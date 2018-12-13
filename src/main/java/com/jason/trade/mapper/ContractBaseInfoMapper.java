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
    List<QueryContractInfo> queryCargoListByExample(ContractParam contractParam);
    Integer queryCargoTotalByExample(ContractParam contractParam);
    void updateStatusByField(ContractParam contractParam);
    List<ContractTotalInfo> getTotalInfo(ContractParam contractParam);
    List<ContractTotalInfo> getTotalInfoForQueryContract(ContractParam contractParam);
    List<ContractForCharts> getTotalNumPerDay();
    List<QueryContractInfo> queryStoreInfoListByExample(ContractParam contractParam);
    Integer countStoreInfoListByExample(ContractParam contractParam);
    CargoTotalInfo getTotalStoreInfoForQuery(ContractParam contractParam);
    List<QueryContractInfo> queryCargoList(ContractParam contractParam);
    Integer countCargoList(ContractParam contractParam);
}