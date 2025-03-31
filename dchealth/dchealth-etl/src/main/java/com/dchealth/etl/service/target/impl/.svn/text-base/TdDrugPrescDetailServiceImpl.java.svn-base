package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdDrugPrescDetail;
import com.dchealth.etl.dao.target.TdDrugPrescDetailMapper;
import com.dchealth.etl.service.target.TdDrugPrescDetailService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 药品处方明细记录(TdDrugPrescDetail)表服务实现类
 *
 * @author makejava
 * @since 2023-04-24 17:43:08
 */
@Service("tdDrugPrescDetailService")
public class TdDrugPrescDetailServiceImpl implements TdDrugPrescDetailService {
    @Resource
    private TdDrugPrescDetailMapper tdDrugPrescDetailDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdDrugPrescDetail queryById(Integer id) {
        return this.tdDrugPrescDetailDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdDrugPrescDetail 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdDrugPrescDetail> queryByPage(TdDrugPrescDetail tdDrugPrescDetail, PageRequest pageRequest) {
        long total = this.tdDrugPrescDetailDao.count(tdDrugPrescDetail);
        return new PageImpl<>(this.tdDrugPrescDetailDao.queryAllByLimit(tdDrugPrescDetail, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdDrugPrescDetail 实例对象
     * @return 实例对象
     */
    @Override
    public TdDrugPrescDetail insert(TdDrugPrescDetail tdDrugPrescDetail) {
        this.tdDrugPrescDetailDao.insert(tdDrugPrescDetail);
        return tdDrugPrescDetail;
    }

    @Override
    public int insertBatch(List<TdDrugPrescDetail> entities) {
        return tdDrugPrescDetailDao.insertBatch(entities);
    }

    /**
     * 修改数据
     *
     * @param tdDrugPrescDetail 实例对象
     * @return 实例对象
     */
    @Override
    public TdDrugPrescDetail update(TdDrugPrescDetail tdDrugPrescDetail) {
        this.tdDrugPrescDetailDao.update(tdDrugPrescDetail);
        return this.queryById(tdDrugPrescDetail.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdDrugPrescDetailDao.deleteById(id) > 0;
    }
}
