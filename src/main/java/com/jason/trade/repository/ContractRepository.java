package com.jason.trade.repository;

import com.jason.trade.model.ContractBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContractRepository extends JpaRepository<ContractBaseInfo,Integer>,JpaSpecificationExecutor<ContractBaseInfo> {
    ContractBaseInfo findByExternalContractAndStatus(String externalContract, String status);
    ContractBaseInfo findByContractId(String contractId);
    List<ContractBaseInfo> findByStatusNot(String status);

    @Query(value = "update contract_base_info set status=?2 where id in (?1) ", nativeQuery = true)
    @Modifying
    void updateStatus(List<String> id,String status);
}
