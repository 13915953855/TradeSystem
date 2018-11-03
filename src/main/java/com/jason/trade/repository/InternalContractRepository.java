package com.jason.trade.repository;

import com.jason.trade.model.InternalContractInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface InternalContractRepository extends JpaRepository<InternalContractInfo,Integer>,JpaSpecificationExecutor<InternalContractInfo> {
    InternalContractInfo findByContractId(String contractId);
    InternalContractInfo findByContractNo(String contractNo);

    @Query(value = "delete from internal_contract_info where id in (?1)", nativeQuery = true)
    @Modifying
    void deleteContract(List<String> id);

    @Query(value = "update internal_contract_info set status=?2 where contract_id in (?1) ", nativeQuery = true)
    @Modifying
    @Transactional
    void updateStatusByContractId(List<String> id,String status);
}
