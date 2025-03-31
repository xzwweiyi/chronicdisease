package com.dchealth.msgTip.service;


import com.dchealth.msgTip.domain.MsgReminderTaskTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 随访消息提醒模板(MsgReminderTaskTemplate)表服务接口
 *
 * @author makejava
 * @since 2023-06-06 10:05:22
 */
public interface MsgReminderTaskTemplateService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MsgReminderTaskTemplate queryById(Long id);

    /**
     * 分页查询
     *
     * @param msgReminderTaskTemplate 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<MsgReminderTaskTemplate> queryByPage(MsgReminderTaskTemplate msgReminderTaskTemplate, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param msgReminderTaskTemplate 实例对象
     * @return 实例对象
     */
    MsgReminderTaskTemplate insert(MsgReminderTaskTemplate msgReminderTaskTemplate);

    /**
     * 修改数据
     *
     * @param msgReminderTaskTemplate 实例对象
     * @return 实例对象
     */
    MsgReminderTaskTemplate update(MsgReminderTaskTemplate msgReminderTaskTemplate);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
