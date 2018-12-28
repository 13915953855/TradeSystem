package com.jason.trade.mapper;

import com.jason.trade.entity.*;
import com.jason.trade.model.CargoInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CargoInfoMapper {
    int updateByPrimaryKeySelective(CargoInfo record);
    int updateByCargoId(CargoInfo record);
    int deleteByContract(List<String> id);
    CargoInfo selectByCargoId(String cargoId);
    List<CargoManageInfo> selectByExample(CargoParam cargoParam);
    Integer getTotalStoreWeightByExample(CargoParam cargoParam);
    Integer getInternalTotalStoreWeightByExample(CargoParam cargoParam);
    Integer getTotalStoreBoxesByExample(CargoParam cargoParam);
    Integer getInternalTotalStoreBoxesByExample(CargoParam cargoParam);
    Integer selectCountByExample(CargoParam cargoParam);
    void storeByContractId(String contractId);
    void selloutByCargoId(String cargoId);
    void storeByCargoId(String cargoId);
    List<CargoSellInfo> getSellList(CargoParam cargoParam);
    Integer countSellList(CargoParam cargoParam);
    CargoTotalInfo getTotalStoreInfoForQuery(CargoParam cargoParam);
    List<CargoStoreInfo> getStoreList(CargoParam cargoParam);
    Integer countStoreList(CargoParam cargoParam);
    CargoTotalInfo getTotalStoreInInfo(CargoParam cargoParam);
    Integer getAvgStatusByContractId(String contractId);
}