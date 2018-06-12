package com.jason.trade.repository;

import com.jason.trade.entity.CargoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CargoRepository extends JpaRepository<CargoInfo,Integer> {
    List<CargoInfo> findByContractIdAndStatus(String contractId,String status);
    //原生SQL实现更新方法接口
    @Query(value = "update cargo_info set status=?2 where id in (?1) ", nativeQuery = true)
    @Modifying
    public void updateStatus(List<String> id,String status);
}
