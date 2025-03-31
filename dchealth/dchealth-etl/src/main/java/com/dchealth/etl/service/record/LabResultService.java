package com.dchealth.etl.service.record;

import com.dchealth.etl.domain.record.LabResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 检验结果(LabResult)表服务接口
 *
 * @author makejava
 * @since 2023-03-31 17:13:06
 */
public interface LabResultService {

    /**
     * 通过ID查询单条数据
     *
     * @param  testNo
     * @return 实例对象
     */
    LabResult queryById(String testNo);

    /**
     * 分页查询
     *
     * @param labResult 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<LabResult> queryByPage(LabResult labResult, PageRequest pageRequest);

    /**
     * 查询当天同步数据量
     * @param labResult
     * @return
     */
    int querySyncInfoCount(LabResult labResult);
    /**
     * 分页查询
     * @param labResult
     * @param pageRequest
     * @return
     */
    Page<LabResult> querySyncInfoByPage(LabResult labResult,PageRequest pageRequest,long total);
    /**
     * 新增数据
     *
     * @param labResult 实例对象
     * @return 实例对象
     */
    LabResult insert(LabResult labResult);

    /**
     * 修改数据
     *
     * @param labResult 实例对象
     * @return 实例对象
     */
    LabResult update(LabResult labResult);

    /**
     * 通过主键删除数据
     *
     * @param  testNo
     * @return 是否成功
     */
    boolean deleteById(String testNo);

}
