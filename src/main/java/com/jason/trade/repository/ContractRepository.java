package com.jason.trade.repository;

import com.jason.trade.entity.ContractBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContractRepository extends JpaRepository<ContractBaseInfo,Integer>,JpaSpecificationExecutor<ContractBaseInfo> {
    ContractBaseInfo findByExternalContractAndStatus(String externalContract, String status);
    ContractBaseInfo findByContractId(String contractId);
    List<ContractBaseInfo> findByStatus(String status);
}
