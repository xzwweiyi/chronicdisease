package com.dchealth.msgTip.service.impl;


import com.dchealth.msgTip.domain.MsgReminderTask;
import com.dchealth.msgTip.mapper.MsgReminderTaskMapper;
import com.dchealth.msgTip.service.MsgReminderTaskService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 随访消息提醒配置表(MsgReminderTask)表服务实现类
 *
 * @author makejava
 * @since 2023-06-06 10:04:57
 */
@Service("msgReminderTaskService")
public class MsgReminderTaskServiceImpl implements MsgReminderTaskService {
    @Resource
    private MsgReminderTaskMapper msgReminderTaskMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
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
    @Override
    public Page<MsgReminderTask> queryByPage(MsgReminderTask msgReminderTask, PageRequest pageRequest) {
        long total = this.msgReminderTaskMapper.count(msgReminderTask);
        return new PageImpl<>(this.msgReminderTaskMapper.queryAllByLimit(msgReminderTask, pageRequest), pageRequest, total);
    }

    @Override
    public List<MsgReminderTask> queryList(MsgReminderTask msgReminderTask) {
        return this.msgReminderTaskMapper.queryAll(msgReminderTask);
    }

    @Override
    public List<MsgReminderTask> save(List<MsgReminderTask> msgReminderTasks) {
        msgReminderTaskMapper.insertBatch(msgReminderTasks);
        return msgReminderTasks;
    }

    /**
     * 新增数据
     *
     * @param msgReminderTask 实例对象
     * @return 实例对象
     */
    @Override
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
    @Override
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
    @Override
    public boolean deleteById(Long id) {
        return this.msgReminderTaskMapper.deleteById(id) > 0;
    }
    @Override
    public boolean deleteByResearchId(Long id) {
        return this.msgReminderTaskMapper.deleteByResearchId(id) > 0;
    }
}
