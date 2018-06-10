package com.jason.trade.repository;

import com.jason.trade.entity.CargoInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CargoRepository extends JpaRepository<CargoInfo,Integer> {
    List<CargoInfo> findByContractId(Integer contractId);
}
