package com.jason.trade.mapper;

import com.jason.trade.model.InternalCargoInfo;
import com.jason.trade.model.InternalCargoInfoKey;

public interface InternalCargoInfoMapper {
    int deleteByPrimaryKey(InternalCargoInfoKey key);

    int insert(InternalCargoInfo record);

    int insertSelective(InternalCargoInfo record);

    InternalCargoInfo selectByPrimaryKey(InternalCargoInfoKey key);

    int updateByPrimaryKeySelective(InternalCargoInfo record);

    int updateByPrimaryKey(InternalCargoInfo record);
}