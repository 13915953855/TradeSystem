package com.jason.trade.repository;

import com.jason.trade.model.OptionInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: 徐森
 * @CreateDate: 2019/1/21
 * @Description:
 */
public interface OptionInfoRepository extends JpaRepository<OptionInfo,Integer> {
    List<OptionInfo> findByGroupEquals(String group);
}
