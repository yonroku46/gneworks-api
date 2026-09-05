package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.Inquiry;
import jakarta.annotation.Generated;
import java.util.List;

public interface InquiryMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(String inquiryId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(Inquiry row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    Inquiry selectByPrimaryKey(String inquiryId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<Inquiry> selectAll();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(Inquiry row);
    
    int saveInquiry(Inquiry row);
    
    int countPendingInquiries();
    
    Inquiry selectLatestPendingInquiry();
}