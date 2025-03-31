package com.dchealth.etl.service.record.impl;

import com.dchealth.etl.domain.record.ExamMaster;
import com.dchealth.etl.dao.record.ExamMasterMapper;
import com.dchealth.etl.service.record.ExamMasterService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 检查主记录(ExamMaster)表服务实现类
 *
 * @author makejava
 * @since 2023-03-29 16:02:01
 */
@Service("examMasterService")
public class ExamMasterServiceImpl implements ExamMasterService {
    @Resource
    private ExamMasterMapper examMasterDao;

    /**
     * 通过ID查询单条数据
     *
     * @param examNo 主键
     * @return 实例对象
     */
    @Override
    public ExamMaster queryById(String examNo) {
        return this.examMasterDao.queryById(examNo);
    }

    /**
     * 分页查询
     *
     * @param examMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ExamMaster> queryByPage(ExamMaster examMaster, PageRequest pageRequest) {
        long total = this.examMasterDao.count(examMaster);
        return new PageImpl<>(this.examMasterDao.queryAllByLimit(examMaster, pageRequest), pageRequest, total);
    }
    /**
     * 查询当天同步数据量
     * @param examMaster
     * @return
     */
    @Override
    public int querySyncInfoCount(ExamMaster examMaster) {
        return this.examMasterDao.querySyncInfoCount(examMaster);
    }
    /**
     * 分页查询
     * @param examMaster
     * @param pageRequest
     * @return
     */
    @Override
    public Page<ExamMaster> querySyncInfoByPage(ExamMaster examMaster, PageRequest pageRequest,long total) {
        return new PageImpl<>(this.examMasterDao.querySyncInfoByPage(examMaster, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param examMaster 实例对象
     * @return 实例对象
     */
    @Override
    public ExamMaster insert(ExamMaster examMaster) {
        this.examMasterDao.insert(examMaster);
        return examMaster;
    }

    /**
     * 修改数据
     *
     * @param examMaster 实例对象
     * @return 实例对象
     */
    @Override
    public ExamMaster update(ExamMaster examMaster) {
        this.examMasterDao.update(examMaster);
        return this.queryById(examMaster.getExamNo());
    }

    /**
     * 通过主键删除数据
     *
     * @param examNo 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String examNo) {
        return this.examMasterDao.deleteById(examNo) > 0;
    }
}
