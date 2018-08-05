package com.jason.trade.mapper;

import com.jason.trade.model.Attachment;
import com.jason.trade.model.AttachmentKey;

public interface AttachmentMapper {
    int deleteByPrimaryKey(AttachmentKey key);

    int insert(Attachment record);

    int insertSelective(Attachment record);

    Attachment selectByPrimaryKey(AttachmentKey key);

    int updateByPrimaryKeySelective(Attachment record);

    int updateByPrimaryKey(Attachment record);
}