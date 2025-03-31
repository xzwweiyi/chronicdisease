package com.dchealth.cro.form.mapper;

import com.dchealth.cro.form.domain.FMedicalProperty;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 属性Mapper接口
 * 
 * @author ruoyi
 * @date 2022-09-09
 */
@Repository
public interface FMedicalPropertyMapper 
{
    /**
     * 查询属性
     * 
     * @param id 属性主键
     * @return 属性
     */
    public FMedicalProperty selectFMedicalPropertyById(Long id);

    /**
     * 根据随访id获取属性
     * @param followId
     * @return
     */
    public List<FMedicalProperty> selectFMedicalPropertyByFollowId(Long followId);

    /**
     * 查询属性列表
     * 
     * @param fMedicalProperty 属性
     * @return 属性集合
     */
    public List<FMedicalProperty> selectFMedicalPropertyList(FMedicalProperty fMedicalProperty);

    /**
     * 新增属性
     * 
     * @param fMedicalProperty 属性
     * @return 结果
     */
    public int insertFMedicalProperty(FMedicalProperty fMedicalProperty);

    /**
     * 修改属性
     * 
     * @param fMedicalProperty 属性
     * @return 结果
     */
    public int updateFMedicalProperty(FMedicalProperty fMedicalProperty);

    /**
     * 删除属性
     * 
     * @param id 属性主键
     * @return 结果
     */
    public int deleteFMedicalPropertyById(Long id);

    /**
     * 批量删除属性
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFMedicalPropertyByIds(Long[] ids);
}
