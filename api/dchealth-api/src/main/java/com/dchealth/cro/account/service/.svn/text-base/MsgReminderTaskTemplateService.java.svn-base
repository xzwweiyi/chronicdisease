package com.dchealth.cro.account.service;

import com.dchealth.cro.account.domain.MsgReminderTaskTemplate;
import com.dchealth.cro.account.mapper.MsgReminderTaskTemplateMapper;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 随访消息提醒模板(MsgReminderTaskTemplate)表服务实现类
 *
 * @author makejava
 * @since 2023-06-06 09:49:54
 */
@Service
public class MsgReminderTaskTemplateService{
    @Resource
    private MsgReminderTaskTemplateMapper msgReminderTaskTemplateMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
   
    public MsgReminderTaskTemplate queryById(Long id) {
        return this.msgReminderTaskTemplateMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param msgReminderTaskTemplate 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
   
    public Page<MsgReminderTaskTemplate> queryByPage(MsgReminderTaskTemplate msgReminderTaskTemplate, PageRequest pageRequest) {
        long total = this.msgReminderTaskTemplateMapper.count(msgReminderTaskTemplate);
        return new PageImpl<>(this.msgReminderTaskTemplateMapper.queryAllByLimit(msgReminderTaskTemplate, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param msgReminderTaskTemplate 实例对象
     * @return 实例对象
     */
   
    public MsgReminderTaskTemplate insert(MsgReminderTaskTemplate msgReminderTaskTemplate) {
        this.msgReminderTaskTemplateMapper.insert(msgReminderTaskTemplate);
        return msgReminderTaskTemplate;
    }

    /**
     * 修改数据
     *
     * @param msgReminderTaskTemplate 实例对象
     * @return 实例对象
     */
   
    public MsgReminderTaskTemplate update(MsgReminderTaskTemplate msgReminderTaskTemplate) {
        this.msgReminderTaskTemplateMapper.update(msgReminderTaskTemplate);
        return this.queryById(msgReminderTaskTemplate.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
   
    public boolean deleteById(Long id) {
        return this.msgReminderTaskTemplateMapper.deleteById(id) > 0;
    }
}
