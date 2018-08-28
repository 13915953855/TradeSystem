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
    List<CargoInfo> findByContractIdAndStatus(String contractId,String status);
    List<CargoInfo> findByContractIdAndStatusNot(String contractId,String status);
    Page<CargoInfo> findByContractIdAndStatus(String contractId, String status, Pageable pageable);
    Page<CargoInfo> findByContractIdAndStatusNot(String contractId,String status,Pageable pageable);
    Page<CargoInfo> findByStatus(String status,Pageable pageable);
    CargoInfo findByCargoIdAndStatus(String cargoId, String status);
    //原生SQL实现更新方法接口
    @Query(value = "update cargo_info set status=?2 where id in (?1) ", nativeQuery = true)
    @Modifying
    void updateStatus(List<String> id,String status);

    @Query(value = "update cargo_info set real_store_weight = ?2,real_store_boxes = ?3 where cargo_id = ?1 ", nativeQuery = true)
    @Modifying
    @Transactional
    void updateWeightAndBoxes(String cargoId,String weight,String boxes);

    @Query(value = "delete from cargo_info where id in (?1) ", nativeQuery = true)
    @Modifying
    void deleteCargo(List<String> id);
}
