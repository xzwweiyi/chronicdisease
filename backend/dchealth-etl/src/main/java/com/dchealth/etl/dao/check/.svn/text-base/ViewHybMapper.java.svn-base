package com.dchealth.etl.dao.check;

import com.dchealth.etl.domain.check.Hyb;
import com.dchealth.etl.domain.check.ViewHyb;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 检验结果(ViewHyb)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-06 11:34:37
 */
public interface ViewHybMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param studyid 主键
     * @return 实例对象
     */
    ViewHyb queryById(String studyid);

    /**
     * 查询指定行数据
     *
     * @param viewHyb 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ViewHyb> queryAllByLimit(ViewHyb viewHyb, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param viewHyb 查询条件
     * @return 总行数
     */
    long count(ViewHyb viewHyb);
    /**
     * 统计总行数
     *
     * @param hyb 查询条件
     * @return 总行数
     */
    long querySyncInfoCount(ViewHyb hyb);

    /**
     * 查询指定行数据
     *
     * @param hyb      查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<ViewHyb> querySyncInfoByPage(@Param("hyb") ViewHyb hyb, @Param("pageable") Pageable pageable);
    /**
     * 新增数据
     *
     * @param viewHyb 实例对象
     * @return 影响行数
     */
    int insert(ViewHyb viewHyb);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ViewHyb> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ViewHyb> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ViewHyb> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ViewHyb> entities);

    /**
     * 修改数据
     *
     * @param viewHyb 实例对象
     * @return 影响行数
     */
    int update(ViewHyb viewHyb);

    /**
     * 通过主键删除数据
     *
     * @param studyid 主键
     * @return 影响行数
     */
    int deleteById(String studyid);

}

