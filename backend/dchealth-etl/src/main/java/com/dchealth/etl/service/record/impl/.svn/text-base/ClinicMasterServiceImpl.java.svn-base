package com.dchealth.etl.service.record.impl;

import com.dchealth.etl.domain.record.ClinicMaster;
import com.dchealth.etl.dao.record.ClinicMasterMapper;
import com.dchealth.etl.domain.record.Orders;
import com.dchealth.etl.service.record.ClinicMasterService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 就诊记录(ClinicMaster)表服务实现类
 *
 * @author makejava
 * @since 2023-05-18 15:34:47
 */
@Service("clinicMasterService")
public class ClinicMasterServiceImpl implements ClinicMasterService {
    @Resource
    private ClinicMasterMapper clinicMasterDao;


    /**
     * 分页查询
     *
     * @param clinicMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ClinicMaster> queryByPage(ClinicMaster clinicMaster, PageRequest pageRequest) {
        long total = this.clinicMasterDao.count(clinicMaster);
        return new PageImpl<>(this.clinicMasterDao.queryAllByLimit(clinicMaster, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param clinicMaster 实例对象
     * @return 实例对象
     */
    @Override
    public ClinicMaster insert(ClinicMaster clinicMaster) {
        this.clinicMasterDao.insert(clinicMaster);
        return clinicMaster;
    }

    /**
     * 修改数据
     *
     * @param clinicMaster 实例对象
     * @return 实例对象
     */
    @Override
    public ClinicMaster update(ClinicMaster clinicMaster) {
        this.clinicMasterDao.update(clinicMaster);
        return clinicMaster;
    }

    public ClinicMasterServiceImpl() {
        super();
    }

    @Override
    public int querySyncInfoCount(ClinicMaster clinicMaster) {
        return 0;
    }

    @Override
    public Page<ClinicMaster> querySyncInfoByPage(ClinicMaster clinicMaster, PageRequest pageRequest, long total) {
        return null;
    }
}
