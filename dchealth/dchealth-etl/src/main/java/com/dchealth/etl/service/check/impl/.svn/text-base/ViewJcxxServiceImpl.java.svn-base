package com.dchealth.etl.service.check.impl;

import com.dchealth.etl.dao.check.ViewJcxxMapper;
import com.dchealth.etl.domain.check.Jcxx;
import com.dchealth.etl.domain.check.ViewJcxx;
import com.dchealth.etl.service.check.ViewJcxxService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 患者基本信息(ViewJcxx)表服务实现类
 *
 * @author makejava
 * @since 2023-06-06 11:34:37
 */
@Service("viewJcxxService")
public class ViewJcxxServiceImpl implements ViewJcxxService {
    @Resource
    private ViewJcxxMapper viewJcxxDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ViewJcxx queryById(String id) {
        return this.viewJcxxDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param viewJcxx 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ViewJcxx> queryByPage(ViewJcxx viewJcxx, PageRequest pageRequest) {
        long total = this.viewJcxxDao.count(viewJcxx);
        return new PageImpl<>(this.viewJcxxDao.queryAllByLimit(viewJcxx, pageRequest), pageRequest, total);
    }

    @Override
    public List<ViewJcxx> querySyncInfoByPage(ViewJcxx jcxx, PageRequest pageRequest) {
        return this.viewJcxxDao.querySyncInfoByPage(jcxx, pageRequest);
    }
    /**
     * 新增数据
     *
     * @param viewJcxx 实例对象
     * @return 实例对象
     */
    @Override
    public ViewJcxx insert(ViewJcxx viewJcxx) {
        this.viewJcxxDao.insert(viewJcxx);
        return viewJcxx;
    }

    /**
     * 修改数据
     *
     * @param viewJcxx 实例对象
     * @return 实例对象
     */
    @Override
    public ViewJcxx update(ViewJcxx viewJcxx) {
        this.viewJcxxDao.update(viewJcxx);
        return this.queryById(viewJcxx.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.viewJcxxDao.deleteById(id) > 0;
    }
}
