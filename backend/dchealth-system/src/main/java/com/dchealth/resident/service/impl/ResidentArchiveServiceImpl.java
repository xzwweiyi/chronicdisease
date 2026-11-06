package com.dchealth.resident.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dchealth.resident.domain.ResidentArchive;
import com.dchealth.resident.domain.vo.ArchivedPatientVO;
import com.dchealth.resident.mapper.ResidentArchiveMapper;
import com.dchealth.resident.service.IResidentArchiveService;

/**
 * 居民档案Service业务层处理
 */
@Service
public class ResidentArchiveServiceImpl implements IResidentArchiveService {

    @Autowired
    private ResidentArchiveMapper residentArchiveMapper;

    // === Archive CRUD ===
    @Override
    public List<ResidentArchive> selectResidentArchiveList(ResidentArchive entity) {
        return residentArchiveMapper.selectResidentArchiveList(entity);
    }

    @Override
    public ResidentArchive selectResidentArchiveById(Long id) {
        return residentArchiveMapper.selectResidentArchiveById(id);
    }

    @Override
    public int insertResidentArchive(ResidentArchive entity) {
        return residentArchiveMapper.insertResidentArchive(entity);
    }

    @Override
    public int updateResidentArchive(ResidentArchive entity) {
        return residentArchiveMapper.updateResidentArchive(entity);
    }

    @Override
    public int deleteResidentArchiveById(Long id) {
        return residentArchiveMapper.deleteResidentArchiveById(id);
    }

    @Override
    public int deleteResidentArchiveByIds(Long[] ids) {
        return residentArchiveMapper.deleteResidentArchiveByIds(ids);
    }

    // === Joined Archive Queries ===
    @Override
    public List<ArchivedPatientVO> selectArchivedPatientList(Map<String, Object> params) {
        return residentArchiveMapper.selectArchivedPatientList(params);
    }

    @Override
    public ArchivedPatientVO selectArchivedPatientByPersonId(String cardId) {
        return residentArchiveMapper.selectArchivedPatientByPersonId(cardId);
    }

    @Override
    public List<ArchivedPatientVO> selectArchivedPatientByPersonIds(List<String> personIdList, Map<String, Object> params) {
        return residentArchiveMapper.selectArchivedPatientByPersonIds(personIdList, params);
    }

    // === 合并症 ===
    @Override
    public List<ArchivedPatientVO> selectComorbidityPatientList(Map<String, Object> params) {
        return residentArchiveMapper.selectComorbidityPatientList(params);
    }

    // === Stats ===
    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            Long total = residentArchiveMapper.countTotal();
            result.put("totalPatients", total != null ? total : 0L);

            Long newThisMonth = residentArchiveMapper.countNewThisMonth();
            result.put("newThisMonth", newThisMonth != null ? newThisMonth : 0L);

            Long crossInstitution = residentArchiveMapper.countCrossInstitution();
            result.put("crossInstitution", crossInstitution != null ? crossInstitution : 0L);

            // 档案完整率：已建档且关键字段齐全的比例（由 mapper 子查询计算）
            try {
                Long completeCount = residentArchiveMapper.countCompleteArchives();
                double rate = total != null && total > 0
                    ? Math.round(completeCount * 10000.0 / total) / 100.0
                    : 0.0;
                result.put("completionRate", rate);
            } catch (Exception e) {
                result.put("completionRate", 0.0);
            }

            // Disease distribution
            List<Map<String, Object>> diseaseDist = residentArchiveMapper.diseaseDistribution();
            result.put("diseaseDistribution", diseaseDist != null ? diseaseDist : new ArrayList<>());
        } catch (Exception e) {
            result.put("totalPatients", 0L);
            result.put("newThisMonth", 0L);
            result.put("crossInstitution", 0L);
            result.put("completionRate", 0.0);
            result.put("diseaseDistribution", new ArrayList<>());
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getCrossOrgDistribution() {
        return residentArchiveMapper.crossOrgDistribution();
    }
}
