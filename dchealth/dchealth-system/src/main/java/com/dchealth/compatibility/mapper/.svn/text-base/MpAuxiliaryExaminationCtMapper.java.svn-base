package com.dchealth.compatibility.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpAuxiliaryExaminationCt;
import com.dchealth.medical.dto.PersonCheckItemDto;

/**
 * 唐都-辅助检查-ct项目相关Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpAuxiliaryExaminationCtMapper 
{
    /**
     * 查询唐都-辅助检查-ct项目相关
     * 
     * @param id 唐都-辅助检查-ct项目相关主键
     * @return 唐都-辅助检查-ct项目相关
     */
    public MpAuxiliaryExaminationCt selectMpAuxiliaryExaminationCtById(Long id);

    /**
     * 查询唐都-辅助检查-ct项目相关列表
     * 
     * @param mpAuxiliaryExaminationCt 唐都-辅助检查-ct项目相关
     * @return 唐都-辅助检查-ct项目相关集合
     */
    public List<MpAuxiliaryExaminationCt> selectMpAuxiliaryExaminationCtList(MpAuxiliaryExaminationCt mpAuxiliaryExaminationCt);

    /**
     * 新增唐都-辅助检查-ct项目相关
     * 
     * @param mpAuxiliaryExaminationCt 唐都-辅助检查-ct项目相关
     * @return 结果
     */
    public int insertMpAuxiliaryExaminationCt(MpAuxiliaryExaminationCt mpAuxiliaryExaminationCt);

    /**
     * 修改唐都-辅助检查-ct项目相关
     * 
     * @param mpAuxiliaryExaminationCt 唐都-辅助检查-ct项目相关
     * @return 结果
     */
    public int updateMpAuxiliaryExaminationCt(MpAuxiliaryExaminationCt mpAuxiliaryExaminationCt);

    /**
     * 删除唐都-辅助检查-ct项目相关
     * 
     * @param id 唐都-辅助检查-ct项目相关主键
     * @return 结果
     */
    public int deleteMpAuxiliaryExaminationCtById(Long id);

    /**
     * 批量删除唐都-辅助检查-ct项目相关
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMpAuxiliaryExaminationCtByIds(String[] ids);

    MpAuxiliaryExaminationCt queryAuxiliaryExaminationCtByItem(@Param("dto") PersonCheckItemDto dto, @Param("itemName") String itemName);

}
