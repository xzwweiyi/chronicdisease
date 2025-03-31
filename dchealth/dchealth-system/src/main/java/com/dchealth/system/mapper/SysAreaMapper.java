package com.dchealth.system.mapper;

import java.util.List;
import com.dchealth.system.domain.SysArea;
import com.dchealth.system.domain.vo.SysAreaVo;

/**
 * 地区码Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-18
 */
public interface SysAreaMapper 
{
    /**
     * 查询地区码
     * 
     * @param areaId 地区码主键
     * @return 地区码
     */
    public SysArea selectSysAreaByAreaId(Long areaId);

    /**
     * 查询地区码列表
     * 
     * @param sysArea 地区码
     * @return 地区码集合
     */
    public List<SysArea> selectSysAreaList(SysArea sysArea);

    /**
     * 新增地区码
     * 
     * @param sysArea 地区码
     * @return 结果
     */
    public int insertSysArea(SysArea sysArea);

    /**
     * 修改地区码
     * 
     * @param sysArea 地区码
     * @return 结果
     */
    public int updateSysArea(SysArea sysArea);

    /**
     * 删除地区码
     * 
     * @param areaId 地区码主键
     * @return 结果
     */
    public int deleteSysAreaByAreaId(Long areaId);

    /**
     * 批量删除地区码
     * 
     * @param areaIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAreaByAreaIds(Long[] areaIds);

    /**
     * 动态查询省市区--二三级数据查询
     * @param areald
     * @return
     */
    List<SysArea> selectSysAreaListByParentId(Long areald);

    /**
     * 查询3级地区信息
     * @return
     */
    List<SysAreaVo> selectAreaFullNameList();
}
