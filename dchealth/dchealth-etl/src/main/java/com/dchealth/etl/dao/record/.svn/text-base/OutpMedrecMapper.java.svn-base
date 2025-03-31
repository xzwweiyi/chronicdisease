package com.dchealth.etl.dao.record;

import com.dchealth.etl.domain.record.Orders;
import com.dchealth.etl.domain.record.OutpMedrec;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 门诊诊断表(OutpMedrec)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-18 15:33:58
 */
public interface OutpMedrecMapper {


    /**
     * 查询指定行数据
     *
     * @param outpMedrec 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<OutpMedrec> queryAllByLimit(OutpMedrec outpMedrec, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param outpMedrec 查询条件
     * @return 总行数
     */
    long count(OutpMedrec outpMedrec);

    /**
     * 新增数据
     *
     * @param outpMedrec 实例对象
     * @return 影响行数
     */
    int insert(OutpMedrec outpMedrec);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<OutpMedrec> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<OutpMedrec> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<OutpMedrec> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<OutpMedrec> entities);

    /**
     * 修改数据
     *
     * @param outpMedrec 实例对象
     * @return 影响行数
     */
    int update(OutpMedrec outpMedrec);

    /**
     *查询当天同步数据量
     * @param outpMedrec
     * @return
     */
    int querySyncInfoCount(OutpMedrec outpMedrec);
    /**
     * 查询指定行数据
     *
     * @param outpMedrec 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<OutpMedrec> querySyncInfoByPage(@Param("outpMedrec") OutpMedrec outpMedrec, @Param("pageable") Pageable pageable);


}

