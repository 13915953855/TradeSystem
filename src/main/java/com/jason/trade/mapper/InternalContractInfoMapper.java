package com.jason.trade.mapper;

import com.jason.trade.entity.ContractParam;
import com.jason.trade.entity.InternalContractParam;
import com.jason.trade.model.ContractBaseInfo;
import com.jason.trade.model.InternalContractInfo;

import java.util.List;

public interface InternalContractInfoMapper {
    InternalContractInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InternalContractInfo record);

    List<InternalContractInfo> selectByExample(InternalContractParam contractParam);
    Integer selectCountByExample(InternalContractParam contractParam);
}