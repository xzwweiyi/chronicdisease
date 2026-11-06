package com.dchealth.base.mapper;

import com.dchealth.base.domain.Hospital;

import java.util.List;

/**
 * 机构管理Mapper接口
 *
 * @author ruoyi
 */
public interface HospitalMapper
{
    /**
     * 查询机构管理
     *
     * @param id 机构管理主键
     * @return 机构管理
     */
    public Hospital selectHospitalById(Long id);

    /**
     * 查询机构管理列表
     *
     * @param hospital 机构管理
     * @return 机构管理集合
     */
    public List<Hospital> selectHospitalList(Hospital hospital);

    /**
     * 新增机构管理
     *
     * @param hospital 机构管理
     * @return 结果
     */
    public int insertHospital(Hospital hospital);

    /**
     * 修改机构管理
     *
     * @param hospital 机构管理
     * @return 结果
     */
    public int updateHospital(Hospital hospital);

    /**
     * 删除机构管理
     *
     * @param id 机构管理主键
     * @return 结果
     */
    public int deleteHospitalById(Long id);

    /**
     * 批量删除机构管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHospitalByIds(Long[] ids);
}
