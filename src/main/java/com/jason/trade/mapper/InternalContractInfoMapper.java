package com.jason.trade.mapper;

import com.jason.trade.entity.ContractParam;
import com.jason.trade.entity.InternalContractParam;
import com.jason.trade.model.ContractBaseInfo;
import com.jason.trade.model.InternalContractInfo;

import java.util.List;

public interface InternalContractInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InternalContractInfo record);

    int insertSelective(InternalContractInfo record);

    InternalContractInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InternalContractInfo record);

    int updateByPrimaryKey(InternalContractInfo record);

    List<InternalContractInfo> selectByExample(InternalContractParam contractParam);
    Integer selectCountByExample(InternalContractParam contractParam);
}