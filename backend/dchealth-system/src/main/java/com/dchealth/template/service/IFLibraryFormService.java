package com.dchealth.template.service;

import java.util.List;
import com.dchealth.template.domain.FLibraryForm;

/**
 * 属性库单Service接口
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
public interface IFLibraryFormService 
{
    /**
     * 查询属性库单
     * 
     * @param libraryId 属性库单主键
     * @return 属性库单
     */
    public FLibraryForm selectFLibraryFormByLibraryId(Long libraryId);

    /**
     * 表单库子表单及属性
     * @param libraryId
     * @return
     */
    public FLibraryForm childFormAndProperty(Long libraryId,String formNameSearch);

    /**
     * 查询属性库单列表
     * 
     * @param fLibraryForm 属性库单
     * @return 属性库单集合
     */
    public List<FLibraryForm> selectFLibraryFormList(FLibraryForm fLibraryForm);

    /**
     * 新增属性库单
     * 
     * @param fLibraryForm 属性库单
     * @return 结果
     */
    public int insertFLibraryForm(FLibraryForm fLibraryForm);

    /**
     * 修改属性库单
     * 
     * @param fLibraryForm 属性库单
     * @return 结果
     */
    public int updateFLibraryForm(FLibraryForm fLibraryForm);

    /**
     * 批量删除属性库单
     * 
     * @param libraryIds 需要删除的属性库单主键集合
     * @return 结果
     */
    public int deleteFLibraryFormByLibraryIds(Long[] libraryIds);

    public int updateDelFlagByLibraryIds(Long[] libraryIds);

    /**
     * 删除属性库单信息
     * 
     * @param libraryId 属性库单主键
     * @return 结果
     */
    public int deleteFLibraryFormByLibraryId(Long libraryId);
}
