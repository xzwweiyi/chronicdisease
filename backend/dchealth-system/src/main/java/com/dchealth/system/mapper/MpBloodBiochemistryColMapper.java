package com.dchealth.system.mapper;

import java.util.List;
import com.dchealth.system.domain.MpBloodBiochemistryCol;

/**
 * 肝or肾Mapper接口
 * 
 * @author lchbu
 * @date 2024-07-04
 */
public interface MpBloodBiochemistryColMapper 
{
    /**
     * 查询肝or肾
     * 
     * @param id 肝or肾主键
     * @return 肝or肾
     */
    public MpBloodBiochemistryCol selectMpBloodBiochemistryColById(Long id);

    /**
     * 查询肝or肾列表
     * 
     * @param mpBloodBiochemistryCol 肝or肾
     * @return 肝or肾集合
     */
    public List<MpBloodBiochemistryCol> selectMpBloodBiochemistryColList(MpBloodBiochemistryCol mpBloodBiochemistryCol);

    /**
     * 新增肝or肾
     * 
     * @param mpBloodBiochemistryCol 肝or肾
     * @return 结果
     */
    public int insertMpBloodBiochemistryCol(MpBloodBiochemistryCol mpBloodBiochemistryCol);

    /**
     * 修改肝or肾
     * 
     * @param mpBloodBiochemistryCol 肝or肾
     * @return 结果
     */
    public int updateMpBloodBiochemistryCol(MpBloodBiochemistryCol mpBloodBiochemistryCol);

    /**
     * 删除肝or肾
     * 
     * @param id 肝or肾主键
     * @return 结果
     */
    public int deleteMpBloodBiochemistryColById(Long id);

    /**
     * 批量删除肝or肾
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMpBloodBiochemistryColByIds(Long[] ids);
}
