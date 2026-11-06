package com.dchealth.referral.mapper;

import java.util.List;
import java.util.Map;
import com.dchealth.referral.domain.ReferralTransfer;

public interface ReferralTransferMapper {
    List<ReferralTransfer> selectReferralTransferList(ReferralTransfer entity);
    ReferralTransfer selectReferralTransferById(Long id);
    int insertReferralTransfer(ReferralTransfer entity);
    int updateReferralTransfer(ReferralTransfer entity);
    int deleteReferralTransferById(Long id);
    int deleteReferralTransferByIds(Long[] ids);

    /** 关联 mp_person 查询转诊患者列表（通过 patient_info 桥接） */
    List<ReferralTransfer> selectReferralTransferWithPatient(Map<String, Object> params);
}
