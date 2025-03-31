package com.dchealth.template.service;

import java.util.List;

import com.dchealth.template.domain.FLibraryForm;
import com.dchealth.template.domain.FLibraryProperty;

/**
 * 属性库属性Service接口
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
public interface IFLibraryPropertyService 
{
    /**
     * 查询属性库属性
     * 
     * @param id 属性库属性主键
     * @return 属性库属性
     */
    public FLibraryProperty selectFLibraryPropertyById(Long id);

    /**
     * 查询属性库属性列表
     * 
     * @param fLibraryProperty 属性库属性
     * @return 属性库属性集合
     */
    public List<FLibraryProperty> selectFLibraryPropertyList(FLibraryProperty fLibraryProperty);

    /**
     * 新增属性库属性
     * 
     * @param fLibraryProperty 属性库属性
     * @return 结果
     */
    public int insertFLibraryProperty(FLibraryProperty fLibraryProperty);
    public int insertFLibraryProperty(FLibraryForm fLibraryForm);

    /**
     * 修改属性库属性
     * 
     * @param fLibraryProperty 属性库属性
     * @return 结果
     */
    public int updateFLibraryProperty(FLibraryProperty fLibraryProperty);

    public int updateFLibraryProperty(FLibraryForm fLibraryForm);

    /**
     * 批量删除属性库属性
     * 
     * @param ids 需要删除的属性库属性主键集合
     * @return 结果
     */
    public int deleteFLibraryPropertyByIds(Long[] ids);

    /**
     * 删除属性库属性信息
     * 
     * @param id 属性库属性主键
     * @return 结果
     */
    public int deleteFLibraryPropertyById(Long id);
}
