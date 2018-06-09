package com.jason.trade.repository;

import com.jason.trade.entity.CargoInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<CargoInfo,Integer> {
}
