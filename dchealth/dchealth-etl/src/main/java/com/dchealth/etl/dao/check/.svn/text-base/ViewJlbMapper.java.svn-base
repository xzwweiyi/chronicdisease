package com.dchealth.etl.dao.check;

import com.dchealth.etl.domain.check.Jlb;
import com.dchealth.etl.domain.check.ViewJlb;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 体检结论(ViewJlb)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-06 11:34:38
 */
public interface ViewJlbMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param studyid 主键
     * @return 实例对象
     */
    ViewJlb queryById(String studyid);

    /**
     * 查询指定行数据
     *
     * @param viewJlb 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ViewJlb> queryAllByLimit(ViewJlb viewJlb, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param viewJlb 查询条件
     * @return 总行数
     */
    long count(ViewJlb viewJlb);

    /**
     * 统计总行数
     *
     * @param jlb 查询条件
     * @return 总行数
     */
    long querySyncInfoCount(ViewJlb jlb);

    /**
     * 查询指定行数据
     *
     * @param jlb      查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<ViewJlb> querySyncInfoByPage(@Param("jlb") ViewJlb jlb, @Param("pageable") Pageable pageable);

    /**
     * 新增数据
     *
     * @param viewJlb 实例对象
     * @return 影响行数
     */
    int insert(ViewJlb viewJlb);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ViewJlb> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ViewJlb> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ViewJlb> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ViewJlb> entities);

    /**
     * 修改数据
     *
     * @param viewJlb 实例对象
     * @return 影响行数
     */
    int update(ViewJlb viewJlb);

    /**
     * 通过主键删除数据
     *
     * @param studyid 主键
     * @return 影响行数
     */
    int deleteById(String studyid);

}

