package com.jason.trade.repository;

import com.jason.trade.model.CargoInfo;
import com.jason.trade.model.InternalCargoInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InternalCargoRepository extends JpaRepository<InternalCargoInfo,Integer>,JpaSpecificationExecutor<InternalCargoInfo> {
    List<InternalCargoInfo> findByContractIdAndStatus(String contractId, String status);
    List<InternalCargoInfo> findByContractIdAndStatusNot(String contractId, String status);
    Page<InternalCargoInfo> findByContractIdAndStatus(String contractId, String status, Pageable pageable);
    Page<InternalCargoInfo> findByContractIdAndStatusNot(String contractId, String status, Pageable pageable);
    Page<InternalCargoInfo> findByStatus(String status, Pageable pageable);
    InternalCargoInfo findByCargoIdAndStatus(String cargoId, String status);
    //原生SQL实现更新方法接口
    @Query(value = "update internal_cargo_info set status=?2 where id in (?1) ", nativeQuery = true)
    @Modifying
    void updateStatus(List<String> id, String status);

    @Query(value = "delete from internal_cargo_info where id in (?1) ", nativeQuery = true)
    @Modifying
    void deleteCargo(List<String> id);
}
