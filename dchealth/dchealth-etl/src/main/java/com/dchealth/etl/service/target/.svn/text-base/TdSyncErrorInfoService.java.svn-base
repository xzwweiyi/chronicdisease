package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdSyncErrorInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 唐都数据同步异常信息表(TdSyncErrorInfo)表服务接口
 *
 * @author makejava
 * @since 2023-03-27 16:22:02
 */
public interface TdSyncErrorInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdSyncErrorInfo queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdSyncErrorInfo 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdSyncErrorInfo> queryByPage(TdSyncErrorInfo tdSyncErrorInfo, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdSyncErrorInfo 实例对象
     * @return 实例对象
     */
    TdSyncErrorInfo insert(TdSyncErrorInfo tdSyncErrorInfo);

    /**
     * 修改数据
     *
     * @param tdSyncErrorInfo 实例对象
     * @return 实例对象
     */
    TdSyncErrorInfo update(TdSyncErrorInfo tdSyncErrorInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
