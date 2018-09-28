package com.demo.mybatis.query;

public class MeetingRoomQuery extends com.demo.mybatis.entity.MeetingRoom {
    /**
     * 查询条件
     */
    private CommonConditionQuery queryCondition;

    public CommonConditionQuery getQueryCondition() {
        return this.queryCondition;
    }

    public void setQueryCondition(CommonConditionQuery queryCondition) {
        this.queryCondition=queryCondition;
    }
}