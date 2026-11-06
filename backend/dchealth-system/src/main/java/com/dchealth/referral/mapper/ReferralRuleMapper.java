package com.dchealth.referral.mapper;

import java.util.List;
import com.dchealth.referral.domain.ReferralRule;

public interface ReferralRuleMapper {
    List<ReferralRule> selectReferralRuleList(ReferralRule entity);
    ReferralRule selectReferralRuleById(Long id);
    int insertReferralRule(ReferralRule entity);
    int updateReferralRule(ReferralRule entity);
    int deleteReferralRuleById(Long id);
    int deleteReferralRuleByIds(Long[] ids);
}
