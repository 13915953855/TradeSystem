package com.jason.trade.mapper;

import com.jason.trade.entity.*;
import com.jason.trade.model.CargoInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface CargoInfoMapper {
    int updateByPrimaryKeySelective(CargoInfo record);
    int updateByCargoId(CargoInfo record);
    int deleteByContract(List<String> id);
    CargoInfo selectByCargoId(String cargoId);
    List<CargoManageInfo> selectByExample(CargoParam cargoParam);
    List<CargoManageInfo> selectTotalByExample(CargoParam cargoParam);
    List<CargoManageInfo> selectByExampleForPre(CargoParam cargoParam);
    List<CargoManageInfo> selectTotalByExampleForPre(CargoParam cargoParam);
    Integer getTotalStoreWeightByExample(CargoParam cargoParam);
    Integer getInternalTotalStoreWeightByExample(CargoParam cargoParam);
    Integer getTotalStoreBoxesByExample(CargoParam cargoParam);
    Integer getInternalTotalStoreBoxesByExample(CargoParam cargoParam);
    Integer selectCountByExample(CargoParam cargoParam);
    Integer countCargoByContractId(CargoParam cargoParam);
    Integer selectCountByExampleForPre(CargoParam cargoParam);
    void storeByContractId(String contractId);
    void updateStatusByContractId(@Param("contractId") String contractId, @Param("status") String status);
    void selloutByCargoId(String cargoId);
    void storeByCargoId(String cargoId);
    List<CargoSellInfo> getSellList(CargoParam cargoParam);
    Integer countSellList(CargoParam cargoParam);
    CargoTotalInfo getTotalStoreInfoForQuery(CargoParam cargoParam);
    List<CargoStoreInfo> getStoreList(CargoParam cargoParam);
    List<CargoStoreInfo> getStoreList2(CargoParam cargoParam);
    Integer countStoreList(CargoParam cargoParam);
    CargoTotalInfo getTotalStoreInInfo(CargoParam cargoParam);
    Integer getAvgStatusByContractId(String contractId);
}