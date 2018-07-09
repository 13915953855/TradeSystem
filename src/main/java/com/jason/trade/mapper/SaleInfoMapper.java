package com.jason.trade.mapper;

import com.jason.trade.model.SaleInfo;
import org.springframework.stereotype.Component;

@Component
public interface SaleInfoMapper {
    int deleteByPrimaryKey(Integer saleId);

    int insert(SaleInfo record);

    int insertSelective(SaleInfo record);

    SaleInfo selectByPrimaryKey(Integer saleId);

    int updateByPrimaryKeySelective(SaleInfo record);

    int updateByPrimaryKey(SaleInfo record);
}