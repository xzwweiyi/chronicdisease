package com.dchealth.etl.service.check.impl;

import com.dchealth.etl.dao.check.ViewJcbMapper;
import com.dchealth.etl.domain.check.Jcb;
import com.dchealth.etl.domain.check.ViewJcb;
import com.dchealth.etl.service.check.ViewJcbService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 检查结果(ViewJcb)表服务实现类
 *
 * @author makejava
 * @since 2023-06-06 11:34:37
 */
@Service("viewJcbService")
public class ViewJcbServiceImpl implements ViewJcbService {
    @Resource
    private ViewJcbMapper viewJcbDao;

    /**
     * 通过ID查询单条数据
     *
     * @param studyid 主键
     * @return 实例对象
     */
    @Override
    public ViewJcb queryById(String studyid) {
        return this.viewJcbDao.queryById(studyid);
    }

    /**
     * 分页查询
     *
     * @param viewJcb 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ViewJcb> queryByPage(ViewJcb viewJcb, PageRequest pageRequest) {
        long total = this.viewJcbDao.count(viewJcb);
        return new PageImpl<>(this.viewJcbDao.queryAllByLimit(viewJcb, pageRequest), pageRequest, total);
    }

    /**
     * 查询总数
     *
     * @param jcb         筛选条件
     * @return 查询结果
     */
    @Override
    public long querySyncInfoCount(ViewJcb jcb) {
        return this.viewJcbDao.querySyncInfoCount(jcb);
    }
    /**
     * 分页查询
     *
     * @param jcb         筛选条件
     * @param pageRequest 分页对象
     * @param total
     * @return 查询结果
     */
    @Override
    public Page<ViewJcb> querySyncInfoByPage(ViewJcb jcb, PageRequest pageRequest,long total) {
        return new PageImpl<>(this.viewJcbDao.querySyncInfoByPage(jcb, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param viewJcb 实例对象
     * @return 实例对象
     */
    @Override
    public ViewJcb insert(ViewJcb viewJcb) {
        this.viewJcbDao.insert(viewJcb);
        return viewJcb;
    }

    /**
     * 修改数据
     *
     * @param viewJcb 实例对象
     * @return 实例对象
     */
    @Override
    public ViewJcb update(ViewJcb viewJcb) {
        this.viewJcbDao.update(viewJcb);
        return this.queryById(viewJcb.getStudyid());
    }

    /**
     * 通过主键删除数据
     *
     * @param studyid 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String studyid) {
        return this.viewJcbDao.deleteById(studyid) > 0;
    }
}
