package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdBloodBiochemistry;
import com.dchealth.etl.dao.target.TdBloodBiochemistryMapper;
import com.dchealth.etl.service.target.TdBloodBiochemistryService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 唐都-血液生化表(TdBloodBiochemistry)表服务实现类
 *
 * @author makejava
 * @since 2023-04-03 09:28:49
 */
@Service("tdBloodBiochemistryService")
public class TdBloodBiochemistryServiceImpl implements TdBloodBiochemistryService {
    @Resource
    private TdBloodBiochemistryMapper tdBloodBiochemistryDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdBloodBiochemistry queryById(Integer id) {
        return this.tdBloodBiochemistryDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdBloodBiochemistry 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdBloodBiochemistry> queryByPage(TdBloodBiochemistry tdBloodBiochemistry, PageRequest pageRequest) {
        long total = this.tdBloodBiochemistryDao.count(tdBloodBiochemistry);
        return new PageImpl<>(this.tdBloodBiochemistryDao.queryAllByLimit(tdBloodBiochemistry, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdBloodBiochemistry 实例对象
     * @return 实例对象
     */
    @Override
    public TdBloodBiochemistry insert(TdBloodBiochemistry tdBloodBiochemistry) {
        this.tdBloodBiochemistryDao.insert(tdBloodBiochemistry);
        return tdBloodBiochemistry;
    }
    /**
     * 新增数据
     *
     * @param entities 实例对象
     * @return 实例对象
     */
    @Override
    public int insertBatch(List<TdBloodBiochemistry> entities) {
        return this.tdBloodBiochemistryDao.insertBatch(entities);
    }

    /**
     * 修改数据
     *
     * @param tdBloodBiochemistry 实例对象
     * @return 实例对象
     */
    @Override
    public TdBloodBiochemistry update(TdBloodBiochemistry tdBloodBiochemistry) {
        this.tdBloodBiochemistryDao.update(tdBloodBiochemistry);
        return this.queryById(tdBloodBiochemistry.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdBloodBiochemistryDao.deleteById(id) > 0;
    }
}
