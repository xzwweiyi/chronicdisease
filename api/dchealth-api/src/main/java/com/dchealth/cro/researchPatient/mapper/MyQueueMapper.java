package com.dchealth.cro.researchPatient.mapper;

import com.dchealth.cro.researchPatient.domain.MyQueue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 我的队列mapper
 * @author zhangxiao
 */
public interface MyQueueMapper {

    /**
     * 根据个人ID查找队列列表
     * 此方法旨在为特定个人ID获取相关的队列列表
     *
     * @param personId 个人的唯一标识符
     * @return 包含MyQueue对象的列表如果找不到匹配的队列，返回空列表
     */
    List<MyQueue> findListByPersonId(@Param("personId") Long personId);
}
