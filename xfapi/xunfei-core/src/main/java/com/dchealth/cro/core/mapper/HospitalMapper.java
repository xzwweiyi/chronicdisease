package com.dchealth.cro.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dchealth.cro.core.domain.Hospital;
import com.dchealth.cro.core.vo.DictVO;

/**
 * 医院信息Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-13
 */
public interface HospitalMapper 
{
    /**
     * 查询医院信息
     * 
     * @param id 医院信息ID
     * @return 医院信息
     */
    public Hospital selectHospitalById(Integer id);

    /**
     * 查询医院信息列表
     * 
     * @param hospital 医院信息
     * @return 医院信息集合
     */
    public List<Hospital> selectHospitalList(Hospital hospital);

    /**
     * 新增医院信息
     * 
     * @param hospital 医院信息
     * @return 结果
     */
    public int insertHospital(Hospital hospital);

    /**
     * 修改医院信息
     * 
     * @param hospital 医院信息
     * @return 结果
     */
    public int updateHospital(Hospital hospital);

    /**
     * 删除医院信息
     * 
     * @param id 医院信息ID
     * @return 结果
     */
    public int deleteHospitalById(Integer id);

    /**
     * 批量删除医院信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHospitalByIds(String[] ids);
    
    public  List<DictVO> selectHospitalDepartment(@Param("tenantId")String tenantId);
    
    public  List<DictVO> selectHospital(@Param("tenantId")String tenantId);
    public  List<DictVO> selectHospitalByAccountId(@Param("tenantId")String tenantId,@Param("accountId")Long accountId);
}
