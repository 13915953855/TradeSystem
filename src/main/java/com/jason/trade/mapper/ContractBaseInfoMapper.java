package com.jason.trade.mapper;

import com.jason.trade.model.ContractBaseInfo;
import org.springframework.stereotype.Component;

@Component
public interface ContractBaseInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ContractBaseInfo record);

    int insertSelective(ContractBaseInfo record);

    ContractBaseInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ContractBaseInfo record);

    int updateByPrimaryKey(ContractBaseInfo record);
}