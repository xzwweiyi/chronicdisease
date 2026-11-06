package com.dchealth.resident.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dchealth.resident.domain.ResidentGradingRecord;
import com.dchealth.resident.mapper.ResidentGradingMapper;
import com.dchealth.resident.service.IResidentGradingService;

/**
 * 分级评估Service业务层处理
 */
@Service
public class ResidentGradingServiceImpl implements IResidentGradingService {

    @Autowired
    private ResidentGradingMapper gradingMapper;

    @Override
    public List<ResidentGradingRecord> selectGradingRecordList(ResidentGradingRecord record) {
        return gradingMapper.selectGradingRecordList(record);
    }

    @Override
    public List<ResidentGradingRecord> selectGradingRecordByPersonId(String personId) {
        return gradingMapper.selectGradingRecordByPersonId(personId);
    }

    @Override
    public ResidentGradingRecord selectGradingRecordById(Long id) {
        return gradingMapper.selectGradingRecordById(id);
    }

    @Override
    public int insertGradingRecord(ResidentGradingRecord record) {
        return gradingMapper.insertGradingRecord(record);
    }

    @Override
    public int updateGradingRecord(ResidentGradingRecord record) {
        return gradingMapper.updateGradingRecord(record);
    }

    @Override
    public int deleteGradingRecordById(Long id) {
        return gradingMapper.deleteGradingRecordById(id);
    }

    @Override
    public int deleteGradingRecordByIds(Long[] ids) {
        return gradingMapper.deleteGradingRecordByIds(ids);
    }
}
