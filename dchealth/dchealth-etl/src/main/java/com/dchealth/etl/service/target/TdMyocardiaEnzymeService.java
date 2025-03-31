package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdMyocardiaEnzyme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 唐都-心肌酶表(TdMyocardiaEnzyme)表服务接口
 *
 * @author makejava
 * @since 2023-04-03 09:35:31
 */
public interface TdMyocardiaEnzymeService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdMyocardiaEnzyme queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdMyocardiaEnzyme 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdMyocardiaEnzyme> queryByPage(TdMyocardiaEnzyme tdMyocardiaEnzyme, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdMyocardiaEnzyme 实例对象
     * @return 实例对象
     */
    TdMyocardiaEnzyme insert(TdMyocardiaEnzyme tdMyocardiaEnzyme);
    /**
     * 批量插入
     * @param entities
     * @return
     */
    int insertBatch(List<TdMyocardiaEnzyme> entities);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdMyocardiaEnzyme 实例对象
     * @return 实例对象
     */
    TdMyocardiaEnzyme update(TdMyocardiaEnzyme tdMyocardiaEnzyme);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
