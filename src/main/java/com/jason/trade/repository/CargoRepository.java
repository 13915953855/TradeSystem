package com.jason.trade.repository;

import com.jason.trade.model.CargoInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CargoRepository extends JpaRepository<CargoInfo,Integer>,JpaSpecificationExecutor<CargoInfo> {
    List<CargoInfo> findByContractId(String contractId);
    CargoInfo findByCargoId(String cargoId);

    @Query(value = "update cargo_info set real_store_weight = ?2,real_store_boxes = ?3 where cargo_id = ?1 ", nativeQuery = true)
    @Modifying
    @Transactional
    void updateWeightAndBoxes(String cargoId,String weight,String boxes);

    @Query(value = "delete from cargo_info where id in (?1) ", nativeQuery = true)
    @Modifying
    void deleteCargo(List<String> id);

    //当前库存箱数≤5，预警通知；
    //select * from cargo_info where real_store_boxes <=5 and real_store_boxes > 0;
    List<CargoInfo> findByRealStoreBoxesBetween(Integer start,Integer end);
    Integer countByRealStoreBoxesBetween(Integer start,Integer end);
}
