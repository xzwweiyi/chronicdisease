package com.dchealth.etl.service.record.impl;

import com.dchealth.etl.domain.record.Diagnosis;
import com.dchealth.etl.dao.record.DiagnosisMapper;
import com.dchealth.etl.service.record.DiagnosisService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 诊断记录(Diagnosis)表服务实现类
 *
 * @author makejava
 * @since 2023-04-04 16:23:44
 */
@Service("diagnosisService")
public class DiagnosisServiceImpl implements DiagnosisService {
    @Resource
    private DiagnosisMapper diagnosisDao;

    /**
     * 分页查询
     *
     * @param diagnosis 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<Diagnosis> queryByPage(Diagnosis diagnosis, PageRequest pageRequest) {
        long total = this.diagnosisDao.count(diagnosis);
        return new PageImpl<>(this.diagnosisDao.queryAllByLimit(diagnosis, pageRequest), pageRequest, total);
    }

    @Override
    public int querySyncInfoCount(Diagnosis diagnosis) {
        return this.diagnosisDao.querySyncInfoCount(diagnosis);
    }

    @Override
    public Page<Diagnosis> querySyncInfoByPage(Diagnosis diagnosis, PageRequest pageRequest, long total) {
        return new PageImpl<>(this.diagnosisDao.querySyncInfoByPage(diagnosis, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param diagnosis 实例对象
     * @return 实例对象
     */
    @Override
    public Diagnosis insert(Diagnosis diagnosis) {
        this.diagnosisDao.insert(diagnosis);
        return diagnosis;
    }

    /**
     * 修改数据
     *
     * @param diagnosis 实例对象
     * @return 实例对象
     */
    @Override
    public Diagnosis update(Diagnosis diagnosis) {
        this.diagnosisDao.update(diagnosis);
        return diagnosis;
    }

}
