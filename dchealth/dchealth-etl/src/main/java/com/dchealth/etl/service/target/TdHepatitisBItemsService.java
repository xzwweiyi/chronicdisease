package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdHepatitisBItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 乙肝项目表(TdHepatitisBItems)表服务接口
 *
 * @author makejava
 * @since 2023-04-10 17:03:30
 */
public interface TdHepatitisBItemsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdHepatitisBItems queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdHepatitisBItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdHepatitisBItems> queryByPage(TdHepatitisBItems tdHepatitisBItems, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdHepatitisBItems 实例对象
     * @return 实例对象
     */
    TdHepatitisBItems insert(TdHepatitisBItems tdHepatitisBItems);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdHepatitisBItems 实例对象
     * @return 实例对象
     */
    TdHepatitisBItems update(TdHepatitisBItems tdHepatitisBItems);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
