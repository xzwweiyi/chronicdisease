package com.dchealth.msgTip.service;


import com.dchealth.msgTip.domain.MsgReminderStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 消息提醒统计表(MsgReminderStats)表服务接口
 *
 * @author makejava
 * @since 2023-06-06 17:46:25
 */
public interface MsgReminderStatsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MsgReminderStats queryById(Long id);

    /**
     * 分页查询
     *
     * @param msgReminderStats 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<MsgReminderStats> queryByPage(MsgReminderStats msgReminderStats, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param msgReminderStats 实例对象
     * @return 实例对象
     */
    MsgReminderStats insert(MsgReminderStats msgReminderStats);

    /**
     * 修改数据
     *
     * @param msgReminderStats 实例对象
     * @return 实例对象
     */
    MsgReminderStats update(MsgReminderStats msgReminderStats);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
