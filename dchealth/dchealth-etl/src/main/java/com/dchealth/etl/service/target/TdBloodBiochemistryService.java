package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdBloodBiochemistry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 唐都-血液生化表(TdBloodBiochemistry)表服务接口
 *
 * @author makejava
 * @since 2023-04-03 09:28:49
 */
public interface TdBloodBiochemistryService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdBloodBiochemistry queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdBloodBiochemistry 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdBloodBiochemistry> queryByPage(TdBloodBiochemistry tdBloodBiochemistry, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdBloodBiochemistry 实例对象
     * @return 实例对象
     */
    TdBloodBiochemistry insert(TdBloodBiochemistry tdBloodBiochemistry);
    /**
     * 批量插入
     * @param entities
     * @return
     */
    int insertBatch(List<TdBloodBiochemistry> entities);
    /**
     * 修改数据
     *
     * @param tdBloodBiochemistry 实例对象
     * @return 实例对象
     */
    TdBloodBiochemistry update(TdBloodBiochemistry tdBloodBiochemistry);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
