package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdPatientBaseCheckRel;
import com.dchealth.etl.dao.target.TdPatientBaseCheckRelMapper;
import com.dchealth.etl.service.target.TdPatientBaseCheckRelService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 基础信息表和体检关系表(TdPatientBaseCheckRel)表服务实现类
 *
 * @author makejava
 * @since 2023-04-06 17:24:12
 */
@Service("tdPatientBaseCheckRelService")
public class TdPatientBaseCheckRelServiceImpl implements TdPatientBaseCheckRelService {
    @Resource
    private TdPatientBaseCheckRelMapper tdPatientBaseCheckRelDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdPatientBaseCheckRel queryById(Integer id) {
        return this.tdPatientBaseCheckRelDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdPatientBaseCheckRel 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdPatientBaseCheckRel> queryByPage(TdPatientBaseCheckRel tdPatientBaseCheckRel, PageRequest pageRequest) {
        long total = this.tdPatientBaseCheckRelDao.count(tdPatientBaseCheckRel);
        return new PageImpl<>(this.tdPatientBaseCheckRelDao.queryAllByLimit(tdPatientBaseCheckRel, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdPatientBaseCheckRel 实例对象
     * @return 实例对象
     */
    @Override
    public TdPatientBaseCheckRel insert(TdPatientBaseCheckRel tdPatientBaseCheckRel) {
        this.tdPatientBaseCheckRelDao.insert(tdPatientBaseCheckRel);
        return tdPatientBaseCheckRel;
    }

    @Override
    public int insertBatch(List<TdPatientBaseCheckRel> entities) {
        return this.tdPatientBaseCheckRelDao.insertBatch(entities);
    }

    /**
     * 修改数据
     *
     * @param tdPatientBaseCheckRel 实例对象
     * @return 实例对象
     */
    @Override
    public TdPatientBaseCheckRel update(TdPatientBaseCheckRel tdPatientBaseCheckRel) {
        this.tdPatientBaseCheckRelDao.update(tdPatientBaseCheckRel);
        return this.queryById(tdPatientBaseCheckRel.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdPatientBaseCheckRelDao.deleteById(id) > 0;
    }
}
