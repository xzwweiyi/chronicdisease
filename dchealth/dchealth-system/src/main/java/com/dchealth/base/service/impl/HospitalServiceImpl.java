package com.dchealth.base.service.impl;

import java.util.List;

import com.dchealth.base.domain.Hospital;
import com.dchealth.base.mapper.HospitalMapper;
import com.dchealth.base.service.IHospitalService;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 机构管理Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class HospitalServiceImpl implements IHospitalService
{
    @Autowired
    private HospitalMapper hospitalMapper;

    @Override
    public Hospital selectHospitalById(Long id)
    {
        return hospitalMapper.selectHospitalById(id);
    }

    @Override
    public List<Hospital> selectHospitalList(Hospital hospital)
    {
        return hospitalMapper.selectHospitalList(hospital);
    }

    @Override
    public int insertHospital(Hospital hospital)
    {
        hospital.setCreateTime(DateUtils.getNowDate());
        return hospitalMapper.insertHospital(hospital);
    }

    @Override
    public int updateHospital(Hospital hospital)
    {
        hospital.setUpdateTime(DateUtils.getNowDate());
        return hospitalMapper.updateHospital(hospital);
    }

    @Override
    public int deleteHospitalByIds(Long[] ids)
    {
        return hospitalMapper.deleteHospitalByIds(ids);
    }

    @Override
    public int deleteHospitalById(Long id)
    {
        return hospitalMapper.deleteHospitalById(id);
    }
}
