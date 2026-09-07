package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.User;
import com.gneworks.dto.req.AdminUserSearchReq;
import com.gneworks.dto.res.AdminUserRes;
import jakarta.annotation.Generated;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(String userId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(User row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    User selectByPrimaryKey(String userId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<User> selectAll();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(User row);

    List<User> findLoginUser(@Param("userId") String userId);

    User findUser(@Param("userId") String userId);

    List<User> selectAllActiveUsers();

    int updateProfile(User user);

    int updateUserByAdmin(User user);

    int updatePassword(@Param("userId") String userId, @Param("userPw") String userPw);

    int softDeleteUser(@Param("userId") String userId);

    int updateLastLogin(@Param("userId") String userId);

    List<AdminUserRes> selectUserListPaged(AdminUserSearchReq req);

    long selectUserCount(AdminUserSearchReq req);
}