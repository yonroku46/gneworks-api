package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.PushSubscription;
import org.apache.ibatis.annotations.Param;

import javax.annotation.processing.Generated;
import java.util.List;

public interface PushSubscriptionMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(String subscriptionId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(PushSubscription row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    PushSubscription selectByPrimaryKey(String subscriptionId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<PushSubscription> selectAll();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(PushSubscription row);

    PushSubscription selectByEndpoint(String endpoint);

    List<PushSubscription> selectByUserId(String userId);

    List<PushSubscription> selectAdminSubscriptions();

    int deleteByEndpoint(String endpoint);

    int deleteByUserIdAndEndpoint(@Param("userId") String userId, @Param("endpoint") String endpoint);
}
