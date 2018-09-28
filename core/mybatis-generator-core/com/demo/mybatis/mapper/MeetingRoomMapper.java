package com.demo.mybatis.mapper;

import com.demo.mybatis.entity.MeetingRoom;
import com.demo.mybatis.example.MeetingRoomExample;
import com.demo.mybatis.query.MeetingRoomQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MeetingRoomMapper {
    /**
     * This method corresponds to the database table t_meeting_room 
     */
    long countByExample(MeetingRoomExample example) throws Exception;

    /**
     * This method corresponds to the database table t_meeting_room to delete record
     */
    int deleteByExample(MeetingRoomExample example) throws Exception;

    /**
     * This method corresponds to the database table t_meeting_room to delete record
     */
    int deleteByPrimaryKey(Integer id) throws Exception;

    /**
     * This method corresponds to the database table t_meeting_room to insert record
     */
    int insert(MeetingRoom record) throws Exception;

    /**
     * This method corresponds to the database table t_meeting_room to insert record
     */
    int insertSelective(MeetingRoom record) throws Exception;

    /**
     * This method corresponds to the database table t_meeting_room to select record
     */
    List<MeetingRoom> selectByExample(MeetingRoomExample example) throws Exception;

    /**
     * This method corresponds to the database table t_meeting_room to select record
     */
    MeetingRoom selectByPrimaryKey(Integer id) throws Exception;

    /**
     * This method corresponds to the database table t_meeting_room to update record
     */
    int updateByExampleSelective(@Param("record") MeetingRoom record, @Param("example") MeetingRoomExample example) throws Exception;

    /**
     * This method corresponds to the database table t_meeting_room to update record
     */
    int updateByExample(@Param("record") MeetingRoom record, @Param("example") MeetingRoomExample example) throws Exception;

    /**
     * This method corresponds to the database table t_meeting_room to update record
     */
    int updateByPrimaryKeySelective(MeetingRoom record) throws Exception;

    /**
     * This method corresponds to the database table t_meeting_room to update record
     */
    int updateByPrimaryKey(MeetingRoom record) throws Exception;

    /**
     * 批量插入
     * @param records 模型列表
     */
    int insertBatch(List<MeetingRoom> records) throws Exception;

    /**
     * 批量修改（部分字段）
     * @param example 修改样本（只含修改字段）
     * @param ids 目标记录ID列表
     */
    int updateSelectiveBatch(@Param("example") MeetingRoom example, @Param("ids") List<Integer> ids) throws Exception;

    /**
     * 批量查询
     * @param example 查询样本
     */
    List<MeetingRoom> selectBatch(MeetingRoomQuery example) throws Exception;

    /**
     * 查询记录数
     * @param example 查询样本
     */
    int selectAmount(MeetingRoomQuery example) throws Exception;
}