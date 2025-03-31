package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdAuxiliaryExaminationUltrasound;
import com.dchealth.etl.dao.target.TdAuxiliaryExaminationUltrasoundMapper;
import com.dchealth.etl.service.target.TdAuxiliaryExaminationUltrasoundService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 唐都-辅助检查-超声项目相关表(TdAuxiliaryExaminationUltrasound)表服务实现类
 *
 * @author makejava
 * @since 2023-03-29 16:20:37
 */
@Service("tdAuxiliaryExaminationUltrasoundService")
public class TdAuxiliaryExaminationUltrasoundServiceImpl implements TdAuxiliaryExaminationUltrasoundService {
    @Resource
    private TdAuxiliaryExaminationUltrasoundMapper tdAuxiliaryExaminationUltrasoundDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdAuxiliaryExaminationUltrasound queryById(Integer id) {
        return this.tdAuxiliaryExaminationUltrasoundDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdAuxiliaryExaminationUltrasound 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdAuxiliaryExaminationUltrasound> queryByPage(TdAuxiliaryExaminationUltrasound tdAuxiliaryExaminationUltrasound, PageRequest pageRequest) {
        long total = this.tdAuxiliaryExaminationUltrasoundDao.count(tdAuxiliaryExaminationUltrasound);
        return new PageImpl<>(this.tdAuxiliaryExaminationUltrasoundDao.queryAllByLimit(tdAuxiliaryExaminationUltrasound, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdAuxiliaryExaminationUltrasound 实例对象
     * @return 实例对象
     */
    @Override
    public TdAuxiliaryExaminationUltrasound insert(TdAuxiliaryExaminationUltrasound tdAuxiliaryExaminationUltrasound) {
        this.tdAuxiliaryExaminationUltrasoundDao.insert(tdAuxiliaryExaminationUltrasound);
        return tdAuxiliaryExaminationUltrasound;
    }

    @Override
    public int insertBatch(List<TdAuxiliaryExaminationUltrasound> entities) {
        return this.tdAuxiliaryExaminationUltrasoundDao.insertBatch(entities);
    }

    /**
     * 修改数据
     *
     * @param tdAuxiliaryExaminationUltrasound 实例对象
     * @return 实例对象
     */
    @Override
    public TdAuxiliaryExaminationUltrasound update(TdAuxiliaryExaminationUltrasound tdAuxiliaryExaminationUltrasound) {
        this.tdAuxiliaryExaminationUltrasoundDao.update(tdAuxiliaryExaminationUltrasound);
        return this.queryById(tdAuxiliaryExaminationUltrasound.getId());
    }
    @Override
    public int insertBatchByMaps(List<Map> entities) {
        return this.tdAuxiliaryExaminationUltrasoundDao.insertBatchByMaps(entities);
    }
    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdAuxiliaryExaminationUltrasoundDao.deleteById(id) > 0;
    }
}
