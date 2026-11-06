package com.dchealth.msgTip.mapper;

import com.dchealth.msgTip.domain.SysMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 消息表-包括push，消息，提醒等数据(SysMessage)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-11 13:50:24
 */
public interface SysMessageMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysMessage queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param sysMessage 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<SysMessage> queryAllByLimit(SysMessage sysMessage, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param sysMessage 查询条件
     * @return 总行数
     */
    long count(SysMessage sysMessage);

    /**
     * 新增数据
     *
     * @param sysMessage 实例对象
     * @return 影响行数
     */
    int insert(SysMessage sysMessage);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysMessage> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysMessage> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysMessage> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysMessage> entities);

    /**
     * 修改数据
     *
     * @param sysMessage 实例对象
     * @return 影响行数
     */
    int update(SysMessage sysMessage);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 生成质疑未处理提醒
     * @param offSet
     */
    int saveQuestionMsg(@Param("offSet")Long offSet,@Param("sysMessage") SysMessage sysMessage);
    /**
     * 生成预警未处理提醒
     * @param offSet
     */
    int saveWarnMsg(@Param("offSet")Long offSet,@Param("sysMessage") SysMessage sysMessage);
    /**
     * 生成随访未提交提醒
     * @param offSet
     */
    int saveFollowMsg(@Param("offSet")Long offSet,@Param("sysMessage") SysMessage sysMessage);

}

