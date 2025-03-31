package com.dchealth.etl.service.record;

import com.dchealth.etl.domain.record.ExamMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 检查主记录(ExamMaster)表服务接口
 *
 * @author makejava
 * @since 2023-03-29 16:02:01
 */
public interface ExamMasterService {

    /**
     * 通过ID查询单条数据
     *
     * @param examNo 主键
     * @return 实例对象
     */
    ExamMaster queryById(String examNo);

    /**
     * 分页查询
     *
     * @param examMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ExamMaster> queryByPage(ExamMaster examMaster, PageRequest pageRequest);

    /**
     * 查询当天同步数据量
     * @param examMaster
     * @return
     */
    int querySyncInfoCount(ExamMaster examMaster);

    /**
     * 分页查询
     * @param examMaster
     * @param pageRequest
     * @return
     */
    Page<ExamMaster> querySyncInfoByPage(ExamMaster examMaster, PageRequest pageRequest,long total);
    /**
     * 新增数据
     *
     * @param examMaster 实例对象
     * @return 实例对象
     */
    ExamMaster insert(ExamMaster examMaster);

    /**
     * 修改数据
     *
     * @param examMaster 实例对象
     * @return 实例对象
     */
    ExamMaster update(ExamMaster examMaster);

    /**
     * 通过主键删除数据
     *
     * @param examNo 主键
     * @return 是否成功
     */
    boolean deleteById(String examNo);

}
