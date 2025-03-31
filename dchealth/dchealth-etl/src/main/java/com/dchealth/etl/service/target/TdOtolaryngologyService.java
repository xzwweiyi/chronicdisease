package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdOtolaryngology;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 唐都-耳鼻喉相关表(TdOtolaryngology)表服务接口
 *
 * @author makejava
 * @since 2023-04-07 14:32:35
 */
public interface TdOtolaryngologyService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdOtolaryngology queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdOtolaryngology 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdOtolaryngology> queryByPage(TdOtolaryngology tdOtolaryngology, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdOtolaryngology 实例对象
     * @return 实例对象
     */
    TdOtolaryngology insert(TdOtolaryngology tdOtolaryngology);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdOtolaryngology 实例对象
     * @return 实例对象
     */
    TdOtolaryngology update(TdOtolaryngology tdOtolaryngology);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
