package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdInternalMedicineExam;
import com.dchealth.etl.dao.target.TdInternalMedicineExamMapper;
import com.dchealth.etl.service.target.TdInternalMedicineExamService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 内科检查表(TdInternalMedicineExam)表服务实现类
 *
 * @author makejava
 * @since 2023-04-07 14:08:44
 */
@Service("tdInternalMedicineExamService")
public class TdInternalMedicineExamServiceImpl implements TdInternalMedicineExamService {
    @Resource
    private TdInternalMedicineExamMapper tdInternalMedicineExamDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdInternalMedicineExam queryById(String id) {
        return this.tdInternalMedicineExamDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdInternalMedicineExam 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdInternalMedicineExam> queryByPage(TdInternalMedicineExam tdInternalMedicineExam, PageRequest pageRequest) {
        long total = this.tdInternalMedicineExamDao.count(tdInternalMedicineExam);
        return new PageImpl<>(this.tdInternalMedicineExamDao.queryAllByLimit(tdInternalMedicineExam, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdInternalMedicineExam 实例对象
     * @return 实例对象
     */
    @Override
    public TdInternalMedicineExam insert(TdInternalMedicineExam tdInternalMedicineExam) {
        this.tdInternalMedicineExamDao.insert(tdInternalMedicineExam);
        return tdInternalMedicineExam;
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdInternalMedicineExamDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdInternalMedicineExam 实例对象
     * @return 实例对象
     */
    @Override
    public TdInternalMedicineExam update(TdInternalMedicineExam tdInternalMedicineExam) {
        this.tdInternalMedicineExamDao.update(tdInternalMedicineExam);
        return this.queryById(tdInternalMedicineExam.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.tdInternalMedicineExamDao.deleteById(id) > 0;
    }
}
