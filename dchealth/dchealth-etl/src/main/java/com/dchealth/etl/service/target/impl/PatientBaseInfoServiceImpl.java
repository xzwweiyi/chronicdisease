package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.PatientBaseInfo;
import com.dchealth.etl.dao.target.PatientBaseInfoMapper;
import com.dchealth.etl.service.target.PatientBaseInfoService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 患者基础表(PatientBaseInfo)表服务实现类
 *
 * @author makejava
 * @since 2023-03-27 15:03:36
 */
@Service("patientBaseInfoService")
public class PatientBaseInfoServiceImpl implements PatientBaseInfoService {
    @Resource
    private PatientBaseInfoMapper patientBaseInfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public PatientBaseInfo queryById(Long id) {
        return this.patientBaseInfoDao.queryById(id);
    }

    @Override
    public PatientBaseInfo queryByIdCard(String idCard) {
        return this.patientBaseInfoDao.queryByIdCard(idCard);
    }

    /**
     * 分页查询
     *
     * @param patientBaseInfo 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<PatientBaseInfo> queryByPage(PatientBaseInfo patientBaseInfo, PageRequest pageRequest) {
        long total = this.patientBaseInfoDao.count(patientBaseInfo);
        return new PageImpl<>(this.patientBaseInfoDao.queryAllByLimit(patientBaseInfo, pageRequest), pageRequest, total);
    }

    @Override
    public List<PatientBaseInfo> queryByPage(PatientBaseInfo patientBaseInfo){
        return this.patientBaseInfoDao.queryByPage(patientBaseInfo);
    }


    /**
     * 新增数据
     *
     * @param patientBaseInfo 实例对象
     * @return 实例对象
     */
    @Override
    public PatientBaseInfo insert(PatientBaseInfo patientBaseInfo) {
        this.patientBaseInfoDao.insert(patientBaseInfo);
        return patientBaseInfo;
    }

    @Override
    public int insertBatch(List<PatientBaseInfo> entities) {
        return this.patientBaseInfoDao.insertBatch(entities);
    }

    /**
     * 修改数据
     *
     * @param patientBaseInfo 实例对象
     * @return 实例对象
     */
    @Override
    public PatientBaseInfo update(PatientBaseInfo patientBaseInfo) {
        this.patientBaseInfoDao.update(patientBaseInfo);
        return this.queryById(patientBaseInfo.getId());
    }

    @Override
    public int updateBatch(List<PatientBaseInfo> list) {
        return this.patientBaseInfoDao.updateBatch(list);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.patientBaseInfoDao.deleteById(id) > 0;
    }
}
