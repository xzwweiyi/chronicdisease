package com.dchealth.referral.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.referral.domain.ReferralTransfer;
import com.dchealth.referral.mapper.ReferralTransferMapper;
import com.dchealth.referral.service.IReferralTransferService;

@Service
public class ReferralTransferServiceImpl implements IReferralTransferService
{
    @Autowired
    private ReferralTransferMapper referralTransferMapper;

    @Override
    public List<ReferralTransfer> selectReferralTransferList(ReferralTransfer entity) { return referralTransferMapper.selectReferralTransferList(entity); }
    @Override
    public ReferralTransfer selectReferralTransferById(Long id) { return referralTransferMapper.selectReferralTransferById(id); }
    @Override
    public int insertReferralTransfer(ReferralTransfer entity) { return referralTransferMapper.insertReferralTransfer(entity); }
    @Override
    public int updateReferralTransfer(ReferralTransfer entity) { return referralTransferMapper.updateReferralTransfer(entity); }
    @Override
    public int deleteReferralTransferById(Long id) { return referralTransferMapper.deleteReferralTransferById(id); }
    @Override
    public int deleteReferralTransferByIds(Long[] ids) { return referralTransferMapper.deleteReferralTransferByIds(ids); }

    @Override
    public List<ReferralTransfer> selectReferralTransferWithPatient(Map<String, Object> params) {
        return referralTransferMapper.selectReferralTransferWithPatient(params);
    }
}
