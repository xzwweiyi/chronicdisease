package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.PatientBaseInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 患者基础表(PatientBaseInfo)表服务接口
 *
 * @author makejava
 * @since 2023-03-27 15:03:36
 */
public interface PatientBaseInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    PatientBaseInfo queryById(Long id);
    /**
     * 通过idCard查询单条数据
     *
     * @param idCard
     * @return 实例对象
     */
    PatientBaseInfo queryByIdCard(String idCard);


    /**
     * 分页查询
     *
     * @param patientBaseInfo 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<PatientBaseInfo> queryByPage(PatientBaseInfo patientBaseInfo, PageRequest pageRequest);

    List<PatientBaseInfo> queryByPage(PatientBaseInfo patientBaseInfo);
    /**
     * 新增数据
     *
     * @param patientBaseInfo 实例对象
     * @return 实例对象
     */
    PatientBaseInfo insert(PatientBaseInfo patientBaseInfo);
    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<PatientBaseInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<PatientBaseInfo> entities);
    /**
     * 修改数据
     *
     * @param patientBaseInfo 实例对象
     * @return 实例对象
     */
    PatientBaseInfo update(PatientBaseInfo patientBaseInfo);

    int updateBatch(List<PatientBaseInfo> list);
    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
