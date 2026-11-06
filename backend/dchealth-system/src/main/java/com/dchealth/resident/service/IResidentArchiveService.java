package com.dchealth.resident.service;

import java.util.List;
import java.util.Map;

import com.dchealth.resident.domain.ResidentArchive;
import com.dchealth.resident.domain.vo.ArchivedPatientVO;

/**
 * 居民档案Service接口
 */
public interface IResidentArchiveService {
    // === Archive CRUD ===
    List<ResidentArchive> selectResidentArchiveList(ResidentArchive entity);
    ResidentArchive selectResidentArchiveById(Long id);
    int insertResidentArchive(ResidentArchive entity);
    int updateResidentArchive(ResidentArchive entity);
    int deleteResidentArchiveById(Long id);
    int deleteResidentArchiveByIds(Long[] ids);

    // === Joined Archive Queries ===
    List<ArchivedPatientVO> selectArchivedPatientList(Map<String, Object> params);
    ArchivedPatientVO selectArchivedPatientByPersonId(String cardId);
    List<ArchivedPatientVO> selectArchivedPatientByPersonIds(List<String> personIdList, Map<String, Object> params);

    // === 合并症 ===
    List<ArchivedPatientVO> selectComorbidityPatientList(Map<String, Object> params);

    // === Stats ===
    Map<String, Object> getStats();
    List<Map<String, Object>> getCrossOrgDistribution();
}
