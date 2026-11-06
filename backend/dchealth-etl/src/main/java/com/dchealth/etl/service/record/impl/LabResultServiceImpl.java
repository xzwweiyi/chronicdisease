package com.dchealth.etl.service.record.impl;

import com.dchealth.etl.domain.record.ExamReport;
import com.dchealth.etl.domain.record.LabResult;
import com.dchealth.etl.dao.record.LabResultMapper;
import com.dchealth.etl.service.record.LabResultService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 检验结果(LabResult)表服务实现类
 *
 * @author makejava
 * @since 2023-03-31 17:13:06
 */
@Service("labResultService")
public class LabResultServiceImpl implements LabResultService {
    @Resource
    private LabResultMapper labResultDao;

    /**
     * 通过ID查询单条数据
     *
     * @param  testNo
     * @return 实例对象
     */
    @Override
    public LabResult queryById(String testNo) {
        return this.labResultDao.queryById(testNo);
    }

    /**
     * 分页查询
     *
     * @param labResult 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<LabResult> queryByPage(LabResult labResult, PageRequest pageRequest) {
        long total = this.labResultDao.count(labResult);
        return new PageImpl<>(this.labResultDao.queryAllByLimit(labResult, pageRequest), pageRequest, total);
    }

    /**
     * 查询当天同步数据量
     * @param labResult
     * @return
     */
    @Override
    public int querySyncInfoCount(LabResult labResult) {
        return this.labResultDao.querySyncInfoCount(labResult);
    }
    /**
     * 分页查询
     * @param labResult
     * @param pageRequest
     * @return
     */
    @Override
    public Page<LabResult> querySyncInfoByPage(LabResult labResult, PageRequest pageRequest, long total) {
        return new PageImpl<>(this.labResultDao.querySyncInfoByPage(labResult, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param labResult 实例对象
     * @return 实例对象
     */
    @Override
    public LabResult insert(LabResult labResult) {
        this.labResultDao.insert(labResult);
        return labResult;
    }

    /**
     * 修改数据
     *
     * @param labResult 实例对象
     * @return 实例对象
     */
    @Override
    public LabResult update(LabResult labResult) {
        this.labResultDao.update(labResult);
        return this.queryById(labResult.getTestNo());
    }

    /**
     * 通过主键删除数据
     *
     * @param  testNo
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String testNo) {
        return this.labResultDao.deleteById(testNo) > 0;
    }
}
