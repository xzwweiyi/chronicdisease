package com.dchealth.cro.account.service;

import cn.hutool.core.util.StrUtil;
import com.dchealth.cro.account.domain.AccountLeaveMessage;
import com.dchealth.cro.account.domain.SysUser;
import com.dchealth.cro.account.form.req.UserMessageListReq;
import com.dchealth.cro.account.form.vo.UserMessageListVO;
import com.dchealth.cro.account.mapper.AccountLeaveMessageMapper;
import com.dchealth.cro.account.mapper.SysUserMapper;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.core.config.CroCoreConfig;
import com.dchealth.cro.health.domain.MpPerson;
import com.dchealth.cro.health.mapper.MpPersonMapper;
import com.dchealth.cro.medical.form.LeaveMessageForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
@Slf4j
@Service
public class LeaveMessageService {

    @Autowired
    private AccountLeaveMessageMapper leaveMessageMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private MpPersonMapper mpPersonMapper;

    @Autowired
    private CroCoreConfig croCoreConfig;

    @Value("${image.oldDomain}")
    private String oldDomain;

    @Value("${image.newDomain}")
    private String newDomain;


    @Transactional(rollbackFor = Exception.class)
    public DchealthCloudResponseEntity<List<UserMessageListVO>> saveLeaveMessage(LeaveMessageForm leaveMessageForm) {
        DchealthCloudResponseEntity<List<UserMessageListVO>> result = new DchealthCloudResponseEntity<>();
        List<UserMessageListVO> list = new ArrayList<>();

        AccountLeaveMessage leaveMessage = new AccountLeaveMessage();
        BeanUtils.copyProperties(leaveMessageForm, leaveMessage);
        leaveMessage.setCreateTime(new Date());
        leaveMessage.setAccountId(leaveMessageForm.getAccountId())
                .setSenderId(leaveMessageForm.getPersonId())
                .setSendTime(new Date())
                .setPersonId(leaveMessage.getPersonId())
                .setUserType("2")
        ;
        if ("2".equals(leaveMessageForm.getContentType()) && leaveMessageForm.getImageList() != null && leaveMessageForm.getImageList().size() > 0) {
            List<String> imageList = leaveMessageForm.getImageList();
            for (String imageUrl : imageList) {
                leaveMessage.setContent(imageUrl);
                leaveMessageMapper.insertLeaveMessage(leaveMessage);
            }
        } else if ("1".equals(leaveMessageForm.getContentType())) {
            leaveMessageMapper.insertLeaveMessage(leaveMessage);
        }
        ArrayList<UserMessageListVO> listVOS = new ArrayList<>();

        list = leaveMessageMapper.selectLeaveMessageByPersonId(leaveMessageForm.getPersonId());
        list.forEach(x -> {
            UserMessageListVO wvo = new UserMessageListVO();
            wvo.setMsgType(x.getContentType());
            wvo.setId(x.getId());
            wvo.setAccountId(x.getAccountId());
            wvo.setMsgTime(DateUtils.parseDateToStrFull(x.getCreateTime()));
            wvo.setMsgText(x.getContent());
            wvo.setUserType(x.getUserType());
            if ("1".equals(x.getUserType())) {
                SysUser sysUser = sysUserMapper.selectSysUserByUserId(x.getAccountId());
                if (sysUser != null) {
                    wvo.setHeadImage(sysUser.getHeadImage() == null ? "https://hzfw.dchealthclinical.com/files/admin.png" : sysUser.getHeadImage());
                    wvo.setUserName(sysUser.getNickName() == null ? "" : sysUser.getNickName());
                }
            } else if ("2".equals(x.getUserType())) {
                MpPerson mpPerson = mpPersonMapper.selectPersonByAccountId(x.getAccountId());
                if (mpPerson != null) {
                    wvo.setHeadImage(croCoreConfig.getUserHead());
                    wvo.setUserName(mpPerson.getPersonName());
                }
            }
            listVOS.add(wvo);
        });

        result.setData(listVOS);
        return result;
    }

    public List<UserMessageListVO> list(UserMessageListReq messageListReq) {
        List<UserMessageListVO> result = new ArrayList<>();
        AccountLeaveMessage leaveMessage = new AccountLeaveMessage();
        leaveMessage.setPersonId(messageListReq.getPersonId());
        List<AccountLeaveMessage> list = leaveMessageMapper.selectLeaveMessageList(leaveMessage);
        if (list != null && !list.isEmpty()) {
            list.forEach(x -> {
                UserMessageListVO wvo = new UserMessageListVO();
                wvo.setMsgType(x.getContentType());
                wvo.setId(x.getId());
                wvo.setAccountId(x.getAccountId());
                wvo.setMsgTime(DateUtils.parseDateToStrFull(x.getCreateTime()));
                wvo.setMsgText(x.getContent());
                wvo.setUserType(x.getUserType());
                if ("1".equals(x.getUserType())) {
                    SysUser sysUser = sysUserMapper.selectSysUserByUserId(x.getAccountId());
                    if (sysUser != null) {
                        wvo.setHeadImage(sysUser.getHeadImage() == null ? "https://hzfw.dchealthclinical.com/files/admin.png" : sysUser.getHeadImage());
                        wvo.setUserName(sysUser.getNickName() == null ? "" : sysUser.getNickName());
                    }
                } else if ("2".equals(x.getUserType())) {
                    MpPerson mpPerson = mpPersonMapper.selectPersonByAccountId(x.getAccountId());
                    if (mpPerson != null) {
                        wvo.setHeadImage(croCoreConfig.getUserHead());
                        wvo.setUserName(mpPerson.getPersonName());
                    }
                }

                String content = x.getContent();
                if (StrUtil.isNotEmpty(content) && content.contains(oldDomain)) {
                    String image = content.replace(oldDomain, newDomain);
                    wvo.setMsgText(image);
                }
                result.add(wvo);
            });
        }
        return result;
    }

    public Integer reminder(LeaveMessageForm messageForm) {

        // 医生回复后 没有回复红点提示条数
        List<AccountLeaveMessage> messageList = leaveMessageMapper.selectUpdateReadStatus(messageForm.getPersonId(),  messageForm.getPersonId());
        if (messageList != null && !messageList.isEmpty()) {
            return messageList.size();
        }
        return 0;
    }

    public Boolean updateStatus(LeaveMessageForm messageForm) {

        AtomicReference<Boolean> result = new AtomicReference<>(true);
        // 在患者回复后 需要将已读的状态变为未读
        List<AccountLeaveMessage> messageList = leaveMessageMapper.selectUpdateReadStatus(messageForm.getPersonId(),  messageForm.getPersonId());
        if (messageList != null && !messageList.isEmpty()) {
            messageList.forEach(x -> {
                x.setIsRead(true);
                x.setReceiveTime(new Date());
                x.setUpdateTime(new Date());
                int i = leaveMessageMapper.updateLeaveMessage(x);
                if (i <= 0) {
                    result.set(false);
                }
            });
        }
        return result.get();
    }


    public Integer clear(LeaveMessageForm messageForm) {
        // 删除单条聊天记录
        if (messageForm.getId() == null) {
            log.error("id不能为空");
            throw new RuntimeException("id不能为空");
        }

        int i = leaveMessageMapper.deleteLeaveMessageById(messageForm.getId());
        if (i <= 0) {
            log.error("删除失败");
            throw new RuntimeException("删除失败");
        }
        return i;
    }


    public Integer beachClear(LeaveMessageForm messageForm) {
        // 批量删除 ids 不能为空
        if (messageForm.getIds() == null || messageForm.getIds().isEmpty()) {
            log.error("ids不能为空");
            throw new RuntimeException("ids不能为空");
        }
        int i = leaveMessageMapper.deleteLeaveMessageByIds(messageForm.getIds());
        if (i <= 0) {
            log.error("批量删除失败");
            throw new RuntimeException("批量删除失败");
        }
        return i;
    }
}
