package com.dchealth.etl.service.check.impl;

import com.dchealth.etl.dao.check.ViewJlbMapper;
import com.dchealth.etl.domain.check.Jlb;
import com.dchealth.etl.domain.check.ViewJlb;
import com.dchealth.etl.service.check.ViewJlbService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 体检结论(ViewJlb)表服务实现类
 *
 * @author makejava
 * @since 2023-06-06 11:34:38
 */
@Service("viewJlbService")
public class ViewJlbServiceImpl implements ViewJlbService {
    @Resource
    private ViewJlbMapper viewJlbDao;

    /**
     * 通过ID查询单条数据
     *
     * @param studyid 主键
     * @return 实例对象
     */
    @Override
    public ViewJlb queryById(String studyid) {
        return this.viewJlbDao.queryById(studyid);
    }

    /**
     * 分页查询
     *
     * @param viewJlb 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ViewJlb> queryByPage(ViewJlb viewJlb, PageRequest pageRequest) {
        long total = this.viewJlbDao.count(viewJlb);
        return new PageImpl<>(this.viewJlbDao.queryAllByLimit(viewJlb, pageRequest), pageRequest, total);
    }

    /**
     * 查询总数
     *
     * @param jlb         筛选条件
     * @return 查询结果
     */
    @Override
    public long querySyncInfoCount(ViewJlb jlb) {
        return this.viewJlbDao.querySyncInfoCount(jlb);
    }
    /**
     * 分页查询
     *
     * @param jlb         筛选条件
     * @param pageRequest 分页对象
     * @param total
     * @return 查询结果
     */
    @Override
    public Page<ViewJlb> querySyncInfoByPage(ViewJlb jlb, PageRequest pageRequest,long total) {
        return new PageImpl<>(this.viewJlbDao.querySyncInfoByPage(jlb, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param viewJlb 实例对象
     * @return 实例对象
     */
    @Override
    public ViewJlb insert(ViewJlb viewJlb) {
        this.viewJlbDao.insert(viewJlb);
        return viewJlb;
    }

    /**
     * 修改数据
     *
     * @param viewJlb 实例对象
     * @return 实例对象
     */
    @Override
    public ViewJlb update(ViewJlb viewJlb) {
        this.viewJlbDao.update(viewJlb);
        return this.queryById(viewJlb.getStudyid());
    }

    /**
     * 通过主键删除数据
     *
     * @param studyid 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String studyid) {
        return this.viewJlbDao.deleteById(studyid) > 0;
    }
}
