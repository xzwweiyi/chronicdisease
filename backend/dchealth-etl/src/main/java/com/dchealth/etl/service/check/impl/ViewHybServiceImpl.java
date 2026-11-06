package com.dchealth.etl.service.check.impl;

import com.dchealth.etl.dao.check.ViewHybMapper;
import com.dchealth.etl.domain.check.Hyb;
import com.dchealth.etl.domain.check.ViewHyb;
import com.dchealth.etl.service.check.ViewHybService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 检验结果(ViewHyb)表服务实现类
 *
 * @author makejava
 * @since 2023-06-06 11:34:37
 */
@Service("viewHybService")
public class ViewHybServiceImpl implements ViewHybService {
    @Resource
    private ViewHybMapper viewHybDao;

    /**
     * 通过ID查询单条数据
     *
     * @param studyid 主键
     * @return 实例对象
     */
    @Override
    public ViewHyb queryById(String studyid) {
        return this.viewHybDao.queryById(studyid);
    }

    /**
     * 分页查询
     *
     * @param viewHyb 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ViewHyb> queryByPage(ViewHyb viewHyb, PageRequest pageRequest) {
        long total = this.viewHybDao.count(viewHyb);
        return new PageImpl<>(this.viewHybDao.queryAllByLimit(viewHyb, pageRequest), pageRequest, total);
    }
    /**
     * 查询总数
     *
     * @param hyb         筛选条件
     * @return 查询结果
     */
    @Override
    public long querySyncInfoCount(ViewHyb hyb) {
        return this.viewHybDao.querySyncInfoCount(hyb);
    }
    /**
     * 分页查询
     *
     * @param hyb         筛选条件
     * @param pageRequest 分页对象
     * @param total
     * @return 查询结果
     */
    @Override
    public Page<ViewHyb> querySyncInfoByPage(ViewHyb hyb, PageRequest pageRequest,long total) {
        return new PageImpl<>(this.viewHybDao.querySyncInfoByPage(hyb, pageRequest), pageRequest, total);
    }
    /**
     * 新增数据
     *
     * @param viewHyb 实例对象
     * @return 实例对象
     */
    @Override
    public ViewHyb insert(ViewHyb viewHyb) {
        this.viewHybDao.insert(viewHyb);
        return viewHyb;
    }

    /**
     * 修改数据
     *
     * @param viewHyb 实例对象
     * @return 实例对象
     */
    @Override
    public ViewHyb update(ViewHyb viewHyb) {
        this.viewHybDao.update(viewHyb);
        return this.queryById(viewHyb.getStudyid());
    }

    /**
     * 通过主键删除数据
     *
     * @param studyid 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String studyid) {
        return this.viewHybDao.deleteById(studyid) > 0;
    }
}
