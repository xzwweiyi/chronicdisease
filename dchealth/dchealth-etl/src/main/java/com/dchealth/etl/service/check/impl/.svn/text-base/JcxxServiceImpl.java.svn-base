package com.dchealth.etl.service.check.impl;

import com.dchealth.etl.domain.check.Jcxx;
import com.dchealth.etl.dao.check.JcxxMapper;
import com.dchealth.etl.service.check.JcxxService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 检查信息表(Jcxx)表服务实现类
 *
 * @author makejava
 * @since 2023-03-22 17:44:09
 */
@Service("jcxxService")
public class JcxxServiceImpl implements JcxxService {
    @Resource
    private JcxxMapper jcxxMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Jcxx queryById(String id) {
        return this.jcxxMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param jcxx 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<Jcxx> queryByPage(Jcxx jcxx, PageRequest pageRequest) {
        long total = this.jcxxMapper.count(jcxx);
        return new PageImpl<>(this.jcxxMapper.queryAllByLimit(jcxx, pageRequest), pageRequest, total);
    }

    @Override
    public List<Jcxx> querySyncInfoByPage(Jcxx jcxx, PageRequest pageRequest) {
        return this.jcxxMapper.querySyncInfoByPage(jcxx, pageRequest);
    }

    /**
     * 新增数据
     *
     * @param jcxx 实例对象
     * @return 实例对象
     */
    @Override
    public Jcxx insert(Jcxx jcxx) {
        this.jcxxMapper.insert(jcxx);
        return jcxx;
    }

    /**
     * 修改数据
     *
     * @param jcxx 实例对象
     * @return 实例对象
     */
    @Override
    public Jcxx update(Jcxx jcxx) {
        this.jcxxMapper.update(jcxx);
        return this.queryById(jcxx.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.jcxxMapper.deleteById(id) > 0;
    }
}
