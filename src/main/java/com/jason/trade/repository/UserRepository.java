package com.jason.trade.repository;

import com.jason.trade.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserInfo,Integer> {
    public UserInfo findByAccount(String account);
}
