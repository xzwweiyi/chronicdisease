package com.dchealth.etl.service.record.impl;

import com.dchealth.etl.domain.record.Orders;
import com.dchealth.etl.domain.record.OutpMedrec;
import com.dchealth.etl.dao.record.OutpMedrecMapper;
import com.dchealth.etl.service.record.OutpMedrecService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 门诊诊断表(OutpMedrec)表服务实现类
 *
 * @author makejava
 * @since 2023-05-18 15:33:58
 */
@Service("outpMedrecService")
public class OutpMedrecServiceImpl implements OutpMedrecService {
    @Resource
    private OutpMedrecMapper outpMedrecDao;


    /**
     * 分页查询
     *
     * @param outpMedrec 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<OutpMedrec> queryByPage(OutpMedrec outpMedrec, PageRequest pageRequest) {
        long total = this.outpMedrecDao.count(outpMedrec);
        return new PageImpl<>(this.outpMedrecDao.queryAllByLimit(outpMedrec, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param outpMedrec 实例对象
     * @return 实例对象
     */
    @Override
    public OutpMedrec insert(OutpMedrec outpMedrec) {
        this.outpMedrecDao.insert(outpMedrec);
        return outpMedrec;
    }

    /**
     * 修改数据
     *
     * @param outpMedrec 实例对象
     * @return 实例对象
     */
    @Override
    public OutpMedrec update(OutpMedrec outpMedrec) {
        this.outpMedrecDao.update(outpMedrec);
        return outpMedrec;
    }

    @Override
    public int querySyncInfoCount(OutpMedrec outpMedrec) {
        return outpMedrecDao.querySyncInfoCount(outpMedrec);
    }

    @Override
    public Page<OutpMedrec> querySyncInfoByPage(OutpMedrec outpMedrec, PageRequest pageRequest, long total) {
        return new PageImpl<>(this.outpMedrecDao.querySyncInfoByPage(outpMedrec, pageRequest), pageRequest, total);
    }
}
