package com.dchealth.template.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.template.domain.FLibraryProperty;
import com.dchealth.template.mapper.FLibraryPropertyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.template.mapper.FLibraryFormMapper;
import com.dchealth.template.domain.FLibraryForm;
import com.dchealth.template.service.IFLibraryFormService;

/**
 * 属性库单Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@Service
public class FLibraryFormServiceImpl implements IFLibraryFormService 
{
    @Autowired
    private FLibraryFormMapper fLibraryFormMapper;
    @Autowired
    private FLibraryPropertyMapper fLibraryPropertyMapper;

    /**
     * 查询属性库单
     * 
     * @param libraryId 属性库单主键
     * @return 属性库单
     */
    @Override
    public FLibraryForm selectFLibraryFormByLibraryId(Long libraryId)
    {
        return fLibraryFormMapper.selectFLibraryFormByLibraryId(libraryId);
    }

    @Override
    public FLibraryForm childFormAndProperty(Long libraryId,String formNameSearch) {
        FLibraryForm fLibraryForm = fLibraryFormMapper.selectFLibraryFormByLibraryId(libraryId);
        if (fLibraryForm != null){
            FLibraryForm params = new FLibraryForm();
            params.setParentId(libraryId);
            params.setFormNameSearch2(formNameSearch);
            List<FLibraryForm> fLibraryForms = fLibraryFormMapper.selectFLibraryFormList(params);
            fLibraryForm.setLibraryForms(children(fLibraryForms,formNameSearch));
            FLibraryProperty property = new FLibraryProperty();
            property.setLibraryId(libraryId);
            List<FLibraryProperty> fLibraryProperties = fLibraryPropertyMapper.selectFLibraryPropertyList(property);

            fLibraryForm.setLibraryProperties(fLibraryProperties);
        }

        return fLibraryForm;
    }

    /**
     * 获取字表单
     * @param fLibraryForms
     * @return
     */
    private List<FLibraryForm> children(List<FLibraryForm> fLibraryForms,String formNameSearch){
        if (fLibraryForms != null && !fLibraryForms.isEmpty()){
            fLibraryForms.forEach(x->{
                FLibraryForm params = new FLibraryForm();
                params.setParentId(x.getLibraryId());
                List<FLibraryForm> childrens = fLibraryFormMapper.selectFLibraryFormList(params);
                if (childrens != null && !childrens.isEmpty()){
                    x.setLibraryForms(children(childrens,formNameSearch));
                }
                FLibraryProperty property = new FLibraryProperty();
                property.setLibraryId(x.getLibraryId());
                property.setPropertyName(formNameSearch);
                List<FLibraryProperty> fLibraryProperties = fLibraryPropertyMapper.selectFLibraryPropertyList(property);
                fLibraryProperties.forEach(item->{
                    item.setId(null);
                });
                x.setLibraryProperties(fLibraryProperties);
            });
        }
        return fLibraryForms;
    }

    /**
     * 查询属性库单列表
     * 
     * @param fLibraryForm 属性库单
     * @return 属性库单
     */
    @Override
    public List<FLibraryForm> selectFLibraryFormList(FLibraryForm fLibraryForm)
    {
        return fLibraryFormMapper.selectFLibraryFormList(fLibraryForm);
    }

    /**
     * 新增属性库单
     * 
     * @param fLibraryForm 属性库单
     * @return 结果
     */
    @Override
    public int insertFLibraryForm(FLibraryForm fLibraryForm)
    {
        fLibraryForm.setCreateTime(DateUtils.getNowDate());
        return fLibraryFormMapper.insertFLibraryForm(fLibraryForm);
    }

    /**
     * 修改属性库单
     * 
     * @param fLibraryForm 属性库单
     * @return 结果
     */
    @Override
    public int updateFLibraryForm(FLibraryForm fLibraryForm)
    {
        fLibraryForm.setUpdateTime(DateUtils.getNowDate());
        return fLibraryFormMapper.updateFLibraryForm(fLibraryForm);
    }

    /**
     * 批量删除属性库单
     * 
     * @param libraryIds 需要删除的属性库单主键
     * @return 结果
     */
    @Override
    public int deleteFLibraryFormByLibraryIds(Long[] libraryIds)
    {
        return fLibraryFormMapper.deleteFLibraryFormByLibraryIds(libraryIds);
    }

    @Override
    public int updateDelFlagByLibraryIds(Long[] libraryIds) {
        return fLibraryFormMapper.updateDelFlagByLibraryIds(libraryIds);
    }

    /**
     * 删除属性库单信息
     * 
     * @param libraryId 属性库单主键
     * @return 结果
     */
    @Override
    public int deleteFLibraryFormByLibraryId(Long libraryId)
    {
        return fLibraryFormMapper.deleteFLibraryFormByLibraryId(libraryId);
    }
}
