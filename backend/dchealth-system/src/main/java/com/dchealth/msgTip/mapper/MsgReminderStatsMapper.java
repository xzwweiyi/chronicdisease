package com.dchealth.msgTip.mapper;


import com.dchealth.msgTip.domain.MsgReminderStats;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 消息提醒统计表(MsgReminderStats)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-06 17:46:21
 */
public interface MsgReminderStatsMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MsgReminderStats queryById(Long id);
    /**
     * 通过用户ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MsgReminderStats queryByUserId(Long userId);
    MsgReminderStats queryByUserIdAndRecordId(Long userId,Long recordId);

    /**
     * 查询指定行数据
     *
     * @param msgReminderStats 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<MsgReminderStats> queryAllByLimit(MsgReminderStats msgReminderStats, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param msgReminderStats 查询条件
     * @return 总行数
     */
    long count(MsgReminderStats msgReminderStats);

    /**
     * 新增数据
     *
     * @param msgReminderStats 实例对象
     * @return 影响行数
     */
    int insert(MsgReminderStats msgReminderStats);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MsgReminderStats> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MsgReminderStats> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MsgReminderStats> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MsgReminderStats> entities);

    /**
     * 修改数据
     *
     * @param msgReminderStats 实例对象
     * @return 影响行数
     */
    int update(MsgReminderStats msgReminderStats);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

