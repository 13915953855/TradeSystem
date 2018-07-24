package com.jason.trade.mapper;

import com.jason.trade.entity.CargoManageInfo;
import com.jason.trade.entity.CargoParam;
import com.jason.trade.model.CargoInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CargoInfoMapper {
    int updateByPrimaryKeySelective(CargoInfo record);
    List<CargoManageInfo> selectByExample(CargoParam cargoParam);
    Integer getTotalStoreWeightByExample(CargoParam cargoParam);
    Integer getTotalStoreBoxesByExample(CargoParam cargoParam);
    Integer selectCountByExample(CargoParam cargoParam);
}