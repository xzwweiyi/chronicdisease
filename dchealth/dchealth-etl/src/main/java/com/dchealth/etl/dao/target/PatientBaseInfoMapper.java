package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.PatientBaseInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 患者基础表(PatientBaseInfo)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-27 15:03:35
 */
public interface PatientBaseInfoMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    PatientBaseInfo queryById(Long id);

    PatientBaseInfo queryByIdCard(String idCard);
    /**
     * 查询指定行数据
     *
     * @param patientBaseInfo 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<PatientBaseInfo> queryAllByLimit(PatientBaseInfo patientBaseInfo, @Param("pageable") Pageable pageable);
    /**
     * 查询指定行数据
     *
     * @param patientBaseInfo 查询条件
     * @return 对象列表
     */
    List<PatientBaseInfo> queryByPage(PatientBaseInfo patientBaseInfo);
    /**
     * 统计总行数
     *
     * @param patientBaseInfo 查询条件
     * @return 总行数
     */
    long count(PatientBaseInfo patientBaseInfo);

    /**
     * 新增数据
     *
     * @param patientBaseInfo 实例对象
     * @return 影响行数
     */
    int insert(PatientBaseInfo patientBaseInfo);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<PatientBaseInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<PatientBaseInfo> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<PatientBaseInfo> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<PatientBaseInfo> entities);

    /**
     * 修改数据
     *
     * @param patientBaseInfo 实例对象
     * @return 影响行数
     */
    int update(PatientBaseInfo patientBaseInfo);
    public int updateBatch(List<PatientBaseInfo> list);
    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

