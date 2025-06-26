package com.dchealth.user.service.impl;

import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.SecurityUtils;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.mapper.MpPersonMapper;
import com.dchealth.system.mapper.SysUserMapper;
import com.dchealth.user.domain.LeaveMessage;
import com.dchealth.user.mapper.UserLeaveMessageMapper;
import com.dchealth.user.service.ILeaveMessageService;
import com.dchealth.user.vo.UserMessageListVO;
import com.dchealth.user.vo.req.UserMessageListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 用户指导聊天Service业务层处理
 *
 * @author lchbu
 * @date 2023-03-17
 */
@Service
public class LeaveMessageServiceImpl implements ILeaveMessageService {
    @Autowired
    private UserLeaveMessageMapper userLeaveMessageMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private MpPersonMapper mpPersonMapper;

    /**
     * 查询用户指导聊天
     *
     * @param id 用户指导聊天主键
     * @return 用户指导聊天
     */
    @Override
    public LeaveMessage selectLeaveMessageById(Long id) {
        return userLeaveMessageMapper.selectLeaveMessageById(id);
    }

    /**
     * 查询指导聊天列表
     *
     * @param leaveMessage 指导聊天
     * @return 指导聊天
     */
    @Override
    public List<UserMessageListVO> selectLeaveMessageList(LeaveMessage leaveMessage) {

        List<UserMessageListVO> result = new ArrayList<>();
        List<LeaveMessage> list = userLeaveMessageMapper.selectLeaveMessageList(leaveMessage);
        if (list != null && !list.isEmpty()) {
            list.forEach(x -> {
                UserMessageListVO wvo = new UserMessageListVO();
                wvo.setId(x.getId());
                wvo.setAccountId(x.getAccountId());
                wvo.setMsgType(x.getContentType());
                wvo.setMsgTime(DateUtils.parseDateToStrFull(x.getCreateTime()));
                wvo.setMsgText(x.getContent());
                wvo.setUserType(x.getUserType());
                if ("1".equals(x.getUserType())) {
                    SysUser sysUser = sysUserMapper.selectUserById(x.getAccountId());
                    if (sysUser != null) {
                        wvo.setUserName(sysUser.getNickName());
                        wvo.setHeadImage(sysUser.getHeadImage());
                    }
                } else if ("2".equals(x.getUserType())) {
                    MpPerson mpPerson = mpPersonMapper.selectPersonByAccountId(x.getAccountId());
                    if (mpPerson != null) {
                        wvo.setUserName(mpPerson.getPersonName());
//                        wvo.setHeadImage(co);
                    }
                }
                result.add(wvo);
            });
        }

        return result;
    }

    /**
     * 新增用户指导聊天
     *
     * @param leaveMessage 用户指导聊天
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertLeaveMessage(LeaveMessage leaveMessage) {
        leaveMessage.setCreateTime(DateUtils.getNowDate());
        return userLeaveMessageMapper.insertLeaveMessage(leaveMessage);
    }

    /**
     * 修改用户指导聊天
     *
     * @param leaveMessage 用户指导聊天
     * @return 结果
     */
    @Override
    public int updateLeaveMessage(LeaveMessage leaveMessage) {
        return userLeaveMessageMapper.updateLeaveMessage(leaveMessage);
    }

    /**
     * 批量删除用户指导聊天
     *
     * @param ids 需要删除的用户指导聊天主键
     * @return 结果
     */
    @Override
    public int deleteLeaveMessageByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return userLeaveMessageMapper.deleteLeaveMessageByIds(ids);
    }

    /**
     * 删除用户指导聊天信息
     *
     * @param id 用户指导聊天主键
     * @return 结果
     */
    @Override
    public int deleteLeaveMessageById(Long id) {
        return userLeaveMessageMapper.deleteLeaveMessageById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long sendMessage(UserMessageListReq messageListReq) {
        Long userId = SecurityUtils.getLoginUser().getUserId();
        Date now = new Date();
        LeaveMessage leaveMessage = new LeaveMessage();
        leaveMessage.setRecordId(messageListReq.getPersonId())
                .setAccountId(messageListReq.getFromUserId())
                .setPersonId(messageListReq.getPersonId())
                .setSenderId(userId)
                .setSendTime(now)
                .setReceiverId(messageListReq.getPersonId())

                .setUserType("1")
                .setContentType(messageListReq.getMsgType());
        if ("1".equals(messageListReq.getMsgType())) {
            //文本
            leaveMessage.setContent(messageListReq.getMsgText());
        } else if ("2".equals(messageListReq.getMsgType())) {
            //图片
            leaveMessage.setContent(messageListReq.getMsgImageUrl());
        }
        leaveMessage.setCreateTime(new Date());
        userLeaveMessageMapper.insertLeaveMessage(leaveMessage);

        return leaveMessage.getId();
    }

    @Override
    public Boolean updateReadStatus(Long personId) {
        Long userId = SecurityUtils.getUserId();
        AtomicReference<Boolean> result = new AtomicReference<>(true);
        List<LeaveMessage> leaveMessage = userLeaveMessageMapper.selectUpdateReadStatus(personId, personId);
        if (leaveMessage != null) {
            // 更新是否已读的状态
            leaveMessage.forEach(x -> {
                x.setIsRead(true);
                x.setReceiverId(userId);
                x.setReceiveTime(new Date());
                int i = userLeaveMessageMapper.updateLeaveMessage(x);
                if (i <= 0) {
                    result.set(false);
                }
            });
        }

        return result.get();
    }
}
