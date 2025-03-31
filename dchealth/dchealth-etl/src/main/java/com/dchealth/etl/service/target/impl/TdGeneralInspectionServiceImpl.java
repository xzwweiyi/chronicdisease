package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdGeneralInspection;
import com.dchealth.etl.dao.target.TdGeneralInspectionMapper;
import com.dchealth.etl.service.target.TdGeneralInspectionService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 一般检查表(TdGeneralInspection)表服务实现类
 *
 * @author makejava
 * @since 2023-03-28 13:42:42
 */
@Service("tdGeneralInspectionService")
public class TdGeneralInspectionServiceImpl implements TdGeneralInspectionService {
    @Resource
    private TdGeneralInspectionMapper tdGeneralInspectionDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdGeneralInspection queryById(Integer id) {
        return this.tdGeneralInspectionDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdGeneralInspection 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdGeneralInspection> queryByPage(TdGeneralInspection tdGeneralInspection, PageRequest pageRequest) {
        long total = this.tdGeneralInspectionDao.count(tdGeneralInspection);
        return new PageImpl<>(this.tdGeneralInspectionDao.queryAllByLimit(tdGeneralInspection, pageRequest), pageRequest, total);
    }

    /**
     * 查询数据列表
     *
     * @param tdGeneralInspection 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public List<TdGeneralInspection> queryInfoList(TdGeneralInspection tdGeneralInspection, PageRequest pageRequest){

        return this.tdGeneralInspectionDao.queryInfoList(tdGeneralInspection, pageRequest);

    }

    /**
     * 新增数据
     *
     * @param tdGeneralInspection 实例对象
     * @return 实例对象
     */
    @Override
    public TdGeneralInspection insert(TdGeneralInspection tdGeneralInspection) {
        this.tdGeneralInspectionDao.insert(tdGeneralInspection);
        return tdGeneralInspection;
    }
    /**
     * 新增数据
     * @param entities 实例对象
     * @return 实例对象
     */
    @Override
    public int insertBatch(List<TdGeneralInspection> entities){
        return this.tdGeneralInspectionDao.insertBatch(entities);
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdGeneralInspectionDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdGeneralInspection 实例对象
     * @return 实例对象
     */
    @Override
    public TdGeneralInspection update(TdGeneralInspection tdGeneralInspection) {
        this.tdGeneralInspectionDao.update(tdGeneralInspection);
        return this.queryById(tdGeneralInspection.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdGeneralInspectionDao.deleteById(id) > 0;
    }
}
