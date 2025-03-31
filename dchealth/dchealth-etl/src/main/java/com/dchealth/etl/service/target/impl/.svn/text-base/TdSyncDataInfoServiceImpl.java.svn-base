package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdSyncDataInfo;
import com.dchealth.etl.dao.target.TdSyncDataInfoMapper;
import com.dchealth.etl.service.target.TdSyncDataInfoService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 数据同步信息(TdSyncDataInfo)表服务实现类
 *
 * @author makejava
 * @since 2023-03-27 16:17:57
 */
@Service("tdSyncDataInfoService")
public class TdSyncDataInfoServiceImpl implements TdSyncDataInfoService {
    @Resource
    private TdSyncDataInfoMapper tdSyncDataInfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdSyncDataInfo queryById(Integer id) {
        return this.tdSyncDataInfoDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdSyncDataInfo 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdSyncDataInfo> queryByPage(TdSyncDataInfo tdSyncDataInfo, PageRequest pageRequest) {
        long total = this.tdSyncDataInfoDao.count(tdSyncDataInfo);
        return new PageImpl<>(this.tdSyncDataInfoDao.queryAllByLimit(tdSyncDataInfo, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdSyncDataInfo 实例对象
     * @return 实例对象
     */
    @Override
    public TdSyncDataInfo insert(TdSyncDataInfo tdSyncDataInfo) {
        this.tdSyncDataInfoDao.insert(tdSyncDataInfo);
        return tdSyncDataInfo;
    }

    /**
     * 修改数据
     *
     * @param tdSyncDataInfo 实例对象
     * @return 实例对象
     */
    @Override
    public TdSyncDataInfo update(TdSyncDataInfo tdSyncDataInfo) {
        this.tdSyncDataInfoDao.update(tdSyncDataInfo);
        return this.queryById(tdSyncDataInfo.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdSyncDataInfoDao.deleteById(id) > 0;
    }

    /**
     * 通过tableName查询单条数据
     *
     * @param tableName
     * @return 实例对象
     */
    @Override
    public TdSyncDataInfo queryByTableName(String tableName){
        return this.tdSyncDataInfoDao.queryByTableName(tableName);
    }

}
