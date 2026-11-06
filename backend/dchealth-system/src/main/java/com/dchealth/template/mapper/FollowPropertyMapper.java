package com.dchealth.template.mapper;

import com.dchealth.template.domain.FollowProperty;
import com.dchealth.template.domain.TextData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FollowPropertyMapper {
    List<FollowProperty> getFollowProperty(@Param("followId") Long followId, @Param("propertyCodeList") List<String> propertyCodeList);

    List<Map<String, Object>> getResult(@Param("tableName") String tableName, @Param("condition") String condition, @Param("personId") String personId);

    void insertTextData(@Param("researchId") Long researchId, @Param("recordId") Long recordId, @Param("list") List<TextData> list);


    List<Long> getDict(@Param("dictType")String dictType);
}
