package com.jason.trade.repository;

import com.jason.trade.model.ContractBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ContractRepository extends JpaRepository<ContractBaseInfo,Integer>,JpaSpecificationExecutor<ContractBaseInfo> {
    ContractBaseInfo findByExternalContractAndStatus(String externalContract, String status);
    ContractBaseInfo findByExternalContractAndStatusNot(String externalContract, String status);
    ContractBaseInfo findByContractId(String contractId);
    List<ContractBaseInfo> findByStatusNot(String status);

    @Query(value = "delete from contract_base_info where id in (?1)", nativeQuery = true)
    @Modifying
    void deleteContract(List<String> id);

    @Query(value = "update contract_base_info set status=?2 where contract_id in (?1) ", nativeQuery = true)
    @Modifying
    @Transactional
    void updateStatusByContractId(List<String> id,String status);
}
