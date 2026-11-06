package com.dchealth.etl.dao.check;

import com.dchealth.etl.domain.check.Jcb;
import com.dchealth.etl.domain.check.ViewJcb;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 检查结果(ViewJcb)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-06 11:34:37
 */
public interface ViewJcbMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param studyid 主键
     * @return 实例对象
     */
    ViewJcb queryById(String studyid);

    /**
     * 查询指定行数据
     *
     * @param viewJcb 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ViewJcb> queryAllByLimit(ViewJcb viewJcb, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param viewJcb 查询条件
     * @return 总行数
     */
    long count(ViewJcb viewJcb);
    /**
     * 统计总行数
     *
     * @param jcb 查询条件
     * @return 总行数
     */
    long querySyncInfoCount(ViewJcb jcb);

    /**
     * 查询指定行数据
     *
     * @param jcb      查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<ViewJcb> querySyncInfoByPage(@Param("jcb") ViewJcb jcb, @Param("pageable") Pageable pageable);
    /**
     * 新增数据
     *
     * @param viewJcb 实例对象
     * @return 影响行数
     */
    int insert(ViewJcb viewJcb);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ViewJcb> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ViewJcb> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ViewJcb> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ViewJcb> entities);

    /**
     * 修改数据
     *
     * @param viewJcb 实例对象
     * @return 影响行数
     */
    int update(ViewJcb viewJcb);

    /**
     * 通过主键删除数据
     *
     * @param studyid 主键
     * @return 影响行数
     */
    int deleteById(String studyid);

}

