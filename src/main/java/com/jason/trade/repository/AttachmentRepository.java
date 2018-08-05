package com.jason.trade.repository;

import com.jason.trade.model.Attachment;
import com.jason.trade.model.AttachmentKey;
import com.jason.trade.model.CargoInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment,AttachmentKey> {
    List<Attachment> findByContractId(String contractId);
}
