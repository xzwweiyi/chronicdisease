package com.dchealth.msgTip.mapper;


import com.dchealth.msgTip.domain.MsgReminderTaskTemplate;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 随访消息提醒模板(MsgReminderTaskTemplate)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-06 10:05:21
 */
public interface MsgReminderTaskTemplateMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MsgReminderTaskTemplate queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param msgReminderTaskTemplate 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<MsgReminderTaskTemplate> queryAllByLimit(MsgReminderTaskTemplate msgReminderTaskTemplate, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param msgReminderTaskTemplate 查询条件
     * @return 总行数
     */
    long count(MsgReminderTaskTemplate msgReminderTaskTemplate);

    /**
     * 新增数据
     *
     * @param msgReminderTaskTemplate 实例对象
     * @return 影响行数
     */
    int insert(MsgReminderTaskTemplate msgReminderTaskTemplate);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MsgReminderTaskTemplate> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MsgReminderTaskTemplate> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MsgReminderTaskTemplate> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MsgReminderTaskTemplate> entities);

    /**
     * 修改数据
     *
     * @param msgReminderTaskTemplate 实例对象
     * @return 影响行数
     */
    int update(MsgReminderTaskTemplate msgReminderTaskTemplate);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

