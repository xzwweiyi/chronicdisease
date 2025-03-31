package com.dchealth.msgTip.service.impl;


import com.dchealth.msgTip.domain.MsgReminderStats;
import com.dchealth.msgTip.mapper.MsgReminderStatsMapper;
import com.dchealth.msgTip.service.MsgReminderStatsService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 消息提醒统计表(MsgReminderStats)表服务实现类
 *
 * @author makejava
 * @since 2023-06-06 17:46:27
 */
@Service("msgReminderStatsService")
public class MsgReminderStatsServiceImpl implements MsgReminderStatsService {
    @Resource
    private MsgReminderStatsMapper msgReminderStatsMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public MsgReminderStats queryById(Long id) {
        return this.msgReminderStatsMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param msgReminderStats 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<MsgReminderStats> queryByPage(MsgReminderStats msgReminderStats, PageRequest pageRequest) {
        long total = this.msgReminderStatsMapper.count(msgReminderStats);
        return new PageImpl<>(this.msgReminderStatsMapper.queryAllByLimit(msgReminderStats, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param msgReminderStats 实例对象
     * @return 实例对象
     */
    @Override
    public MsgReminderStats insert(MsgReminderStats msgReminderStats) {
        this.msgReminderStatsMapper.insert(msgReminderStats);
        return msgReminderStats;
    }

    /**
     * 修改数据
     *
     * @param msgReminderStats 实例对象
     * @return 实例对象
     */
    @Override
    public MsgReminderStats update(MsgReminderStats msgReminderStats) {
        this.msgReminderStatsMapper.update(msgReminderStats);
        return this.queryById(msgReminderStats.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.msgReminderStatsMapper.deleteById(id) > 0;
    }
}
