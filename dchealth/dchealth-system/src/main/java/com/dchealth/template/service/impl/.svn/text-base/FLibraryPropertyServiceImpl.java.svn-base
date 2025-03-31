package com.dchealth.template.service.impl;

import java.util.Date;
import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.template.domain.FLibraryForm;
import com.dchealth.template.mapper.FLibraryFormMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.template.mapper.FLibraryPropertyMapper;
import com.dchealth.template.domain.FLibraryProperty;
import com.dchealth.template.service.IFLibraryPropertyService;

/**
 * 属性库属性Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@Service
public class FLibraryPropertyServiceImpl implements IFLibraryPropertyService 
{
    @Autowired
    private FLibraryPropertyMapper fLibraryPropertyMapper;
    @Autowired
    private FLibraryFormMapper fLibraryFormMapper;

    /**
     * 查询属性库属性
     * 
     * @param id 属性库属性主键
     * @return 属性库属性
     */
    @Override
    public FLibraryProperty selectFLibraryPropertyById(Long id)
    {
        return fLibraryPropertyMapper.selectFLibraryPropertyById(id);
    }

    /**
     * 查询属性库属性列表
     * 
     * @param fLibraryProperty 属性库属性
     * @return 属性库属性
     */
    @Override
    public List<FLibraryProperty> selectFLibraryPropertyList(FLibraryProperty fLibraryProperty)
    {
        return fLibraryPropertyMapper.selectFLibraryPropertyList(fLibraryProperty);
    }

    /**
     * 新增属性库属性
     * 
     * @param fLibraryProperty 属性库属性
     * @return 结果
     */
    @Override
    public int insertFLibraryProperty(FLibraryProperty fLibraryProperty)
    {
        fLibraryProperty.setCreateTime(DateUtils.getNowDate());
        return fLibraryPropertyMapper.insertFLibraryProperty(fLibraryProperty);
    }

    @Override
    public int insertFLibraryProperty(FLibraryForm fLibraryForm) {
        Date date = DateUtils.getNowDate();
        if (fLibraryForm.getLibraryProperties() != null && !fLibraryForm.getLibraryProperties().isEmpty()){
            fLibraryForm.getLibraryProperties().forEach(x->{
                x.setCreateTime(date);
                fLibraryPropertyMapper.insertFLibraryProperty(x);
            });
        }
        if (fLibraryForm.getLibraryForms() != null && !fLibraryForm.getLibraryForms().isEmpty()){
            fLibraryForm.getLibraryForms().forEach(x->{
                x.setCreateTime(date);
                x.setParentId(fLibraryForm.getLibraryId());
                fLibraryFormMapper.insertFLibraryForm(x);
                if (x.getLibraryProperties() != null && !x.getLibraryProperties().isEmpty()){
                    x.getLibraryProperties().forEach(y->{
                        y.setLibraryId(x.getLibraryId());
                    });
                }
                insertFLibraryProperty(x);
            });
        }
        return 1;
    }

    /**
     * 修改属性库属性
     * 
     * @param fLibraryProperty 属性库属性
     * @return 结果
     */
    @Override
    public int updateFLibraryProperty(FLibraryProperty fLibraryProperty)
    {
        fLibraryProperty.setUpdateTime(DateUtils.getNowDate());
        return fLibraryPropertyMapper.updateFLibraryProperty(fLibraryProperty);
    }

    private int delete(List<FLibraryForm> fLibraryForms){
        if (fLibraryForms != null && !fLibraryForms.isEmpty()){
            fLibraryForms.forEach(x->{
                FLibraryForm params = new FLibraryForm();
                params.setParentId(x.getLibraryId());
                List<FLibraryForm> childrens = fLibraryFormMapper.selectFLibraryFormList(params);
                if (childrens != null && !childrens.isEmpty()){
                    delete(childrens);
                }
                fLibraryPropertyMapper.deleteFLibraryPropertyByLibraryId(x.getLibraryId());
                fLibraryFormMapper.deleteFLibraryFormByLibraryId(x.getLibraryId());
            });
        }


        return 1;
    }

    @Override
    public int updateFLibraryProperty(FLibraryForm fLibraryForm) {
        if(fLibraryForm.getScoresTotalVal()!=null){
            FLibraryForm f = fLibraryFormMapper.selectFLibraryFormByLibraryId(fLibraryForm.getLibraryId());
            f.setScoresTotalVal(fLibraryForm.getScoresTotalVal());
            fLibraryFormMapper.updateFLibraryForm(f);
        }
        FLibraryForm params = new FLibraryForm();
        params.setParentId(fLibraryForm.getLibraryId());
        List<FLibraryForm> fLibraryForms = fLibraryFormMapper.selectFLibraryFormList(params);
        delete(fLibraryForms);
        fLibraryPropertyMapper.deleteFLibraryPropertyByLibraryId(fLibraryForm.getLibraryId());
        insertFLibraryProperty(fLibraryForm);
        return 1;
    }

    /**
     * 批量删除属性库属性
     * 
     * @param ids 需要删除的属性库属性主键
     * @return 结果
     */
    @Override
    public int deleteFLibraryPropertyByIds(Long[] ids)
    {
        return fLibraryPropertyMapper.deleteFLibraryPropertyByIds(ids);
    }

    /**
     * 删除属性库属性信息
     * 
     * @param id 属性库属性主键
     * @return 结果
     */
    @Override
    public int deleteFLibraryPropertyById(Long id)
    {
        return fLibraryPropertyMapper.deleteFLibraryPropertyById(id);
    }
}
