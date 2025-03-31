package com.dchealth.etl.service.record.impl;

import com.dchealth.etl.domain.record.ExamReport;
import com.dchealth.etl.dao.record.ExamReportMapper;
import com.dchealth.etl.service.record.ExamReportService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 检查报告表
此表描述检查报告内容，由检查执行科室生成。
主键：申请序号。(ExamReport)表服务实现类
 *
 * @author makejava
 * @since 2023-03-30 17:27:22
 */
@Service("examReportService")
public class ExamReportServiceImpl implements ExamReportService {
    @Resource
    private ExamReportMapper examReportDao;

    /**
     * 通过ID查询单条数据
     *
     * @param examNo 主键
     * @return 实例对象
     */
    @Override
    public ExamReport queryById(String examNo) {
        return this.examReportDao.queryById(examNo);
    }

    /**
     * 分页查询
     *
     * @param examReport 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ExamReport> queryByPage(ExamReport examReport, PageRequest pageRequest) {
        long total = this.examReportDao.count(examReport);
        return new PageImpl<>(this.examReportDao.queryAllByLimit(examReport, pageRequest), pageRequest, total);
    }

    /**
     * 查询当天同步数据量
     * @param examReport
     * @return
     */
    @Override
    public int querySyncInfoCount(ExamReport examReport){
        return this.examReportDao.querySyncInfoCount(examReport);
    }

    /**
     * 分页查询
     * @param examReport
     * @param pageRequest
     * @return
     */
    @Override
    public Page<ExamReport> querySyncInfoByPage(ExamReport examReport, PageRequest pageRequest,long total){
        return new PageImpl<>(this.examReportDao.querySyncInfoByPage(examReport, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param examReport 实例对象
     * @return 实例对象
     */
    @Override
    public ExamReport insert(ExamReport examReport) {
        this.examReportDao.insert(examReport);
        return examReport;
    }

    /**
     * 修改数据
     *
     * @param examReport 实例对象
     * @return 实例对象
     */
    @Override
    public ExamReport update(ExamReport examReport) {
        this.examReportDao.update(examReport);
        return this.queryById(examReport.getExamNo());
    }

    /**
     * 通过主键删除数据
     *
     * @param examNo 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String examNo) {
        return this.examReportDao.deleteById(examNo) > 0;
    }
}
