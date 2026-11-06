package com.dchealth.msgTip.service;


import com.dchealth.msgTip.domain.MsgReminderTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 随访消息提醒配置表(MsgReminderTask)表服务接口
 *
 * @author makejava
 * @since 2023-06-06 10:04:56
 */
public interface MsgReminderTaskService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MsgReminderTask queryById(Long id);

    /**
     * 分页查询
     *
     * @param msgReminderTask 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<MsgReminderTask> queryByPage(MsgReminderTask msgReminderTask, PageRequest pageRequest);

    List<MsgReminderTask> queryList(MsgReminderTask msgReminderTask);
    List<MsgReminderTask> save(List<MsgReminderTask> msgReminderTasks);
    /**
     * 新增数据
     *
     * @param msgReminderTask 实例对象
     * @return 实例对象
     */
    MsgReminderTask insert(MsgReminderTask msgReminderTask);

    /**
     * 修改数据
     *
     * @param msgReminderTask 实例对象
     * @return 实例对象
     */
    MsgReminderTask update(MsgReminderTask msgReminderTask);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);
    boolean deleteByResearchId(Long id);

}
