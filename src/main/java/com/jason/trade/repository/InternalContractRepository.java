package com.jason.trade.repository;

import com.jason.trade.model.InternalContractInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InternalContractRepository extends JpaRepository<InternalContractInfo,Integer>,JpaSpecificationExecutor<InternalContractInfo> {
    InternalContractInfo findByContractId(String contractId);
}
