package com.jason.trade.mapper;

import com.jason.trade.entity.CargoManageInfo;
import com.jason.trade.entity.CargoParam;
import com.jason.trade.model.CargoInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CargoInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CargoInfo record);

    int insertSelective(CargoInfo record);

    CargoInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CargoInfo record);

    int updateByPrimaryKey(CargoInfo record);

    List<CargoManageInfo> selectByExample(CargoParam cargoParam);
    Integer selectCountByExample(CargoParam cargoParam);
}