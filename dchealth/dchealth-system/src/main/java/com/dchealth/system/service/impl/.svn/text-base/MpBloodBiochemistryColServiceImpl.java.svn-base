package com.dchealth.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.system.mapper.MpBloodBiochemistryColMapper;
import com.dchealth.system.domain.MpBloodBiochemistryCol;
import com.dchealth.system.service.IMpBloodBiochemistryColService;

/**
 * 肝or肾Service业务层处理
 * 
 * @author lchbu
 * @date 2024-07-04
 */
@Service
public class MpBloodBiochemistryColServiceImpl implements IMpBloodBiochemistryColService 
{
    @Autowired
    private MpBloodBiochemistryColMapper mpBloodBiochemistryColMapper;

    /**
     * 查询肝or肾
     * 
     * @param id 肝or肾主键
     * @return 肝or肾
     */
    @Override
    public MpBloodBiochemistryCol selectMpBloodBiochemistryColById(Long id)
    {
        return mpBloodBiochemistryColMapper.selectMpBloodBiochemistryColById(id);
    }

    /**
     * 查询肝or肾列表
     * 
     * @param mpBloodBiochemistryCol 肝or肾
     * @return 肝or肾
     */
    @Override
    public List<MpBloodBiochemistryCol> selectMpBloodBiochemistryColList(MpBloodBiochemistryCol mpBloodBiochemistryCol)
    {
        return mpBloodBiochemistryColMapper.selectMpBloodBiochemistryColList(mpBloodBiochemistryCol);
    }

    /**
     * 新增肝or肾
     * 
     * @param mpBloodBiochemistryCol 肝or肾
     * @return 结果
     */
    @Override
    public int insertMpBloodBiochemistryCol(MpBloodBiochemistryCol mpBloodBiochemistryCol)
    {
        return mpBloodBiochemistryColMapper.insertMpBloodBiochemistryCol(mpBloodBiochemistryCol);
    }

    /**
     * 修改肝or肾
     * 
     * @param mpBloodBiochemistryCol 肝or肾
     * @return 结果
     */
    @Override
    public int updateMpBloodBiochemistryCol(MpBloodBiochemistryCol mpBloodBiochemistryCol)
    {
        return mpBloodBiochemistryColMapper.updateMpBloodBiochemistryCol(mpBloodBiochemistryCol);
    }

    /**
     * 批量删除肝or肾
     * 
     * @param ids 需要删除的肝or肾主键
     * @return 结果
     */
    @Override
    public int deleteMpBloodBiochemistryColByIds(Long[] ids)
    {
        return mpBloodBiochemistryColMapper.deleteMpBloodBiochemistryColByIds(ids);
    }

    /**
     * 删除肝or肾信息
     * 
     * @param id 肝or肾主键
     * @return 结果
     */
    @Override
    public int deleteMpBloodBiochemistryColById(Long id)
    {
        return mpBloodBiochemistryColMapper.deleteMpBloodBiochemistryColById(id);
    }
}
