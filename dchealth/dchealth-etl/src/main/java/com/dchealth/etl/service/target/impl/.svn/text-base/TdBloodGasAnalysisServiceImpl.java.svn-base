package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdBloodGasAnalysis;
import com.dchealth.etl.dao.target.TdBloodGasAnalysisMapper;
import com.dchealth.etl.service.target.TdBloodGasAnalysisService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 血气分析(TdBloodGasAnalysis)表服务实现类
 *
 * @author makejava
 * @since 2023-04-25 18:01:13
 */
@Service("tdBloodGasAnalysisService")
public class TdBloodGasAnalysisServiceImpl implements TdBloodGasAnalysisService {
    @Resource
    private TdBloodGasAnalysisMapper tdBloodGasAnalysisDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdBloodGasAnalysis queryById(Integer id) {
        return this.tdBloodGasAnalysisDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdBloodGasAnalysis 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdBloodGasAnalysis> queryByPage(TdBloodGasAnalysis tdBloodGasAnalysis, PageRequest pageRequest) {
        long total = this.tdBloodGasAnalysisDao.count(tdBloodGasAnalysis);
        return new PageImpl<>(this.tdBloodGasAnalysisDao.queryAllByLimit(tdBloodGasAnalysis, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdBloodGasAnalysis 实例对象
     * @return 实例对象
     */
    @Override
    public TdBloodGasAnalysis insert(TdBloodGasAnalysis tdBloodGasAnalysis) {
        this.tdBloodGasAnalysisDao.insert(tdBloodGasAnalysis);
        return tdBloodGasAnalysis;
    }

    @Override
    public int insertBatch(List<TdBloodGasAnalysis> entities) {
        return this.tdBloodGasAnalysisDao.insertBatch(entities);
    }

    /**
     * 修改数据
     *
     * @param tdBloodGasAnalysis 实例对象
     * @return 实例对象
     */
    @Override
    public TdBloodGasAnalysis update(TdBloodGasAnalysis tdBloodGasAnalysis) {
        this.tdBloodGasAnalysisDao.update(tdBloodGasAnalysis);
        return this.queryById(tdBloodGasAnalysis.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdBloodGasAnalysisDao.deleteById(id) > 0;
    }
}
