package com.demo.mybatis.service.impl;

import com.demo.mybatis.entity.MeetingRoom;
import com.demo.mybatis.example.MeetingRoomExample;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MeetingRoomServiceImpl implements com.demo.mybatis.service.MeetingRoomService {
    @Autowired
    private com.demo.mybatis.mapper.ext.MeetingRoomExtMapper mapper;

    /**
     * 逻辑删除
     * @param id 目标记录ID
     * @param operatorId 操作人ID
     */
    protected int deleteLogical(int id, int operatorId) throws Exception {
        MeetingRoom record = new MeetingRoom();        
record.setId(id);        
record.setStatus(0);        
record.setUpdateTime(new Date());        
record.setUpdateUser(operatorId);        
return mapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 批量逻辑删除
     * @param ids 目标记录ID列表
     * @param operatorId 操作人ID
     */
    protected int deleteLogicalBatch(java.util.List<Integer> ids, int operatorId) throws Exception {
        if (ids == null || ids.size() == 0) {return 0;}        
MeetingRoom record = new MeetingRoom();        
record.setStatus(0);        
record.setUpdateTime(new Date());        
record.setUpdateUser(operatorId);        
MeetingRoomExample example = new MeetingRoomExample();        
example.createCriteria().andIdIn(ids).andStatusEqualTo(1);        
        
return mapper.updateByExampleSelective(record, example);
    }
}