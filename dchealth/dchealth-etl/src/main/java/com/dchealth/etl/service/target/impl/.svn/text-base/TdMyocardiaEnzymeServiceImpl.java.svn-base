package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdMyocardiaEnzyme;
import com.dchealth.etl.dao.target.TdMyocardiaEnzymeMapper;
import com.dchealth.etl.service.target.TdMyocardiaEnzymeService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 唐都-心肌酶表(TdMyocardiaEnzyme)表服务实现类
 *
 * @author makejava
 * @since 2023-04-03 09:35:31
 */
@Service("tdMyocardiaEnzymeService")
public class TdMyocardiaEnzymeServiceImpl implements TdMyocardiaEnzymeService {
    @Resource
    private TdMyocardiaEnzymeMapper tdMyocardiaEnzymeDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdMyocardiaEnzyme queryById(Integer id) {
        return this.tdMyocardiaEnzymeDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdMyocardiaEnzyme 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdMyocardiaEnzyme> queryByPage(TdMyocardiaEnzyme tdMyocardiaEnzyme, PageRequest pageRequest) {
        long total = this.tdMyocardiaEnzymeDao.count(tdMyocardiaEnzyme);
        return new PageImpl<>(this.tdMyocardiaEnzymeDao.queryAllByLimit(tdMyocardiaEnzyme, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdMyocardiaEnzyme 实例对象
     * @return 实例对象
     */
    @Override
    public TdMyocardiaEnzyme insert(TdMyocardiaEnzyme tdMyocardiaEnzyme) {
        this.tdMyocardiaEnzymeDao.insert(tdMyocardiaEnzyme);
        return tdMyocardiaEnzyme;
    }
    /**
     * 新增数据
     *
     * @param entities 实例对象
     * @return 实例对象
     */
    @Override
    public int insertBatch(List<TdMyocardiaEnzyme> entities) {
        return this.tdMyocardiaEnzymeDao.insertBatch(entities);
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdMyocardiaEnzymeDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdMyocardiaEnzyme 实例对象
     * @return 实例对象
     */
    @Override
    public TdMyocardiaEnzyme update(TdMyocardiaEnzyme tdMyocardiaEnzyme) {
        this.tdMyocardiaEnzymeDao.update(tdMyocardiaEnzyme);
        return this.queryById(tdMyocardiaEnzyme.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdMyocardiaEnzymeDao.deleteById(id) > 0;
    }
}
