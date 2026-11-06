package com.dchealth.referral.service;

import java.util.List;
import java.util.Map;
import com.dchealth.referral.domain.ReferralTransfer;

public interface IReferralTransferService {
    public List<ReferralTransfer> selectReferralTransferList(ReferralTransfer entity);
    public ReferralTransfer selectReferralTransferById(Long id);
    public int insertReferralTransfer(ReferralTransfer entity);
    public int updateReferralTransfer(ReferralTransfer entity);
    public int deleteReferralTransferById(Long id);
    public int deleteReferralTransferByIds(Long[] ids);

    /** 关联 mp_person 查询转诊列表 */
    public List<ReferralTransfer> selectReferralTransferWithPatient(Map<String, Object> params);
}
