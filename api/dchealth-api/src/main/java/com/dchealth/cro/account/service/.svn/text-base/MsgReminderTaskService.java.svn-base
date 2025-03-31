package com.dchealth.cro.account.service;


import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dchealth.cro.account.domain.MsgReminderTask;
import com.dchealth.cro.account.mapper.MsgReminderTaskMapper;

/**
 * 随访消息提醒配置表(MsgReminderTask)表服务实现类
 *
 * @author makejava
 * @since 2023-06-06 09:38:37
 */
@Service
public class MsgReminderTaskService  {
    @Resource
    private MsgReminderTaskMapper msgReminderTaskMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
   
    public MsgReminderTask queryById(Long id) {
        return this.msgReminderTaskMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param msgReminderTask 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
   
    public Page<MsgReminderTask> queryByPage(MsgReminderTask msgReminderTask, PageRequest pageRequest) {
        long total = this.msgReminderTaskMapper.count(msgReminderTask);
        return new PageImpl<>(this.msgReminderTaskMapper.queryAllByLimit(msgReminderTask, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param msgReminderTask 实例对象
     * @return 实例对象
     */
   
    public MsgReminderTask insert(MsgReminderTask msgReminderTask) {
        this.msgReminderTaskMapper.insert(msgReminderTask);
        return msgReminderTask;
    }

    /**
     * 修改数据
     *
     * @param msgReminderTask 实例对象
     * @return 实例对象
     */
   
    public MsgReminderTask update(MsgReminderTask msgReminderTask) {
        this.msgReminderTaskMapper.update(msgReminderTask);
        return this.queryById(msgReminderTask.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
   
    public boolean deleteById(Long id) {
        return this.msgReminderTaskMapper.deleteById(id) > 0;
    }
}
