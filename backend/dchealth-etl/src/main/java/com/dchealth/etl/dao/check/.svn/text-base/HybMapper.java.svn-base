package com.dchealth.etl.dao.check;

import com.dchealth.etl.domain.check.Hyb;
import com.dchealth.etl.domain.check.Jcb;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 化验结果表（HYB）(Hyb)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-27 10:46:29
 */
@Repository
public interface HybMapper {


    /**
     * 查询指定行数据
     *
     * @param hyb 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<Hyb> queryAllByLimit(Hyb hyb, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param hyb 查询条件
     * @return 总行数
     */
    long count(Hyb hyb);
    /**
     * 统计总行数
     *
     * @param hyb 查询条件
     * @return 总行数
     */
    long querySyncInfoCount(Hyb hyb);

    /**
     * 查询指定行数据
     *
     * @param hyb      查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<Hyb> querySyncInfoByPage(@Param("hyb") Hyb hyb, @Param("pageable") Pageable pageable);
    /**
     * 新增数据
     *
     * @param hyb 实例对象
     * @return 影响行数
     */
    int insert(Hyb hyb);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Hyb> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Hyb> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Hyb> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Hyb> entities);

    /**
     * 修改数据
     *
     * @param hyb 实例对象
     * @return 影响行数
     */
    int update(Hyb hyb);



}

