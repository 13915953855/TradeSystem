package com.jason.trade.repository;

import com.jason.trade.model.PreSaleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreSaleRepository extends JpaRepository<PreSaleInfo,Integer> {
    List<PreSaleInfo> findByCargoId(String cargoId);
    List<PreSaleInfo> findBySaleIdIn(String[] SaleIds);
    //原生SQL实现更新方法接口
    @Query(value = "delete from presale_info where sale_id in (?1) ", nativeQuery = true)
    @Modifying
    void deleteSaleInfo(List<String> id);

    @Query(value = "select * from presale_info where sale_id in (?1) ", nativeQuery = true)
    @Modifying
    List<PreSaleInfo> findBySaleId(List<String> id);
}
