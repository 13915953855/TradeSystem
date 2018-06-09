package com.jason.trade.repository;

import com.jason.trade.entity.TradeBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<TradeBaseInfo,Integer> {
}
