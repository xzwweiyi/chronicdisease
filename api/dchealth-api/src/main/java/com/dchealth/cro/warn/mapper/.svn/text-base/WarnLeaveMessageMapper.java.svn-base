package com.dchealth.cro.warn.mapper;


import com.dchealth.cro.warn.domain.WarnLeaveMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 预警指导聊天Mapper接口
 * 
 * @author lchbu
 * @date 2023-03-17
 */
@Repository
public interface WarnLeaveMessageMapper 
{
    /**
     * 查询预警指导聊天
     * 
     * @param id 预警指导聊天主键
     * @return 预警指导聊天
     */
    public WarnLeaveMessage selectWarnLeaveMessageById(Long id);

    /**
     * 查询预警指导聊天列表
     * 
     * @param warnLeaveMessage 预警指导聊天
     * @return 预警指导聊天集合
     */
    public List<WarnLeaveMessage> selectWarnLeaveMessageList(WarnLeaveMessage warnLeaveMessage);

    /**
     * 新增预警指导聊天
     * 
     * @param warnLeaveMessage 预警指导聊天
     * @return 结果
     */
    public int insertWarnLeaveMessage(WarnLeaveMessage warnLeaveMessage);

    /**
     * 修改预警指导聊天
     * 
     * @param warnLeaveMessage 预警指导聊天
     * @return 结果
     */
    public int updateWarnLeaveMessage(WarnLeaveMessage warnLeaveMessage);

    /**
     * 删除预警指导聊天
     * 
     * @param id 预警指导聊天主键
     * @return 结果
     */
    public int deleteWarnLeaveMessageById(Long id);

    /**
     * 批量删除预警指导聊天
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWarnLeaveMessageByIds(Long[] ids);

    /**
     * 根据周期id查询指导意见
     * @param recordId
     * @return
     */
    List<WarnLeaveMessage> selectWarnLeaveMessageByRecordId(Long recordId);
}
