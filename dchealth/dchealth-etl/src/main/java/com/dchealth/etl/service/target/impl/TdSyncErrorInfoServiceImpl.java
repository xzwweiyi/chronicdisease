package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdSyncErrorInfo;
import com.dchealth.etl.dao.target.TdSyncErrorInfoMapper;
import com.dchealth.etl.service.target.TdSyncErrorInfoService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 唐都数据同步异常信息表(TdSyncErrorInfo)表服务实现类
 *
 * @author makejava
 * @since 2023-03-27 16:22:02
 */
@Service("tdSyncErrorInfoService")
public class TdSyncErrorInfoServiceImpl implements TdSyncErrorInfoService {
    @Resource
    private TdSyncErrorInfoMapper tdSyncErrorInfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdSyncErrorInfo queryById(Integer id) {
        return this.tdSyncErrorInfoDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdSyncErrorInfo 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdSyncErrorInfo> queryByPage(TdSyncErrorInfo tdSyncErrorInfo, PageRequest pageRequest) {
        long total = this.tdSyncErrorInfoDao.count(tdSyncErrorInfo);
        return new PageImpl<>(this.tdSyncErrorInfoDao.queryAllByLimit(tdSyncErrorInfo, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdSyncErrorInfo 实例对象
     * @return 实例对象
     */
    @Override
    public TdSyncErrorInfo insert(TdSyncErrorInfo tdSyncErrorInfo) {
        this.tdSyncErrorInfoDao.insert(tdSyncErrorInfo);
        return tdSyncErrorInfo;
    }

    /**
     * 修改数据
     *
     * @param tdSyncErrorInfo 实例对象
     * @return 实例对象
     */
    @Override
    public TdSyncErrorInfo update(TdSyncErrorInfo tdSyncErrorInfo) {
        this.tdSyncErrorInfoDao.update(tdSyncErrorInfo);
        return this.queryById(tdSyncErrorInfo.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdSyncErrorInfoDao.deleteById(id) > 0;
    }
}
