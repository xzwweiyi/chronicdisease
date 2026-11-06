package com.dchealth.etl.service.record.impl;

import com.dchealth.etl.domain.record.DrugPrescDetail;
import com.dchealth.etl.dao.record.DrugPrescDetailMapper;
import com.dchealth.etl.service.record.DrugPrescDetailService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (DrugPrescDetail)表服务实现类
 *
 * @author makejava
 * @since 2023-04-24 17:54:22
 */
@Service("drugPrescDetailService")
public class DrugPrescDetailServiceImpl implements DrugPrescDetailService {
    @Resource
    private DrugPrescDetailMapper drugPrescDetailDao;

    /**
     * 通过ID查询单条数据
     *
     * @return 实例对象
     */
    @Override
    public DrugPrescDetail queryById( ) {
        return this.drugPrescDetailDao.queryById();
    }

    /**
     * 分页查询
     *
     * @param drugPrescDetail 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<DrugPrescDetail> queryByPage(DrugPrescDetail drugPrescDetail, PageRequest pageRequest) {
        long total = this.drugPrescDetailDao.count(drugPrescDetail);
        return new PageImpl<>(this.drugPrescDetailDao.queryAllByLimit(drugPrescDetail, pageRequest), pageRequest, total);
    }

    @Override
    public int querySyncInfoCount(DrugPrescDetail drugPrescDetail) {
        return this.drugPrescDetailDao.querySyncInfoCount(drugPrescDetail);
    }

    @Override
    public Page<DrugPrescDetail> querySyncInfoByPage(DrugPrescDetail drugPrescDetail, PageRequest pageRequest, long total) {
        return new PageImpl<>(this.drugPrescDetailDao.querySyncInfoByPage(drugPrescDetail, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param drugPrescDetail 实例对象
     * @return 实例对象
     */
    @Override
    public DrugPrescDetail insert(DrugPrescDetail drugPrescDetail) {
        this.drugPrescDetailDao.insert(drugPrescDetail);
        return drugPrescDetail;
    }

    /**
     * 修改数据
     *
     * @param drugPrescDetail 实例对象
     * @return 实例对象
     */
    @Override
    public DrugPrescDetail update(DrugPrescDetail drugPrescDetail) {
        this.drugPrescDetailDao.update(drugPrescDetail);
        return this.queryById();
    }

    /**
     * 通过主键删除数据
     *
     * @return 是否成功
     */
    @Override
    public boolean deleteById( ) {
        return this.drugPrescDetailDao.deleteById() > 0;
    }
}
