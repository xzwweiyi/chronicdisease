package com.dchealth.etl.service.check.impl;


import com.dchealth.etl.dao.check.ViewPacsMapper;
import com.dchealth.etl.domain.check.ViewJcb;
import com.dchealth.etl.domain.check.ViewPacs;
import com.dchealth.etl.service.check.ViewPacsService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 影像类检查(ViewPacs)表服务实现类
 *
 * @author makejava
 * @since 2023-06-06 11:34:38
 */
@Service("viewPacsService")
public class ViewPacsServiceImpl implements ViewPacsService {
    @Resource
    private ViewPacsMapper viewPacsDao;


    /**
     * 分页查询
     *
     * @param viewPacs 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ViewPacs> queryByPage(ViewPacs viewPacs, PageRequest pageRequest) {
        long total = this.viewPacsDao.count(viewPacs);
        return new PageImpl<>(this.viewPacsDao.queryAllByLimit(viewPacs, pageRequest), pageRequest, total);
    }

    /**
     * 查询总数
     *
     * @param viewPacs         筛选条件
     * @return 查询结果
     */
    @Override
    public long querySyncInfoCount(ViewPacs viewPacs) {
        return this.viewPacsDao.querySyncInfoCount(viewPacs);
    }
    /**
     * 分页查询
     *
     * @param viewPacs         筛选条件
     * @param pageRequest 分页对象
     * @param total
     * @return 查询结果
     */
    @Override
    public Page<ViewPacs> querySyncInfoByPage(ViewPacs viewPacs, PageRequest pageRequest,long total) {
        return new PageImpl<>(this.viewPacsDao.querySyncInfoByPage(viewPacs, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param viewPacs 实例对象
     * @return 实例对象
     */
    @Override
    public ViewPacs insert(ViewPacs viewPacs) {
        this.viewPacsDao.insert(viewPacs);
        return viewPacs;
    }


}
