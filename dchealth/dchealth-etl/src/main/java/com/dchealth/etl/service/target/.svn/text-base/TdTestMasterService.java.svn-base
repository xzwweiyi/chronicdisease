package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdTestMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 检验主记录(TdTestMaster)表服务接口
 *
 * @author makejava
 * @since 2023-03-30 11:23:00
 */
public interface TdTestMasterService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdTestMaster queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdTestMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdTestMaster> queryByPage(TdTestMaster tdTestMaster, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdTestMaster 实例对象
     * @return 实例对象
     */
    TdTestMaster insert(TdTestMaster tdTestMaster);

    /**
     * 新增数据
     * @param tdTestMasters
     * @return
     */
    public int insertBatch(List<TdTestMaster> tdTestMasters);
    /**
     * 修改数据
     *
     * @param tdTestMaster 实例对象
     * @return 实例对象
     */
    TdTestMaster update(TdTestMaster tdTestMaster);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
