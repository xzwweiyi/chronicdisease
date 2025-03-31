package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdDiagnosis;
import com.dchealth.etl.dao.target.TdDiagnosisMapper;
import com.dchealth.etl.service.target.TdDiagnosisService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 诊断记录(TdDiagnosis)表服务实现类
 *
 * @author makejava
 * @since 2023-04-04 16:33:42
 */
@Service("tdDiagnosisService")
public class TdDiagnosisServiceImpl implements TdDiagnosisService {
    @Resource
    private TdDiagnosisMapper tdDiagnosisDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdDiagnosis queryById(Integer id) {
        return this.tdDiagnosisDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdDiagnosis 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdDiagnosis> queryByPage(TdDiagnosis tdDiagnosis, PageRequest pageRequest) {
        long total = this.tdDiagnosisDao.count(tdDiagnosis);
        return new PageImpl<>(this.tdDiagnosisDao.queryAllByLimit(tdDiagnosis, pageRequest), pageRequest, total);
    }

    @Override
    public List<TdDiagnosis> queryByPage(TdDiagnosis tdDiagnosis) {
        return this.tdDiagnosisDao.queryByPage(tdDiagnosis);
    }

    /**
     * 新增数据
     *
     * @param tdDiagnosis 实例对象
     * @return 实例对象
     */
    @Override
    public TdDiagnosis insert(TdDiagnosis tdDiagnosis) {
        this.tdDiagnosisDao.insert(tdDiagnosis);
        return tdDiagnosis;
    }
    /**
     * 批量插入
     * @param entities
     * @return
     */
    @Override
    public int insertBatch(List<TdDiagnosis> entities){
        return this.tdDiagnosisDao.insertBatch(entities);
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdDiagnosisDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdDiagnosis 实例对象
     * @return 实例对象
     */
    @Override
    public TdDiagnosis update(TdDiagnosis tdDiagnosis) {
        this.tdDiagnosisDao.update(tdDiagnosis);
        return this.queryById(tdDiagnosis.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdDiagnosisDao.deleteById(id) > 0;
    }
}
