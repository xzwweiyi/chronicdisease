package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdPatientBaseCheckRel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 基础信息表和体检关系表(TdPatientBaseCheckRel)表服务接口
 *
 * @author makejava
 * @since 2023-04-06 17:24:12
 */
public interface TdPatientBaseCheckRelService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdPatientBaseCheckRel queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdPatientBaseCheckRel 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdPatientBaseCheckRel> queryByPage(TdPatientBaseCheckRel tdPatientBaseCheckRel, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdPatientBaseCheckRel 实例对象
     * @return 实例对象
     */
    TdPatientBaseCheckRel insert(TdPatientBaseCheckRel tdPatientBaseCheckRel);

    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatch(List<TdPatientBaseCheckRel> entities);
    /**
     * 修改数据
     *
     * @param tdPatientBaseCheckRel 实例对象
     * @return 实例对象
     */
    TdPatientBaseCheckRel update(TdPatientBaseCheckRel tdPatientBaseCheckRel);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
