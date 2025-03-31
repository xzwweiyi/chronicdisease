package com.dchealth.etl.service.check.impl;

import com.dchealth.etl.domain.check.Jcb;
import com.dchealth.etl.dao.check.JcbMapper;
import com.dchealth.etl.service.check.JcbService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 检查表(Jcb)表服务实现类
 *
 * @author makejava
 * @since 2023-03-22 17:25:15
 */
@Service("jcbService")
public class JcbServiceImpl implements JcbService {
    @Resource
    private JcbMapper jcbMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param 主键
     * @return 实例对象
     */
    @Override
    public Jcb queryById(String studyId) {
        return this.jcbMapper.queryById(studyId);
    }

    /**
     * 分页查询
     *
     * @param jcb         筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Jcb> queryByPage(Jcb jcb, PageRequest pageRequest) {
        long total = this.jcbMapper.count(jcb);
        return new PageImpl<>(this.jcbMapper.queryAllByLimit(jcb, pageRequest), pageRequest, total);
    }
    /**
     * 查询总数
     *
     * @param jcb         筛选条件
     * @return 查询结果
     */
    @Override
    public long querySyncInfoCount(Jcb jcb) {
        return this.jcbMapper.querySyncInfoCount(jcb);
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
    public Page<Jcb> querySyncInfoByPage(Jcb jcb, PageRequest pageRequest,long total) {
        return new PageImpl<>(this.jcbMapper.querySyncInfoByPage(jcb, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param jcb 实例对象
     * @return 实例对象
     */
    @Override
    public Jcb insert(Jcb jcb) {
        this.jcbMapper.insert(jcb);
        return jcb;
    }

    /**
     * 通过主键删除数据
     *
     * @param 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById() {
        return this.jcbMapper.deleteById() > 0;
    }
}
