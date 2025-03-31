package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdSurgicalExam;
import com.dchealth.etl.dao.target.TdSurgicalExamMapper;
import com.dchealth.etl.service.target.TdSurgicalExamService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 外科检查表(TdSurgicalExam)表服务实现类
 *
 * @author makejava
 * @since 2023-04-07 14:31:50
 */
@Service("tdSurgicalExamService")
public class TdSurgicalExamServiceImpl implements TdSurgicalExamService {
    @Resource
    private TdSurgicalExamMapper tdSurgicalExamDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdSurgicalExam queryById(Integer id) {
        return this.tdSurgicalExamDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdSurgicalExam 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdSurgicalExam> queryByPage(TdSurgicalExam tdSurgicalExam, PageRequest pageRequest) {
        long total = this.tdSurgicalExamDao.count(tdSurgicalExam);
        return new PageImpl<>(this.tdSurgicalExamDao.queryAllByLimit(tdSurgicalExam, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdSurgicalExam 实例对象
     * @return 实例对象
     */
    @Override
    public TdSurgicalExam insert(TdSurgicalExam tdSurgicalExam) {
        this.tdSurgicalExamDao.insert(tdSurgicalExam);
        return tdSurgicalExam;
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdSurgicalExamDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdSurgicalExam 实例对象
     * @return 实例对象
     */
    @Override
    public TdSurgicalExam update(TdSurgicalExam tdSurgicalExam) {
        this.tdSurgicalExamDao.update(tdSurgicalExam);
        return this.queryById(tdSurgicalExam.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdSurgicalExamDao.deleteById(id) > 0;
    }
}
