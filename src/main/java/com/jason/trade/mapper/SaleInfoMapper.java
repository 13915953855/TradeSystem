package com.jason.trade.mapper;

import com.jason.trade.model.SaleInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SaleInfoMapper {
    int deleteByPrimaryKey(Integer saleId);
    int deleteByContract(List<String> id);
    int insert(SaleInfo record);

    int insertSelective(SaleInfo record);

    SaleInfo selectByPrimaryKey(Integer saleId);

    int updateByPrimaryKeySelective(SaleInfo record);

    int updateByPrimaryKey(SaleInfo record);
}