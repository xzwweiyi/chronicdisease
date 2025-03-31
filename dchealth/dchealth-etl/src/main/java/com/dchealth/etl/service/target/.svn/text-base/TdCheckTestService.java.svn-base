package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdCheckTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 唐都-体检检验表(TdCheckTest)表服务接口
 *
 * @author makejava
 * @since 2023-04-11 14:24:24
 */
public interface TdCheckTestService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdCheckTest queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdCheckTest 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdCheckTest> queryByPage(TdCheckTest tdCheckTest, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdCheckTest 实例对象
     * @return 实例对象
     */
    TdCheckTest insert(TdCheckTest tdCheckTest);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdCheckTest 实例对象
     * @return 实例对象
     */
    TdCheckTest update(TdCheckTest tdCheckTest);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
