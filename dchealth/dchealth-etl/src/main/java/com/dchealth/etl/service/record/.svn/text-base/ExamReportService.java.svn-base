package com.dchealth.etl.service.record;

import com.dchealth.etl.domain.record.ExamReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 检查报告表
此表描述检查报告内容，由检查执行科室生成。
主键：申请序号。(ExamReport)表服务接口
 *
 * @author makejava
 * @since 2023-03-30 17:27:22
 */
public interface ExamReportService {

    /**
     * 通过ID查询单条数据
     *
     * @param examNo 主键
     * @return 实例对象
     */
    ExamReport queryById(String examNo);

    /**
     * 分页查询
     *
     * @param examReport 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ExamReport> queryByPage(ExamReport examReport, PageRequest pageRequest);

    /**
     * 查询当天同步数据量
     * @param examReport
     * @return
     */
    int querySyncInfoCount(ExamReport examReport);

    /**
     * 分页查询
     * @param examReport
     * @param pageRequest
     * @return
     */
    Page<ExamReport> querySyncInfoByPage(ExamReport examReport, PageRequest pageRequest,long total);

    /**
     * 新增数据
     *
     * @param examReport 实例对象
     * @return 实例对象
     */
    ExamReport insert(ExamReport examReport);

    /**
     * 修改数据
     *
     * @param examReport 实例对象
     * @return 实例对象
     */
    ExamReport update(ExamReport examReport);

    /**
     * 通过主键删除数据
     *
     * @param examNo 主键
     * @return 是否成功
     */
    boolean deleteById(String examNo);

}
