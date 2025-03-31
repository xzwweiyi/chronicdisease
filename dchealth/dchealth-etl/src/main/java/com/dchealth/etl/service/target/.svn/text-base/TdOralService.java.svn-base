package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdOral;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 唐都-口腔科相关表(TdOral)表服务接口
 *
 * @author makejava
 * @since 2023-04-07 14:56:03
 */
public interface TdOralService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdOral queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdOral 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdOral> queryByPage(TdOral tdOral, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdOral 实例对象
     * @return 实例对象
     */
    TdOral insert(TdOral tdOral);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdOral 实例对象
     * @return 实例对象
     */
    TdOral update(TdOral tdOral);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
