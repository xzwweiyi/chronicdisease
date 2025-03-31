package com.dchealth.etl.dao.record;

import com.dchealth.etl.domain.record.LabTestMaster;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 检验主记录表
注释：此表描述每张检验申请单。一个检验申请与一个标本必须一一对应。此表记录由开单程序生成（病房或门诊或检验科室），由检验科室修改。
以1000张床位计，每天全院的检验申请约为2000件。每月的数据增长量约为6万条。
主键：申请序号(LabTestMaster)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-30 11:34:06
 */
public interface LabTestMasterMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param testNo 主键
     * @return 实例对象
     */
    LabTestMaster queryById(String testNo);

    /**
     * 查询指定行数据
     *
     * @param labTestMaster 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<LabTestMaster> queryAllByLimit(LabTestMaster labTestMaster, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param labTestMaster 查询条件
     * @return 总行数
     */
    long count(LabTestMaster labTestMaster);

    /**
     * 查询指定行数据
     *
     * @param labTestMaster 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<LabTestMaster> querySyncInfoByPage(@Param("labTestMaster") LabTestMaster labTestMaster, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param labTestMaster 查询条件
     * @return 总行数
     */
    int querySyncInfoCount(LabTestMaster labTestMaster);
    /**
     * 新增数据
     *
     * @param labTestMaster 实例对象
     * @return 影响行数
     */
    int insert(LabTestMaster labTestMaster);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<LabTestMaster> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<LabTestMaster> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<LabTestMaster> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<LabTestMaster> entities);

    /**
     * 修改数据
     *
     * @param labTestMaster 实例对象
     * @return 影响行数
     */
    int update(LabTestMaster labTestMaster);

    /**
     * 通过主键删除数据
     *
     * @param testNo 主键
     * @return 影响行数
     */
    int deleteById(String testNo);

}

