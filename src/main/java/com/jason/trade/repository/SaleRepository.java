package com.jason.trade.repository;

import com.jason.trade.entity.SaleInfo;
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
    @Query(value = "update sale_info set status=?2 where sale_id in (?1) ", nativeQuery = true)
    @Modifying
    void updateStatus(List<String> id,String status);

    //update sale_info set status='1' where cargo_id in (select cargo_id from cargo_info where contract_id in (select contract_id from contract_base_info where id > 7))
    @Query(value = "update sale_info set status='1' where cargo_id in (select cargo_id from cargo_info where contract_id in (select contract_id from contract_base_info where id in (?1)))", nativeQuery = true)
    @Modifying
    void deleteByContract(List<String> id);
}
