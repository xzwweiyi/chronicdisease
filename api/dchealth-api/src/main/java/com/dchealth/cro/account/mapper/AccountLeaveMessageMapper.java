package com.dchealth.cro.account.mapper;


import com.dchealth.cro.account.domain.AccountLeaveMessage;
import com.dchealth.cro.account.form.vo.UserMessageListVO;
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
public interface AccountLeaveMessageMapper {
    /**
     * 查询预警指导聊天
     * 
     * @param id 预警指导聊天主键
     * @return 预警指导聊天
     */
    public AccountLeaveMessage selectLeaveMessageById(Long id);

    /**
     * 查询预警指导聊天列表
     * 
     * @param LeaveMessage 预警指导聊天
     * @return 预警指导聊天集合
     */
    public List<AccountLeaveMessage> selectLeaveMessageList(AccountLeaveMessage LeaveMessage);

    /**
     * 新增预警指导聊天
     * 
     * @param LeaveMessage 预警指导聊天
     * @return 结果
     */
    public int insertLeaveMessage(AccountLeaveMessage LeaveMessage);

    /**
     * 修改预警指导聊天
     * 
     * @param LeaveMessage 预警指导聊天
     * @return 结果
     */
    public int updateLeaveMessage(AccountLeaveMessage LeaveMessage);

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
    public int deleteLeaveMessageByIds(List<Long> ids);

    /**
     * 查询是否有未读的消息
     * @param personId
     * @param userId
     * @return
     */
    List<AccountLeaveMessage>  selectUpdateReadStatus(@Param("personId")Long personId, @Param("userId") Long userId);


    /**
     * 根据personId查询
     */
    List<UserMessageListVO> selectLeaveMessageByPersonId(Long personId);
}
