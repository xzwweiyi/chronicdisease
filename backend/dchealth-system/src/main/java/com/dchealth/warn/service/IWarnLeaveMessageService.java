package com.dchealth.warn.service;

import java.util.List;
import com.dchealth.warn.domain.WarnLeaveMessage;

/**
 * 预警指导聊天Service接口
 * 
 * @author lchbu
 * @date 2023-03-17
 */
public interface IWarnLeaveMessageService 
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
     * 批量删除预警指导聊天
     * 
     * @param ids 需要删除的预警指导聊天主键集合
     * @return 结果
     */
    public int deleteWarnLeaveMessageByIds(Long[] ids);

    /**
     * 删除预警指导聊天信息
     * 
     * @param id 预警指导聊天主键
     * @return 结果
     */
    public int deleteWarnLeaveMessageById(Long id);
}
