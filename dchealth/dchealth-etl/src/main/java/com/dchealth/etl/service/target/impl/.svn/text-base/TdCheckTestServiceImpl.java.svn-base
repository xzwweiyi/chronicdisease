package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdCheckTest;
import com.dchealth.etl.dao.target.TdCheckTestMapper;
import com.dchealth.etl.service.target.TdCheckTestService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 唐都-体检检验表(TdCheckTest)表服务实现类
 *
 * @author makejava
 * @since 2023-04-11 14:24:24
 */
@Service("tdCheckTestService")
public class TdCheckTestServiceImpl implements TdCheckTestService {
    @Resource
    private TdCheckTestMapper tdCheckTestDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdCheckTest queryById(Integer id) {
        return this.tdCheckTestDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdCheckTest 筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdCheckTest> queryByPage(TdCheckTest tdCheckTest, PageRequest pageRequest) {
        long total = this.tdCheckTestDao.count(tdCheckTest);
        return new PageImpl<>(this.tdCheckTestDao.queryAllByLimit(tdCheckTest, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdCheckTest 实例对象
     * @return 实例对象
     */
    @Override
    public TdCheckTest insert(TdCheckTest tdCheckTest) {
        this.tdCheckTestDao.insert(tdCheckTest);
        return tdCheckTest;
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdCheckTestDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdCheckTest 实例对象
     * @return 实例对象
     */
    @Override
    public TdCheckTest update(TdCheckTest tdCheckTest) {
        this.tdCheckTestDao.update(tdCheckTest);
        return this.queryById(tdCheckTest.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdCheckTestDao.deleteById(id) > 0;
    }
}
