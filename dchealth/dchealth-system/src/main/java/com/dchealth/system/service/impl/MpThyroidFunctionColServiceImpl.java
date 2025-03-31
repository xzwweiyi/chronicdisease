package com.dchealth.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.system.mapper.MpThyroidFunctionColMapper;
import com.dchealth.system.domain.MpThyroidFunctionCol;
import com.dchealth.system.service.IMpThyroidFunctionColService;

/**
 * 甲功sService业务层处理
 * 
 * @author lchbu
 * @date 2024-07-05
 */
@Service
public class MpThyroidFunctionColServiceImpl implements IMpThyroidFunctionColService 
{
    @Autowired
    private MpThyroidFunctionColMapper mpThyroidFunctionColMapper;

    /**
     * 查询甲功s
     * 
     * @param id 甲功s主键
     * @return 甲功s
     */
    @Override
    public MpThyroidFunctionCol selectMpThyroidFunctionColById(Long id)
    {
        return mpThyroidFunctionColMapper.selectMpThyroidFunctionColById(id);
    }

    /**
     * 查询甲功s列表
     * 
     * @param mpThyroidFunctionCol 甲功s
     * @return 甲功s
     */
    @Override
    public List<MpThyroidFunctionCol> selectMpThyroidFunctionColList(MpThyroidFunctionCol mpThyroidFunctionCol)
    {
        return mpThyroidFunctionColMapper.selectMpThyroidFunctionColList(mpThyroidFunctionCol);
    }

    /**
     * 新增甲功s
     * 
     * @param mpThyroidFunctionCol 甲功s
     * @return 结果
     */
    @Override
    public int insertMpThyroidFunctionCol(MpThyroidFunctionCol mpThyroidFunctionCol)
    {
        return mpThyroidFunctionColMapper.insertMpThyroidFunctionCol(mpThyroidFunctionCol);
    }

    /**
     * 修改甲功s
     * 
     * @param mpThyroidFunctionCol 甲功s
     * @return 结果
     */
    @Override
    public int updateMpThyroidFunctionCol(MpThyroidFunctionCol mpThyroidFunctionCol)
    {
        return mpThyroidFunctionColMapper.updateMpThyroidFunctionCol(mpThyroidFunctionCol);
    }

    /**
     * 批量删除甲功s
     * 
     * @param ids 需要删除的甲功s主键
     * @return 结果
     */
    @Override
    public int deleteMpThyroidFunctionColByIds(Long[] ids)
    {
        return mpThyroidFunctionColMapper.deleteMpThyroidFunctionColByIds(ids);
    }

    /**
     * 删除甲功s信息
     * 
     * @param id 甲功s主键
     * @return 结果
     */
    @Override
    public int deleteMpThyroidFunctionColById(Long id)
    {
        return mpThyroidFunctionColMapper.deleteMpThyroidFunctionColById(id);
    }
}
