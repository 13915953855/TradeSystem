package com.jason.trade.repository;

import com.jason.trade.entity.ContractBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContractRepository extends JpaRepository<ContractBaseInfo,Integer> {
    ContractBaseInfo findByExternalContract(String externalContract);
    List<ContractBaseInfo> findByStatus(String status);
    /*@Query(value = "select a from contract_base_info a where a.status='1' and a.external_contract like ?1")
    List<ContractBaseInfo> queryContractList(String externalContract);*/
}
