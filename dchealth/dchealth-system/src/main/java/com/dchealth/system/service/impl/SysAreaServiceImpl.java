package com.dchealth.system.service.impl;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.system.domain.SysArea;
import com.dchealth.system.mapper.SysAreaMapper;
import com.dchealth.system.service.ISysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 地区码Service业务层处理
 *
 * @author ruoyi
 * @date 2022-03-18
 */
@Service
public class SysAreaServiceImpl implements ISysAreaService {
    @Autowired
    private SysAreaMapper sysAreaMapper;

    /**
     * 查询地区码
     *
     * @param areaId 地区码主键
     * @return 地区码
     */
    @Override
    public SysArea selectSysAreaByAreaId(Long areaId) {
        return sysAreaMapper.selectSysAreaByAreaId(areaId);
    }

    /**
     * 查询地区码列表
     *
     * @param sysArea 地区码
     * @return 地区码
     */
    @Override
    public List<SysArea> selectSysAreaList(SysArea sysArea) {
        return sysAreaMapper.selectSysAreaList(sysArea);
    }

    /**
     * 新增地区码
     *
     * @param sysArea 地区码
     * @return 结果
     */
    @Override
    public int insertSysArea(SysArea sysArea) {
        sysArea.setCreateTime(DateUtils.getNowDate());
        return sysAreaMapper.insertSysArea(sysArea);
    }

    /**
     * 修改地区码
     *
     * @param sysArea 地区码
     * @return 结果
     */
    @Override
    public int updateSysArea(SysArea sysArea) {
        sysArea.setUpdateTime(DateUtils.getNowDate());
        return sysAreaMapper.updateSysArea(sysArea);
    }

    /**
     * 批量删除地区码
     *
     * @param areaIds 需要删除的地区码主键
     * @return 结果
     */
    @Override
    public int deleteSysAreaByAreaIds(Long[] areaIds) {
        return sysAreaMapper.deleteSysAreaByAreaIds(areaIds);
    }

    /**
     * 删除地区码信息
     *
     * @param areaId 地区码主键
     * @return 结果
     */
    @Override
    public int deleteSysAreaByAreaId(Long areaId) {
        return sysAreaMapper.deleteSysAreaByAreaId(areaId);
    }

    /**
     * 动态查询省市区--二三级数据查询
     *
     * @param areald
     * @return
     */
    @Override
    public List<SysArea> findAreaListByParentId(Long areald) {
        return sysAreaMapper.selectSysAreaListByParentId(areald);
    }

    /**
     * 查询地区码树列表
     *
     * @return 所有地区码信息
     */
    @Override
    public List<SysArea> selectSysAreaTree() {
        List<SysArea> sysAreaList = sysAreaMapper.selectSysAreaList(new SysArea());
        //获取国家
        List<SysArea> sysAreaListGuo = sysAreaList.stream().filter(item -> item.getParentId().longValue() == 0).collect(Collectors.toList());
        sysAreaListGuo.stream().forEach(oneItem ->
        {
            //一级省
            List<SysArea> sysAreaListPro = sysAreaList.stream().filter(twoItem -> twoItem.getParentId().longValue() == oneItem.getAreaId().longValue()).collect(Collectors.toList());
            sysAreaListPro.stream().forEach(twoItem -> {
                //二级市
                List<SysArea> sysAreaListCity = sysAreaList.stream().filter(threeItem -> threeItem.getParentId().longValue() == twoItem.getAreaId().longValue()).collect(Collectors.toList());
                sysAreaListCity.stream().forEach(threeItem -> {
                    //三级地区
                    List<SysArea> sysAreaListDis = sysAreaList.stream().filter(fourItem -> fourItem.getParentId().longValue() == threeItem.getAreaId().longValue()).collect(Collectors.toList());
                    threeItem.setChild(sysAreaListDis);
                });
                twoItem.setChild(sysAreaListCity);
            });
            oneItem.setChild(sysAreaListPro);
        });
        return sysAreaListGuo;
    }
}
