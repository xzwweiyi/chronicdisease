package com.dchealth.warn.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.warn.mapper.WarnLeaveMessageMapper;
import com.dchealth.warn.domain.WarnLeaveMessage;
import com.dchealth.warn.service.IWarnLeaveMessageService;

/**
 * 预警指导聊天Service业务层处理
 * 
 * @author lchbu
 * @date 2023-03-17
 */
@Service
public class WarnLeaveMessageServiceImpl implements IWarnLeaveMessageService 
{
    @Autowired
    private WarnLeaveMessageMapper warnLeaveMessageMapper;

    /**
     * 查询预警指导聊天
     * 
     * @param id 预警指导聊天主键
     * @return 预警指导聊天
     */
    @Override
    public WarnLeaveMessage selectWarnLeaveMessageById(Long id)
    {
        return warnLeaveMessageMapper.selectWarnLeaveMessageById(id);
    }

    /**
     * 查询预警指导聊天列表
     * 
     * @param warnLeaveMessage 预警指导聊天
     * @return 预警指导聊天
     */
    @Override
    public List<WarnLeaveMessage> selectWarnLeaveMessageList(WarnLeaveMessage warnLeaveMessage)
    {
        return warnLeaveMessageMapper.selectWarnLeaveMessageList(warnLeaveMessage);
    }

    /**
     * 新增预警指导聊天
     * 
     * @param warnLeaveMessage 预警指导聊天
     * @return 结果
     */
    @Override
    public int insertWarnLeaveMessage(WarnLeaveMessage warnLeaveMessage)
    {
        warnLeaveMessage.setCreateTime(DateUtils.getNowDate());
        return warnLeaveMessageMapper.insertWarnLeaveMessage(warnLeaveMessage);
    }

    /**
     * 修改预警指导聊天
     * 
     * @param warnLeaveMessage 预警指导聊天
     * @return 结果
     */
    @Override
    public int updateWarnLeaveMessage(WarnLeaveMessage warnLeaveMessage)
    {
        return warnLeaveMessageMapper.updateWarnLeaveMessage(warnLeaveMessage);
    }

    /**
     * 批量删除预警指导聊天
     * 
     * @param ids 需要删除的预警指导聊天主键
     * @return 结果
     */
    @Override
    public int deleteWarnLeaveMessageByIds(Long[] ids)
    {
        return warnLeaveMessageMapper.deleteWarnLeaveMessageByIds(ids);
    }

    /**
     * 删除预警指导聊天信息
     * 
     * @param id 预警指导聊天主键
     * @return 结果
     */
    @Override
    public int deleteWarnLeaveMessageById(Long id)
    {
        return warnLeaveMessageMapper.deleteWarnLeaveMessageById(id);
    }
}
