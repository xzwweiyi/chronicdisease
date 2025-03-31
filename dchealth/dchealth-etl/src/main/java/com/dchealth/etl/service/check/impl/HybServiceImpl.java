package com.dchealth.etl.service.check.impl;

import com.dchealth.etl.domain.check.Hyb;
import com.dchealth.etl.dao.check.HybMapper;
import com.dchealth.etl.service.check.HybService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 化验结果表（HYB）(Hyb)表服务实现类
 *
 * @author makejava
 * @since 2023-03-27 10:46:29
 */
@Service("hybService")
public class HybServiceImpl implements HybService {
    @Resource
    private HybMapper hybMapper;

    /**
     * 分页查询
     *
     * @param hyb         筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Hyb> queryByPage(Hyb hyb, PageRequest pageRequest) {
        long total = this.hybMapper.count(hyb);
        return new PageImpl<>(this.hybMapper.queryAllByLimit(hyb, pageRequest), pageRequest, total);
    }
    /**
     * 查询总数
     *
     * @param hyb         筛选条件
     * @return 查询结果
     */
    @Override
    public long querySyncInfoCount(Hyb hyb) {
        return this.hybMapper.querySyncInfoCount(hyb);
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
    public Page<Hyb> querySyncInfoByPage(Hyb hyb, PageRequest pageRequest,long total) {
        return new PageImpl<>(this.hybMapper.querySyncInfoByPage(hyb, pageRequest), pageRequest, total);
    }
    /**
     * 新增数据
     *
     * @param hyb 实例对象
     * @return 实例对象
     */
    @Override
    public Hyb insert(Hyb hyb) {
        this.hybMapper.insert(hyb);
        return hyb;
    }


}
