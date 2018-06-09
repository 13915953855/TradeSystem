package com.jason.trade.repository;

import com.jason.trade.entity.ContractBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<ContractBaseInfo,Integer> {
}
