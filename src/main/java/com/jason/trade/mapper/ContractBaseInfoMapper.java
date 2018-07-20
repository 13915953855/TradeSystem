package com.jason.trade.mapper;

import com.jason.trade.entity.CargoManageInfo;
import com.jason.trade.entity.CargoParam;
import com.jason.trade.entity.ContractParam;
import com.jason.trade.model.ContractBaseInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ContractBaseInfoMapper {
    int updateByPrimaryKeySelective(ContractBaseInfo record);
    List<ContractBaseInfo> selectByExample(ContractParam contractParam);
    Integer selectCountByExample(ContractParam contractParam);
    void updateStatusByField(ContractParam contractParam);
}