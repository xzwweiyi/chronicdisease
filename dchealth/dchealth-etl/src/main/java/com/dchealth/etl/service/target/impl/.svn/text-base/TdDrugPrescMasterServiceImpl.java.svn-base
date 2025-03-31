package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdDrugPrescMaster;
import com.dchealth.etl.dao.target.TdDrugPrescMasterMapper;
import com.dchealth.etl.service.target.TdDrugPrescMasterService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 药品处方主记录(TdDrugPrescMaster)表服务实现类
 *
 * @author makejava
 * @since 2023-04-24 17:42:39
 */
@Service("tdDrugPrescMasterService")
public class TdDrugPrescMasterServiceImpl implements TdDrugPrescMasterService {
    @Resource
    private TdDrugPrescMasterMapper tdDrugPrescMasterDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdDrugPrescMaster queryById(Integer id) {
        return this.tdDrugPrescMasterDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdDrugPrescMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdDrugPrescMaster> queryByPage(TdDrugPrescMaster tdDrugPrescMaster, PageRequest pageRequest) {
        long total = this.tdDrugPrescMasterDao.count(tdDrugPrescMaster);
        return new PageImpl<>(this.tdDrugPrescMasterDao.queryAllByLimit(tdDrugPrescMaster, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdDrugPrescMaster 实例对象
     * @return 实例对象
     */
    @Override
    public TdDrugPrescMaster insert(TdDrugPrescMaster tdDrugPrescMaster) {
        this.tdDrugPrescMasterDao.insert(tdDrugPrescMaster);
        return tdDrugPrescMaster;
    }

    @Override
    public int insertBatch(List<TdDrugPrescMaster> entities) {
        return tdDrugPrescMasterDao.insertBatch(entities);
    }

    /**
     * 修改数据
     *
     * @param tdDrugPrescMaster 实例对象
     * @return 实例对象
     */
    @Override
    public TdDrugPrescMaster update(TdDrugPrescMaster tdDrugPrescMaster) {
        this.tdDrugPrescMasterDao.update(tdDrugPrescMaster);
        return tdDrugPrescMaster;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdDrugPrescMasterDao.deleteById(id) > 0;
    }
}
