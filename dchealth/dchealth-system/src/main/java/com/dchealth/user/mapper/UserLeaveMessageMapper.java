package com.dchealth.user.mapper;

import com.dchealth.user.domain.LeaveMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预警指导聊天Mapper接口
 * 
 * @author lchbu
 * @date 2023-03-17
 */
@Mapper
public interface UserLeaveMessageMapper
{
    /**
     * 查询预警指导聊天
     * 
     * @param id 预警指导聊天主键
     * @return 预警指导聊天
     */
    public LeaveMessage selectLeaveMessageById(Long id);

    /**
     * 查询预警指导聊天列表
     * 
     * @param LeaveMessage 预警指导聊天
     * @return 预警指导聊天集合
     */
    public List<LeaveMessage> selectLeaveMessageList(LeaveMessage LeaveMessage);

    /**
     * 新增预警指导聊天
     * 
     * @param LeaveMessage 预警指导聊天
     * @return 结果
     */
    public int insertLeaveMessage(LeaveMessage LeaveMessage);

    /**
     * 修改预警指导聊天
     * 
     * @param LeaveMessage 预警指导聊天
     * @return 结果
     */
    public int updateLeaveMessage(LeaveMessage LeaveMessage);

    /**
     * 删除预警指导聊天
     * 
     * @param id 预警指导聊天主键
     * @return 结果
     */
    public int deleteLeaveMessageById(Long id);

    /**
     * 批量删除预警指导聊天
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLeaveMessageByIds(@Param("ids")List<Long> ids);

    /**
     * 查询是否有未读的消息
     * @param personId
     * @param userId
     * @return
     */
    List<LeaveMessage>  selectUpdateReadStatus(@Param("personId")String personId, @Param("senderId")String senderId);
}
