package com.dchealth.medical.mapper;

import com.dchealth.medical.domain.PatientEnrollmentConditionTree;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 那排条件检索树(PatientEnrollmentConditionTree)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-05 15:17:56
 */
@Component
public interface PatientEnrollmentConditionTreeMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    PatientEnrollmentConditionTree queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param patientEnrollmentConditionTree 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<PatientEnrollmentConditionTree> queryAllByLimit(PatientEnrollmentConditionTree patientEnrollmentConditionTree, @Param("pageable") Pageable pageable);/**
     * 查询指定行数据
     *
     * @param patientEnrollmentConditionTree 查询条件
     * @return 对象列表
     */
    List<PatientEnrollmentConditionTree> queryAll(PatientEnrollmentConditionTree patientEnrollmentConditionTree);


    /**
     * 统计总行数
     *
     * @param patientEnrollmentConditionTree 查询条件
     * @return 总行数
     */
    long count(PatientEnrollmentConditionTree patientEnrollmentConditionTree);

    /**
     * 新增数据
     *
     * @param patientEnrollmentConditionTree 实例对象
     * @return 影响行数
     */
    int insert(PatientEnrollmentConditionTree patientEnrollmentConditionTree);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<PatientEnrollmentConditionTree> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<PatientEnrollmentConditionTree> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<PatientEnrollmentConditionTree> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<PatientEnrollmentConditionTree> entities);

    /**
     * 修改数据
     *
     * @param patientEnrollmentConditionTree 实例对象
     * @return 影响行数
     */
    int update(PatientEnrollmentConditionTree patientEnrollmentConditionTree);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

