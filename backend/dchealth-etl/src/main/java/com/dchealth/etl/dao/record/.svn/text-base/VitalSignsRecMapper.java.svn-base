package com.dchealth.etl.dao.record;

import com.dchealth.etl.domain.record.VitalSignsRec;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 病人体症记录(VitalSignsRec)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-27 13:58:50
 */
public interface VitalSignsRecMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    VitalSignsRec queryById(String patientId);

    /**
     * 查询指定行数据
     *
     * @param vitalSignsRec 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<VitalSignsRec> queryAllByLimit(VitalSignsRec vitalSignsRec, @Param("pageable") Pageable pageable);
    /**
     * 查询指定行数据
     *
     * @param vitalSignsRec 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<VitalSignsRec> querySyncInfoByPage(@Param("vitalSignsRec") VitalSignsRec vitalSignsRec, @Param("pageable") Pageable pageable);
    /**
     * 统计总行数
     *
     * @param vitalSignsRec 查询条件
     * @return 总行数
     */
    long count(VitalSignsRec vitalSignsRec);
    /**
     * 统计总行数
     *
     * @param vitalSignsRec 查询条件
     * @return 总行数
     */
    long querySyncInfoCount(VitalSignsRec vitalSignsRec);
    /**
     * 新增数据
     *
     * @param vitalSignsRec 实例对象
     * @return 影响行数
     */
    int insert(VitalSignsRec vitalSignsRec);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<VitalSignsRec> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<VitalSignsRec> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<VitalSignsRec> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<VitalSignsRec> entities);

    /**
     * 修改数据
     *
     * @param vitalSignsRec 实例对象
     * @return 影响行数
     */
    int update(VitalSignsRec vitalSignsRec);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 影响行数
     */
    int deleteById(String patientId);

}

