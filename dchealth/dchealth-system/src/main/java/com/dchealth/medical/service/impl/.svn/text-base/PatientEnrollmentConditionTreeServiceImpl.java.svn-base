package com.dchealth.medical.service.impl;

import com.dchealth.medical.domain.PatientEnrollmentConditionTree;
import com.dchealth.medical.mapper.PatientEnrollmentConditionTreeMapper;
import com.dchealth.medical.service.PatientEnrollmentConditionTreeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 那排条件检索树(PatientEnrollmentConditionTree)表服务实现类
 *
 * @author makejava
 * @since 2023-05-05 15:17:56
 */
@Service("patientEnrollmentConditionTreeService")
public class PatientEnrollmentConditionTreeServiceImpl implements PatientEnrollmentConditionTreeService {
    @Resource
    private PatientEnrollmentConditionTreeMapper patientEnrollmentConditionTreeDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public PatientEnrollmentConditionTree queryById(Integer id) {
        return this.patientEnrollmentConditionTreeDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param patientEnrollmentConditionTree 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<PatientEnrollmentConditionTree> queryByPage(PatientEnrollmentConditionTree patientEnrollmentConditionTree, PageRequest pageRequest) {
        long total = this.patientEnrollmentConditionTreeDao.count(patientEnrollmentConditionTree);
        return new PageImpl<>(this.patientEnrollmentConditionTreeDao.queryAllByLimit(patientEnrollmentConditionTree, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param patientEnrollmentConditionTree 实例对象
     * @return 实例对象
     */
    @Override
    public PatientEnrollmentConditionTree insert(PatientEnrollmentConditionTree patientEnrollmentConditionTree) {
        this.patientEnrollmentConditionTreeDao.insert(patientEnrollmentConditionTree);
        return patientEnrollmentConditionTree;
    }

    /**
     * 修改数据
     *
     * @param patientEnrollmentConditionTree 实例对象
     * @return 实例对象
     */
    @Override
    public PatientEnrollmentConditionTree update(PatientEnrollmentConditionTree patientEnrollmentConditionTree) {
        this.patientEnrollmentConditionTreeDao.update(patientEnrollmentConditionTree);
        return this.queryById(patientEnrollmentConditionTree.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.patientEnrollmentConditionTreeDao.deleteById(id) > 0;
    }
}
