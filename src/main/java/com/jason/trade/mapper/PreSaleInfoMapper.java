package com.jason.trade.mapper;

import com.jason.trade.model.PreSaleInfo;

public interface PreSaleInfoMapper {
    int deleteByPrimaryKey(Integer saleId);

    int insert(PreSaleInfo record);

    int insertSelective(PreSaleInfo record);

    PreSaleInfo selectByPrimaryKey(Integer saleId);

    int updateByPrimaryKeySelective(PreSaleInfo record);

    int updateByPrimaryKey(PreSaleInfo record);

    Float getPreSaleTotal(String cargoId);
}