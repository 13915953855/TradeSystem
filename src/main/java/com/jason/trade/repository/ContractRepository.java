package com.jason.trade.repository;

import com.jason.trade.model.ContractBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ContractRepository extends JpaRepository<ContractBaseInfo,Integer>,JpaSpecificationExecutor<ContractBaseInfo> {
    ContractBaseInfo findByExternalContractAndStatus(String externalContract, String status);
    ContractBaseInfo findByExternalContractAndStatusNot(String externalContract, String status);
    ContractBaseInfo findByContractId(String contractId);
    ContractBaseInfo findById(Integer id);
    List<ContractBaseInfo> findByStatusNot(String status);

    @Query(value = "delete from contract_base_info where id in (?1)", nativeQuery = true)
    @Modifying
    void deleteContract(List<String> id);

    @Query(value = "update contract_base_info set status=?2 where contract_id in (?1) ", nativeQuery = true)
    @Modifying
    @Transactional
    void updateStatusByContractId(List<String> id,String status);

    //-- 当前日期超出预计船期14天，预警通知；
    //select * from contract_base_info where expect_Sailing_Date < '2018-10-20' and status = '1';
    List<ContractBaseInfo> findByExpectSailingDateLessThanAndStatus(String date,String status);
    Integer countByExpectSailingDateLessThanAndStatus(String date,String status);

    //-- 当前日期-入库日期＞60天，预警通知
    //select * from contract_base_info where store_date < '2018-09-03' and status = '4';
    List<ContractBaseInfo> findByStoreDateLessThanAndStatus(String date,String status);
    Integer countByStoreDateLessThanAndStatus(String date,String status);

    //-- 当前日期超出ETA时间15天，预警通知
    //select * from contract_base_info where eta < '2018-10-20' and status = '3';
    List<ContractBaseInfo> findByEtaLessThanAndStatus(String date,String status);
    Integer countByEtaLessThanAndStatus(String date,String status);
}
