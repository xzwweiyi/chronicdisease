package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdAuxiliaryExaminationOther;
import com.dchealth.etl.dao.target.TdAuxiliaryExaminationOtherMapper;
import com.dchealth.etl.service.target.TdAuxiliaryExaminationOtherService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 唐都-辅助检查-心电图等其他项目表(TdAuxiliaryExaminationOther)表服务实现类
 *
 * @author makejava
 * @since 2023-03-29 16:20:11
 */
@Service("tdAuxiliaryExaminationOtherService")
public class TdAuxiliaryExaminationOtherServiceImpl implements TdAuxiliaryExaminationOtherService {
    @Resource
    private TdAuxiliaryExaminationOtherMapper tdAuxiliaryExaminationOtherDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdAuxiliaryExaminationOther queryById(Integer id) {
        return this.tdAuxiliaryExaminationOtherDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdAuxiliaryExaminationOther 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdAuxiliaryExaminationOther> queryByPage(TdAuxiliaryExaminationOther tdAuxiliaryExaminationOther, PageRequest pageRequest) {
        long total = this.tdAuxiliaryExaminationOtherDao.count(tdAuxiliaryExaminationOther);
        return new PageImpl<>(this.tdAuxiliaryExaminationOtherDao.queryAllByLimit(tdAuxiliaryExaminationOther, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdAuxiliaryExaminationOther 实例对象
     * @return 实例对象
     */
    @Override
    public TdAuxiliaryExaminationOther insert(TdAuxiliaryExaminationOther tdAuxiliaryExaminationOther) {
        this.tdAuxiliaryExaminationOtherDao.insert(tdAuxiliaryExaminationOther);
        return tdAuxiliaryExaminationOther;
    }

    @Override
    public int insertBatch(List<TdAuxiliaryExaminationOther> tdAuxiliaryExaminationOthers) {
        return this.tdAuxiliaryExaminationOtherDao.insertBatch(tdAuxiliaryExaminationOthers);
    }

    @Override
    public int insertBatchByMaps(List<Map> entities) {
        return this.tdAuxiliaryExaminationOtherDao.insertBatchByMaps(entities);
    }

    /**
     * 修改数据
     *
     * @param tdAuxiliaryExaminationOther 实例对象
     * @return 实例对象
     */
    @Override
    public TdAuxiliaryExaminationOther update(TdAuxiliaryExaminationOther tdAuxiliaryExaminationOther) {
        this.tdAuxiliaryExaminationOtherDao.update(tdAuxiliaryExaminationOther);
        return this.queryById(tdAuxiliaryExaminationOther.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdAuxiliaryExaminationOtherDao.deleteById(id) > 0;
    }
}
