package com.dchealth.cro.account.mapper;


import com.dchealth.cro.account.domain.MsgReminderTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 随访消息提醒配置表(MsgReminderTask)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-06 09:38:19
 */
@Repository
public interface MsgReminderTaskMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MsgReminderTask queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param msgReminderTask 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<MsgReminderTask> queryAllByLimit(MsgReminderTask msgReminderTask, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param msgReminderTask 查询条件
     * @return 总行数
     */
    long count(MsgReminderTask msgReminderTask);

    /**
     * 新增数据
     *
     * @param msgReminderTask 实例对象
     * @return 影响行数
     */
    int insert(MsgReminderTask msgReminderTask);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MsgReminderTask> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MsgReminderTask> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MsgReminderTask> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MsgReminderTask> entities);

    /**
     * 修改数据
     *
     * @param msgReminderTask 实例对象
     * @return 影响行数
     */
    int update(MsgReminderTask msgReminderTask);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

