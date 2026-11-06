package com.dchealth.resident.service;

import java.util.List;
import com.dchealth.resident.domain.ResidentGradingRecord;

/**
 * 分级评估Service接口
 */
public interface IResidentGradingService {
    List<ResidentGradingRecord> selectGradingRecordList(ResidentGradingRecord record);
    List<ResidentGradingRecord> selectGradingRecordByPersonId(String personId);
    ResidentGradingRecord selectGradingRecordById(Long id);
    int insertGradingRecord(ResidentGradingRecord record);
    int updateGradingRecord(ResidentGradingRecord record);
    int deleteGradingRecordById(Long id);
    int deleteGradingRecordByIds(Long[] ids);
}
