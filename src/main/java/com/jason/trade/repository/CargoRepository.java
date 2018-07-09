package com.jason.trade.repository;

import com.jason.trade.model.CargoInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CargoRepository extends JpaRepository<CargoInfo,Integer>,JpaSpecificationExecutor<CargoInfo> {
    List<CargoInfo> findByContractIdAndStatus(String contractId,String status);
    List<CargoInfo> findByContractIdAndStatusNot(String contractId,String status);
    Page<CargoInfo> findByContractIdAndStatus(String contractId, String status, Pageable pageable);
    Page<CargoInfo> findByContractIdAndStatusNot(String contractId,String status,Pageable pageable);
    Page<CargoInfo> findByStatus(String status,Pageable pageable);
    CargoInfo findByCargoId(String cargoId);
    //原生SQL实现更新方法接口
    @Query(value = "update cargo_info set status=?2 where id in (?1) ", nativeQuery = true)
    @Modifying
    void updateStatus(List<String> id,String status);

    @Query(value = "update cargo_info set expect_store_weight = ?2,expect_store_boxes=?3,real_store_weight=?4,real_store_boxes=?5 where cargo_id = ?1 ", nativeQuery = true)
    @Modifying
    void updateSaleInfo(String cargoId,double expectSaleWeight,Integer expectSaleBoxes,double realSaleWeight,Integer realSaleBoxes);

    @Query(value = "update cargo_info set status='1' where contract_id in (select contract_id from contract_base_info where id in (?1)) ", nativeQuery = true)
    @Modifying
    void deleteByContract(List<String> id);
}
