package com.dchealth.medical.service;

import com.dchealth.medical.domain.PatientEnrollmentConditionTree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 那排条件检索树(PatientEnrollmentConditionTree)表服务接口
 *
 * @author makejava
 * @since 2023-05-05 15:17:56
 */
public interface PatientEnrollmentConditionTreeService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    PatientEnrollmentConditionTree queryById(Integer id);

    /**
     * 分页查询
     *
     * @param patientEnrollmentConditionTree 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<PatientEnrollmentConditionTree> queryByPage(PatientEnrollmentConditionTree patientEnrollmentConditionTree, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param patientEnrollmentConditionTree 实例对象
     * @return 实例对象
     */
    PatientEnrollmentConditionTree insert(PatientEnrollmentConditionTree patientEnrollmentConditionTree);

    /**
     * 修改数据
     *
     * @param patientEnrollmentConditionTree 实例对象
     * @return 实例对象
     */
    PatientEnrollmentConditionTree update(PatientEnrollmentConditionTree patientEnrollmentConditionTree);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
