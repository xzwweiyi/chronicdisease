package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdOphthalmologicalExam;
import com.dchealth.etl.dao.target.TdOphthalmologicalExamMapper;
import com.dchealth.etl.service.target.TdOphthalmologicalExamService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 眼科检查表(TdOphthalmologicalExam)表服务实现类
 *
 * @author makejava
 * @since 2023-04-07 14:55:07
 */
@Service("tdOphthalmologicalExamService")
public class TdOphthalmologicalExamServiceImpl implements TdOphthalmologicalExamService {
    @Resource
    private TdOphthalmologicalExamMapper tdOphthalmologicalExamDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdOphthalmologicalExam queryById(Integer id) {
        return this.tdOphthalmologicalExamDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdOphthalmologicalExam 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdOphthalmologicalExam> queryByPage(TdOphthalmologicalExam tdOphthalmologicalExam, PageRequest pageRequest) {
        long total = this.tdOphthalmologicalExamDao.count(tdOphthalmologicalExam);
        return new PageImpl<>(this.tdOphthalmologicalExamDao.queryAllByLimit(tdOphthalmologicalExam, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdOphthalmologicalExam 实例对象
     * @return 实例对象
     */
    @Override
    public TdOphthalmologicalExam insert(TdOphthalmologicalExam tdOphthalmologicalExam) {
        this.tdOphthalmologicalExamDao.insert(tdOphthalmologicalExam);
        return tdOphthalmologicalExam;
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdOphthalmologicalExamDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdOphthalmologicalExam 实例对象
     * @return 实例对象
     */
    @Override
    public TdOphthalmologicalExam update(TdOphthalmologicalExam tdOphthalmologicalExam) {
        this.tdOphthalmologicalExamDao.update(tdOphthalmologicalExam);
        return this.queryById(tdOphthalmologicalExam.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdOphthalmologicalExamDao.deleteById(id) > 0;
    }
}
