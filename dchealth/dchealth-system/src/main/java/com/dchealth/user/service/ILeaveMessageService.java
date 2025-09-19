package com.dchealth.user.service;



import com.dchealth.user.domain.LeaveMessage;
import com.dchealth.user.vo.UserMessageListVO;
import com.dchealth.user.vo.req.UserMessageListReq;

import java.util.List;

/**
 * 用户指导聊天Service接口
 * 
 * @author lchbu 
 * @date 2023-03-17
 */
public interface ILeaveMessageService
{

    /**
     * 查询用户指导聊天
     *
     * @param id 用户指导聊天主键
     * @return 用户指导聊天
     */
    public LeaveMessage selectLeaveMessageById(Long id);

    /**
     * 查询用户指导聊天列表
     *
     * @param leaveMessage 用户指导聊天
     * @return 用户指导聊天集合
     */
    public List<UserMessageListVO> selectLeaveMessageList(LeaveMessage leaveMessage);

    /**
     * 新增用户指导聊天
     *
     * @param leaveMessage 用户指导聊天
     * @return 结果
     */
    public int insertLeaveMessage(LeaveMessage leaveMessage);

    /**
     * 修改用户指导聊天
     *
     * @param leaveMessage 用户指导聊天
     * @return 结果
     */
    public int updateLeaveMessage(LeaveMessage leaveMessage);

    /**
     * 批量删除用户指导聊天
     *
     * @param ids 需要删除的用户指导聊天主键集合
     * @return 结果
     */
    public int deleteLeaveMessageByIds(List<Long> ids);

    /**
     * 删除用户指导聊天信息
     *
     * @param id 用户指导聊天主键
     * @return 结果
     */
    public int deleteLeaveMessageById(Long id);

    /**
     * 发送指导聊天信息
     * @param messageListReq
     * @return
     */
    Long sendMessage(UserMessageListReq messageListReq);

    Boolean updateReadStatus(String personId);

}
