package com.dchealth.etl.service.check.impl;

import com.dchealth.etl.domain.check.Jcmxx;
import com.dchealth.etl.dao.check.JcmxxMapper;
import com.dchealth.etl.service.check.JcmxxService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 查明细项目(Jcmxx)表服务实现类
 *
 * @author makejava
 * @since 2023-03-22 17:39:52
 */
@Service("jcmxxService")
public class JcmxxServiceImpl implements JcmxxService {
    @Resource
    private JcmxxMapper jcmxxMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    @Override
    public Jcmxx queryById( ) {
        return this.jcmxxMapper.queryById();
    }

    /**
     * 分页查询
     *
     * @param jcmxx 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<Jcmxx> queryByPage(Jcmxx jcmxx, PageRequest pageRequest) {
        long total = this.jcmxxMapper.count(jcmxx);
        return new PageImpl<>(this.jcmxxMapper.queryAllByLimit(jcmxx, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param jcmxx 实例对象
     * @return 实例对象
     */
    @Override
    public Jcmxx insert(Jcmxx jcmxx) {
        this.jcmxxMapper.insert(jcmxx);
        return jcmxx;
    }



    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById( ) {
        return this.jcmxxMapper.deleteById() > 0;
    }
}
