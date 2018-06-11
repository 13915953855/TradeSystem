package com.jason.trade.repository;

import com.jason.trade.entity.CargoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CargoRepository extends JpaRepository<CargoInfo,Integer> {
    List<CargoInfo> findByContractId(Integer contractId);
    //原生SQL实现更新方法接口
    @Query(value = "update cargo_info set contractId=?1,status=0 where id in ?2 ", nativeQuery = true)
    @Modifying
    public void updateContractIdAndStatus(int contractId,String id);
}
