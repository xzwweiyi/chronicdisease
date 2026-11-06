package com.dchealth.resident.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dchealth.resident.domain.ResidentArchive;
import com.dchealth.resident.domain.vo.ArchivedPatientVO;

public interface ResidentArchiveMapper {
    // === Archive CRUD ===
    List<ResidentArchive> selectResidentArchiveList(ResidentArchive entity);
    ResidentArchive selectResidentArchiveById(Long id);
    int insertResidentArchive(ResidentArchive entity);
    int updateResidentArchive(ResidentArchive entity);
    int deleteResidentArchiveById(Long id);
    int deleteResidentArchiveByIds(Long[] ids);

    // === Stats ===
    Long countTotal();
    Long countNewThisMonth();
    Long countCrossInstitution();
    Long countCompleteArchives();
    List<Map<String, Object>> diseaseDistribution();

    // === Joined Archive List (mp_person + resident_archive) ===
    List<ArchivedPatientVO> selectArchivedPatientList(Map<String, Object> params);
    ArchivedPatientVO selectArchivedPatientByPersonId(@Param("cardId") String cardId);

    // === Cross-org distribution ===
    List<Map<String, Object>> crossOrgDistribution();

    // === Disease-specific filter (by person_id list from mp_diagnosis) ===
    List<ArchivedPatientVO> selectArchivedPatientByPersonIds(@Param("personIdList") List<String> personIdList, @Param("params") Map<String, Object> params);

    // === 合并症患者列表（disease_types 含2种及以上慢病标签） ===
    List<ArchivedPatientVO> selectComorbidityPatientList(Map<String, Object> params);
}
