package com.dchealth.cro.core.mapper;

import java.util.List;

import com.dchealth.cro.core.domain.SysArea;




/**
 * 地区码Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-01-06
 */
public interface SysAreaMapper 
{
    /**
     * 查询地区码
     * 
     * @param areaId 地区码ID
     * @return 地区码
     */
    public SysArea selectSysAreaById(Integer areaId);

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
     * @param areaId 地区码ID
     * @return 结果
     */
    public int deleteSysAreaById(Integer areaId);

    /**
     * 批量删除地区码
     * 
     * @param areaIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysAreaByIds(String[] areaIds);
}
