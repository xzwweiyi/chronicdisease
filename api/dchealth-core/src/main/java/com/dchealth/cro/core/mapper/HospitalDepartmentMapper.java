package com.dchealth.cro.core.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.core.domain.HospitalDepartment;

/**
 * 科室管理Mapper接口
 * 
 * @author genemax1107@aliyun.como
 * @date 2020-01-06
 */
@Repository
public interface HospitalDepartmentMapper 
{
    /**
     * 查询科室管理
     * 
     * @param id 科室管理ID
     * @return 科室管理
     */
    public HospitalDepartment selectHospitalDepartmentById(Integer id);

    /**
     * 查询科室管理列表
     * 
     * @param hospitalDepartment 科室管理
     * @return 科室管理集合
     */
    public List<HospitalDepartment> selectHospitalDepartmentList(HospitalDepartment hospitalDepartment);

    /**
     * 新增科室管理
     * 
     * @param hospitalDepartment 科室管理
     * @return 结果
     */
    public int insertHospitalDepartment(HospitalDepartment hospitalDepartment);

    /**
     * 修改科室管理
     * 
     * @param hospitalDepartment 科室管理
     * @return 结果
     */
    public int updateHospitalDepartment(HospitalDepartment hospitalDepartment);

    /**
     * 删除科室管理
     * 
     * @param id 科室管理ID
     * @return 结果
     */
    public int deleteHospitalDepartmentById(Integer id);

    /**
     * 批量删除科室管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHospitalDepartmentByIds(String[] ids);
}
