package com.dchealth.etl.service.record.impl;

import com.dchealth.etl.domain.record.DrugPrescMaster;
import com.dchealth.etl.dao.record.DrugPrescMasterMapper;
import com.dchealth.etl.service.record.DrugPrescMasterService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 药品处方主记录(DrugPrescMaster)表服务实现类
 *
 * @author makejava
 * @since 2023-04-24 17:53:50
 */
@Service("drugPrescMasterService")
public class DrugPrescMasterServiceImpl implements DrugPrescMasterService {
    @Resource
    private DrugPrescMasterMapper drugPrescMasterDao;

    /**
     * 通过ID查询单条数据
     *
     * @return 实例对象
     */
    @Override
    public DrugPrescMaster queryById( ) {
        return this.drugPrescMasterDao.queryById();
    }

    /**
     * 分页查询
     *
     * @param drugPrescMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<DrugPrescMaster> queryByPage(DrugPrescMaster drugPrescMaster, PageRequest pageRequest) {
        long total = this.drugPrescMasterDao.count(drugPrescMaster);
        return new PageImpl<>(this.drugPrescMasterDao.queryAllByLimit(drugPrescMaster, pageRequest), pageRequest, total);
    }

    @Override
    public int querySyncInfoCount(DrugPrescMaster drugPrescMaster) {
        return this.drugPrescMasterDao.querySyncInfoCount(drugPrescMaster);
    }

    @Override
    public Page<DrugPrescMaster> querySyncInfoByPage(DrugPrescMaster drugPrescMaster, PageRequest pageRequest, long total) {
        return new PageImpl<>(this.drugPrescMasterDao.querySyncInfoByPage(drugPrescMaster, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param drugPrescMaster 实例对象
     * @return 实例对象
     */
    @Override
    public DrugPrescMaster insert(DrugPrescMaster drugPrescMaster) {
        this.drugPrescMasterDao.insert(drugPrescMaster);
        return drugPrescMaster;
    }

    /**
     * 修改数据
     *
     * @param drugPrescMaster 实例对象
     * @return 实例对象
     */
    @Override
    public DrugPrescMaster update(DrugPrescMaster drugPrescMaster) {
        this.drugPrescMasterDao.update(drugPrescMaster);
        return this.queryById();
    }

    /**
     * 通过主键删除数据
     *
     * @return 是否成功
     */
    @Override
    public boolean deleteById( ) {
        return this.drugPrescMasterDao.deleteById() > 0;
    }
}
