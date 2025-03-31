package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdAuxiliaryExaminationNmr;
import com.dchealth.etl.dao.target.TdAuxiliaryExaminationNmrMapper;
import com.dchealth.etl.service.target.TdAuxiliaryExaminationNmrService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 唐都-辅助检查-核磁项目相关表(TdAuxiliaryExaminationNmr)表服务实现类
 *
 * @author makejava
 * @since 2023-03-29 16:16:56
 */
@Service("tdAuxiliaryExaminationNmrService")
public class TdAuxiliaryExaminationNmrServiceImpl implements TdAuxiliaryExaminationNmrService {
    @Resource
    private TdAuxiliaryExaminationNmrMapper tdAuxiliaryExaminationNmrDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdAuxiliaryExaminationNmr queryById(Integer id) {
        return this.tdAuxiliaryExaminationNmrDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdAuxiliaryExaminationNmr 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdAuxiliaryExaminationNmr> queryByPage(TdAuxiliaryExaminationNmr tdAuxiliaryExaminationNmr, PageRequest pageRequest) {
        long total = this.tdAuxiliaryExaminationNmrDao.count(tdAuxiliaryExaminationNmr);
        return new PageImpl<>(this.tdAuxiliaryExaminationNmrDao.queryAllByLimit(tdAuxiliaryExaminationNmr, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdAuxiliaryExaminationNmr 实例对象
     * @return 实例对象
     */
    @Override
    public TdAuxiliaryExaminationNmr insert(TdAuxiliaryExaminationNmr tdAuxiliaryExaminationNmr) {
        this.tdAuxiliaryExaminationNmrDao.insert(tdAuxiliaryExaminationNmr);
        return tdAuxiliaryExaminationNmr;
    }

    @Override
    public int insertBatch(List<TdAuxiliaryExaminationNmr> tdAuxiliaryExaminationNmrs) {
        return this.tdAuxiliaryExaminationNmrDao.insertBatch(tdAuxiliaryExaminationNmrs);
    }
    @Override
    public int insertBatchByMaps(List<Map> entities) {
        return this.tdAuxiliaryExaminationNmrDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdAuxiliaryExaminationNmr 实例对象
     * @return 实例对象
     */
    @Override
    public TdAuxiliaryExaminationNmr update(TdAuxiliaryExaminationNmr tdAuxiliaryExaminationNmr) {
        this.tdAuxiliaryExaminationNmrDao.update(tdAuxiliaryExaminationNmr);
        return this.queryById(tdAuxiliaryExaminationNmr.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdAuxiliaryExaminationNmrDao.deleteById(id) > 0;
    }
}
