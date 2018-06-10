package com.jason.trade.repository;

import com.jason.trade.entity.ContractBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<ContractBaseInfo,Integer> {
    ContractBaseInfo findByExternalContract(String externalContract);
    List<ContractBaseInfo> findByStatus(String status);
}
