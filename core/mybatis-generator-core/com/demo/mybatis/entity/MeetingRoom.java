package com.demo.mybatis.entity;

import com.demo.mybatis.base.BaseGroup.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import javax.validation.constraints.Size;

/**
 * 
 * This class corresponds to the database table t_meeting_room
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeetingRoom extends IdEntity {
    /**
     *   会议室名称
     */
    @Size(max = 64, message = "会议室名称长度不能超过64", groups = {CreateGroup.class,ModifyGroup.class,QueryGroup.class,ConditionedQueryGroup.class})
    private String roomName;

    /**
     */
    private Date beginTime;

    /**
     */
    private Date endTime;

    /**
     *   会议室容量，取值范围0-9999
     */
    private Integer capacity;

    /**
     *   会议室位置
     */
    @Size(max = 128, message = "会议室位置长度不能超过128", groups = {CreateGroup.class,ModifyGroup.class,QueryGroup.class,ConditionedQueryGroup.class})
    private String position;

    /**
     *   备注
     */
    @Size(max = 256, message = "备注长度不能超过256", groups = {CreateGroup.class,ModifyGroup.class,QueryGroup.class,ConditionedQueryGroup.class})
    private String remark;

    /**
     *   公司ID
     */
    private Integer companyId;

    /**
     *   创建人ID
     */
    private Integer createUser;

    /**
     *   创建时间
     */
    private Date createTime;

    /**
     *   最后修改人
     */
    private Integer updateUser;

    /**
     *   修改时间
     */
    private Date updateTime;

    /**
     *   会议室是否可用  0：不可用  1：可用
     */
    private Integer isEnable;

    /**
     *   保留字段,应对未来需求变化,需求添加多个字段时可以采用json格式字符串存储
     */
    @Size(max = 5, message = "保留字段,应对未来需求变化,需求添加多个字段时可以采用json格式字符串存储长度不能超过5", groups = {CreateGroup.class,ModifyGroup.class,QueryGroup.class,ConditionedQueryGroup.class})
    private String reserve;

    /**
     *   删除标识  0：未删除  1：已删除
     */
    private Integer status;

    /**
     * @return the value of t_meeting_room.room_name
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * @param roomName the value for t_meeting_room.room_name
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? null : roomName.trim();
    }

    /**
     * @return the value of t_meeting_room.begin_time
     */
    public Date getBeginTime() {
        return beginTime;
    }

    /**
     * @param beginTime the value for t_meeting_room.begin_time
     */
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    /**
     * @return the value of t_meeting_room.end_time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the value for t_meeting_room.end_time
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the value of t_meeting_room.capacity
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the value for t_meeting_room.capacity
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the value of t_meeting_room.position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the value for t_meeting_room.position
     */
    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    /**
     * @return the value of t_meeting_room.remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the value for t_meeting_room.remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return the value of t_meeting_room.company_id
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the value for t_meeting_room.company_id
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the value of t_meeting_room.create_user
     */
    public Integer getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the value for t_meeting_room.create_user
     */
    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    /**
     * @return the value of t_meeting_room.create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the value for t_meeting_room.create_time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the value of t_meeting_room.update_user
     */
    public Integer getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser the value for t_meeting_room.update_user
     */
    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the value of t_meeting_room.update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime the value for t_meeting_room.update_time
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return the value of t_meeting_room.is_enable
     */
    public Integer getIsEnable() {
        return isEnable;
    }

    /**
     * @param isEnable the value for t_meeting_room.is_enable
     */
    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * @return the value of t_meeting_room.reserve
     */
    public String getReserve() {
        return reserve;
    }

    /**
     * @param reserve the value for t_meeting_room.reserve
     */
    public void setReserve(String reserve) {
        this.reserve = reserve == null ? null : reserve.trim();
    }

    /**
     * @return the value of t_meeting_room.status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the value for t_meeting_room.status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}