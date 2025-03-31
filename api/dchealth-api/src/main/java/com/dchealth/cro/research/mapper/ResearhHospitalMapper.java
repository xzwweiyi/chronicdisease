package com.dchealth.cro.research.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.research.domain.ResearhHospital;

/**
 * 队列参与中心信息Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Repository
public interface ResearhHospitalMapper 
{
    /**
     * 查询队列参与中心信息
     * 
     * @param id 队列参与中心信息主键
     * @return 队列参与中心信息
     */
    public ResearhHospital selectResearhHospitalById(Long id);

    /**
     * 查询队列参与中心信息列表
     * 
     * @param researhHospital 队列参与中心信息
     * @return 队列参与中心信息集合
     */
    public List<ResearhHospital> selectResearhHospitalList(ResearhHospital researhHospital);

    /**
     * 新增队列参与中心信息
     * 
     * @param researhHospital 队列参与中心信息
     * @return 结果
     */
    public int insertResearhHospital(ResearhHospital researhHospital);

    /**
     * 修改队列参与中心信息
     * 
     * @param researhHospital 队列参与中心信息
     * @return 结果
     */
    public int updateResearhHospital(ResearhHospital researhHospital);

    /**
     * 删除队列参与中心信息
     * 
     * @param id 队列参与中心信息主键
     * @return 结果
     */
    public int deleteResearhHospitalById(Long id);

    /**
     * 批量删除队列参与中心信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearhHospitalByIds(String[] ids);
}
