package com.dchealth.etl.service.check.impl;

import com.dchealth.etl.domain.check.Jlb;
import com.dchealth.etl.dao.check.JlbMapper;
import com.dchealth.etl.service.check.JlbService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 结论表(Jlb)表服务实现类
 *
 * @author makejava
 * @since 2023-03-22 17:46:18
 */
@Service("jlbService")
public class JlbServiceImpl implements JlbService {
    @Resource
    private JlbMapper jlbMapper;

    /**
     * 分页查询
     *
     * @param jlb 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<Jlb> queryByPage(Jlb jlb, PageRequest pageRequest) {
        long total = this.jlbMapper.count(jlb);
        return new PageImpl<>(this.jlbMapper.queryAllByLimit(jlb, pageRequest), pageRequest, total);
    }
    /**
     * 查询总数
     *
     * @param jlb         筛选条件
     * @return 查询结果
     */
    @Override
    public long querySyncInfoCount(Jlb jlb) {
        return this.jlbMapper.querySyncInfoCount(jlb);
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
    public Page<Jlb> querySyncInfoByPage(Jlb jlb, PageRequest pageRequest,long total) {
        return new PageImpl<>(this.jlbMapper.querySyncInfoByPage(jlb, pageRequest), pageRequest, total);
    }

    /**
     * 分页查询
     * @param params         筛选条件
     * @return 查询结果
     */
    @Override
    public List querySyncInfoByPage(Map params) {
        return this.jlbMapper.querySyncInfosByPage(params);
    }

    /**
     * 新增数据
     *
     * @param jlb 实例对象
     * @return 实例对象
     */
    @Override
    public Jlb insert(Jlb jlb) {
        this.jlbMapper.insert(jlb);
        return jlb;
    }


}
