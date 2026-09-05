package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.UserAssignedRegion;
import com.gneworks.dto.res.RegionWorkerRes;
import com.gneworks.dto.res.UserAssignedRegionDetailRes;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserAssignedRegionMapper {
    int insert(UserAssignedRegion row);

    int deleteByPrimaryKey(String assignedRegionId);

    int deleteByUserIdAndRegionId(@Param("userId") String userId, @Param("regionId") String regionId);

    int deleteByUserId(String userId);

    List<UserAssignedRegion> selectAll();

    List<UserAssignedRegionDetailRes> selectAssignedRegionsByUserId(@Param("userId") String userId);

    List<RegionWorkerRes> selectRegionWorkers(
            @Param("sido") String sido,
            @Param("sigungu") String sigungu,
            @Param("regionId") String regionId
    );
}
