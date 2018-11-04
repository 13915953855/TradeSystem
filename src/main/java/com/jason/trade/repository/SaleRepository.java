package com.jason.trade.repository;

import com.jason.trade.model.SaleInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleRepository extends JpaRepository<SaleInfo,Integer> {
    Page<SaleInfo> findByCargoIdAndStatus(String cargoId, String status, Pageable pageable);
    List<SaleInfo> findByCargoIdAndStatus(String cargoId, String status);
    //原生SQL实现更新方法接口
    @Query(value = "delete from sale_info where sale_id in (?1) ", nativeQuery = true)
    @Modifying
    void deleteSaleInfo(List<String> id);

    //当利润≤0，预警通知   select * from sale_info where profit <= 0;
    List<SaleInfo> findByProfitLessThan(Double profit);
    Integer countByProfitLessThan(Double profit);
}
