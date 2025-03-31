package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdAuxiliaryExaminationCt;
import com.dchealth.etl.dao.target.TdAuxiliaryExaminationCtMapper;
import com.dchealth.etl.service.target.TdAuxiliaryExaminationCtService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 唐都-辅助检查-ct项目相关表(TdAuxiliaryExaminationCt)表服务实现类
 *
 * @author makejava
 * @since 2023-03-29 16:14:32
 */
@Service("tdAuxiliaryExaminationCtService")
public class TdAuxiliaryExaminationCtServiceImpl implements TdAuxiliaryExaminationCtService {
    @Resource
    private TdAuxiliaryExaminationCtMapper tdAuxiliaryExaminationCtDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdAuxiliaryExaminationCt queryById(Integer id) {
        return this.tdAuxiliaryExaminationCtDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdAuxiliaryExaminationCt 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdAuxiliaryExaminationCt> queryByPage(TdAuxiliaryExaminationCt tdAuxiliaryExaminationCt, PageRequest pageRequest) {
        long total = this.tdAuxiliaryExaminationCtDao.count(tdAuxiliaryExaminationCt);
        return new PageImpl<>(this.tdAuxiliaryExaminationCtDao.queryAllByLimit(tdAuxiliaryExaminationCt, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdAuxiliaryExaminationCt 实例对象
     * @return 实例对象
     */
    @Override
    public TdAuxiliaryExaminationCt insert(TdAuxiliaryExaminationCt tdAuxiliaryExaminationCt) {
        this.tdAuxiliaryExaminationCtDao.insert(tdAuxiliaryExaminationCt);
        return tdAuxiliaryExaminationCt;
    }

    @Override
    public int insertBatch(List<TdAuxiliaryExaminationCt> tdAuxiliaryExaminationCts) {
        return this.tdAuxiliaryExaminationCtDao.insertBatch(tdAuxiliaryExaminationCts);
    }
    @Override
    public int insertBatchByMaps(List<Map> entities) {
        return this.tdAuxiliaryExaminationCtDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdAuxiliaryExaminationCt 实例对象
     * @return 实例对象
     */
    @Override
    public TdAuxiliaryExaminationCt update(TdAuxiliaryExaminationCt tdAuxiliaryExaminationCt) {
        this.tdAuxiliaryExaminationCtDao.update(tdAuxiliaryExaminationCt);
        return this.queryById(tdAuxiliaryExaminationCt.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdAuxiliaryExaminationCtDao.deleteById(id) > 0;
    }
}
