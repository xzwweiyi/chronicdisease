package com.dchealth.warn.service.impl;

import com.dchealth.warn.domain.WarnMedicalEarly;
import com.dchealth.warn.domain.WarnMedicalEarlyCondition;
import com.dchealth.warn.domain.WarnTemplateEarly;
import com.dchealth.warn.domain.WarnTemplateEarlyCondition;
import com.dchealth.warn.domain.form.WarnMedicalEarlyConditionForm;
import com.dchealth.warn.domain.form.WarnMedicalEarlyForm;
import com.dchealth.warn.domain.form.WarnTemplateEarlyConditionForm;
import com.dchealth.warn.domain.form.WarnTemplateEarlyForm;
import com.dchealth.warn.domain.vo.WarnMedicalEarlyConditionVO;
import com.dchealth.warn.domain.vo.WarnMedicalEarlyVO;
import com.dchealth.warn.domain.vo.WarnTemplateEarlyConditionVO;
import com.dchealth.warn.domain.vo.WarnTemplateEarlyVO;
import com.dchealth.warn.mapper.WarnMedicalEarlyConditionMapper;
import com.dchealth.warn.mapper.WarnMedicalEarlyMapper;
import com.dchealth.warn.mapper.WarnTemplateEarlyConditionMapper;
import com.dchealth.warn.mapper.WarnTemplateEarlyMapper;
import com.dchealth.warn.service.IWarnService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WarnServiceImpl implements IWarnService {
    
    @Autowired
    private WarnMedicalEarlyMapper warnMedicalEarlyMapper;
    @Autowired
    private WarnMedicalEarlyConditionMapper warnMedicalEarlyConditionMapper;
    @Autowired
    private WarnTemplateEarlyMapper warnTemplateEarlyMapper;
    @Autowired
    private WarnTemplateEarlyConditionMapper warnTemplateEarlyConditionMapper;

    @Override
    public List<WarnMedicalEarlyVO> selectWarnMedicalEarlys(Long followId) {
        List<WarnMedicalEarlyVO> warnMedicalEarlyVOS = new ArrayList<>();
        WarnMedicalEarly params = new WarnMedicalEarly();
        params.setFollowId(followId);
        List<WarnMedicalEarly> warnMedicalEarlies = warnMedicalEarlyMapper.selectWarnMedicalEarlyList(params);
        WarnMedicalEarlyCondition conditionParams = new WarnMedicalEarlyCondition();
        conditionParams.setFollowId(followId);
        List<WarnMedicalEarlyCondition> warnMedicalEarlyConditions = warnMedicalEarlyConditionMapper.selectWarnMedicalEarlyConditionList(conditionParams);
        if (warnMedicalEarlies != null && !warnMedicalEarlies.isEmpty()){
            Map<Long, List<WarnMedicalEarlyCondition>> conds = warnMedicalEarlyConditions == null || warnMedicalEarlyConditions.isEmpty() ? 
                    null : warnMedicalEarlyConditions.stream().collect(Collectors.groupingBy(WarnMedicalEarlyCondition :: getMedicalEarlyWarnId));
            warnMedicalEarlies.forEach(w->{
                WarnMedicalEarlyVO warnMedicalEarlyVO = new WarnMedicalEarlyVO();
                BeanUtils.copyProperties(w, warnMedicalEarlyVO);
                if (conds!=null && conds.containsKey(w.getId())){
                    List<WarnMedicalEarlyCondition> medicalEarlyConditions = conds.get(w.getId());
                    List<WarnMedicalEarlyConditionVO> warnMedicalEarlyConditionVOS = new ArrayList<>();
                    medicalEarlyConditions.forEach(e->{
                        WarnMedicalEarlyConditionVO conditionVO = new WarnMedicalEarlyConditionVO();
                        BeanUtils.copyProperties(e, conditionVO);
                        warnMedicalEarlyConditionVOS.add(conditionVO);
                    });
                    warnMedicalEarlyVO.setWarnMedicalEarlyConditionVOS(warnMedicalEarlyConditionVOS);
                }
                warnMedicalEarlyVOS.add(warnMedicalEarlyVO);
            });
        }
        return warnMedicalEarlyVOS;
    }

    @Override
    public List<WarnMedicalEarlyVO> selectWarnMedicalEarlysByFormId(Long formId) {
        List<WarnMedicalEarlyVO> warnMedicalEarlyVOS = new ArrayList<>();
        WarnMedicalEarly params = new WarnMedicalEarly();
        params.setFormId(formId);
        List<WarnMedicalEarly> warnMedicalEarlies = warnMedicalEarlyMapper.selectWarnMedicalEarlyList(params);
        if (warnMedicalEarlies != null && !warnMedicalEarlies.isEmpty()){
            WarnMedicalEarlyCondition conditionParams = new WarnMedicalEarlyCondition();
            conditionParams.setFollowId(warnMedicalEarlies.get(0).getFollowId());
            List<WarnMedicalEarlyCondition> warnMedicalEarlyConditions = warnMedicalEarlyConditionMapper.selectWarnMedicalEarlyConditionList(conditionParams);
            Map<Long, List<WarnMedicalEarlyCondition>> conds = warnMedicalEarlyConditions == null || warnMedicalEarlyConditions.isEmpty() ?
                    null : warnMedicalEarlyConditions.stream().collect(Collectors.groupingBy(WarnMedicalEarlyCondition :: getMedicalEarlyWarnId));
            warnMedicalEarlies.forEach(w->{
                WarnMedicalEarlyVO warnMedicalEarlyVO = new WarnMedicalEarlyVO();
                BeanUtils.copyProperties(w, warnMedicalEarlyVO);
                if (conds.containsKey(w.getId())){
                    List<WarnMedicalEarlyCondition> medicalEarlyConditions = conds.get(w.getId());
                    List<WarnMedicalEarlyConditionVO> warnMedicalEarlyConditionVOS = new ArrayList<>();
                    medicalEarlyConditions.forEach(e->{
                        WarnMedicalEarlyConditionVO conditionVO = new WarnMedicalEarlyConditionVO();
                        BeanUtils.copyProperties(e, conditionVO);
                        warnMedicalEarlyConditionVOS.add(conditionVO);
                    });
                    warnMedicalEarlyVO.setWarnMedicalEarlyConditionVOS(warnMedicalEarlyConditionVOS);
                }
                warnMedicalEarlyVOS.add(warnMedicalEarlyVO);
            });
        }
        return warnMedicalEarlyVOS;
    }

    @Override
    public List<WarnMedicalEarlyForm> selectWarnMedicalEarlysForm(Long followId) {
        List<WarnMedicalEarlyForm> warnMedicalEarlyForms = new ArrayList<>();
        WarnMedicalEarly params = new WarnMedicalEarly();
        params.setFollowId(followId);
        List<WarnMedicalEarly> warnMedicalEarlies = warnMedicalEarlyMapper.selectWarnMedicalEarlyList(params);
        WarnMedicalEarlyCondition conditionParams = new WarnMedicalEarlyCondition();
        conditionParams.setFollowId(followId);
        List<WarnMedicalEarlyCondition> warnMedicalEarlyConditions = warnMedicalEarlyConditionMapper.selectWarnMedicalEarlyConditionList(conditionParams);
        if (warnMedicalEarlies != null && !warnMedicalEarlies.isEmpty()){
            Map<Long, List<WarnMedicalEarlyCondition>> conds = warnMedicalEarlyConditions == null || warnMedicalEarlyConditions.isEmpty() ?
                    null : warnMedicalEarlyConditions.stream().collect(Collectors.groupingBy(WarnMedicalEarlyCondition :: getMedicalEarlyWarnId));
            warnMedicalEarlies.forEach(w->{
                WarnMedicalEarlyForm warnMedicalEarlyForm = new WarnMedicalEarlyForm();
                BeanUtils.copyProperties(w, warnMedicalEarlyForm);
                if (conds.containsKey(w.getId())){
                    List<WarnMedicalEarlyCondition> medicalEarlyConditions = conds.get(w.getId());
                    List<WarnMedicalEarlyConditionForm> warnMedicalEarlyConditionForms = new ArrayList<>();
                    medicalEarlyConditions.forEach(e->{
                        WarnMedicalEarlyConditionForm warnMedicalEarlyConditionForm = new WarnMedicalEarlyConditionForm();
                        BeanUtils.copyProperties(e, warnMedicalEarlyConditionForm);
                        warnMedicalEarlyConditionForms.add(warnMedicalEarlyConditionForm);
                    });
                    warnMedicalEarlyForm.setWarnMedicalEarlyConditionForms(warnMedicalEarlyConditionForms);
                }
                warnMedicalEarlyForms.add(warnMedicalEarlyForm);
            });
        }
        return warnMedicalEarlyForms;
    }

    @Override
    @Transactional
    public int saveWarnMedical(Long followId, Long formId, Long propertyId,List<WarnMedicalEarlyForm> warnMedicalEarlyForms) {
        warnMedicalEarlyForms.forEach(warnMedicalEarlyForm -> {
            warnMedicalEarlyForm.setFollowId(followId);
            warnMedicalEarlyForm.setFormId(formId);
            warnMedicalEarlyForm.setPropertyId(propertyId);
            WarnMedicalEarly warnMedicalEarly = new WarnMedicalEarly();
            BeanUtils.copyProperties(warnMedicalEarlyForm, warnMedicalEarly);
            Date now = new Date();
            if (warnMedicalEarly.getId() == null){
                warnMedicalEarly.setCreateTime(now);
                warnMedicalEarlyMapper.insertWarnMedicalEarly(warnMedicalEarly);
            }else{
                warnMedicalEarlyMapper.updateWarnMedicalEarly(warnMedicalEarly);
            }
            if (warnMedicalEarlyForm.getWarnMedicalEarlyConditionForms() != null
                    && !warnMedicalEarlyForm.getWarnMedicalEarlyConditionForms().isEmpty()){
                warnMedicalEarlyForm.getWarnMedicalEarlyConditionForms().forEach(x->{
                    x.setFollowId(followId);
                    x.setPropertyId(propertyId);
                    WarnMedicalEarlyCondition warnMedicalEarlyCondition = new WarnMedicalEarlyCondition();
                    BeanUtils.copyProperties(x, warnMedicalEarlyCondition);
                    warnMedicalEarlyCondition.setMedicalEarlyWarnId(warnMedicalEarly.getId());
                    if (warnMedicalEarlyCondition.getId() == null){
                        warnMedicalEarlyCondition.setCreateTime(now);
                        warnMedicalEarlyConditionMapper.insertWarnMedicalEarlyCondition(warnMedicalEarlyCondition);
                    }else{
                        warnMedicalEarlyConditionMapper.updateWarnMedicalEarlyCondition(warnMedicalEarlyCondition);
                    }
                });
            }
        });
        return 1;
    }

    @Override
    public int importWarnMedical(Long followId, Long formId, Long propertyId, List<WarnTemplateEarlyForm> warnTemplateEarlyForms) {
        warnTemplateEarlyForms.forEach(warnTemplateEarlyForm -> {
            WarnMedicalEarly warnMedicalEarly = new WarnMedicalEarly();
            //230601 导入模板时清空原模板id
            warnTemplateEarlyForm.setId(null);
            BeanUtils.copyProperties(warnTemplateEarlyForm, warnMedicalEarly);
            warnMedicalEarly.setFollowId(followId);
            warnMedicalEarly.setFormId(formId);
            warnMedicalEarly.setPropertyId(propertyId);
            Date now = new Date();
            if (warnMedicalEarly.getId() == null){
                warnMedicalEarly.setCreateTime(now);
                warnMedicalEarlyMapper.insertWarnMedicalEarly(warnMedicalEarly);
            }else{
                warnMedicalEarlyMapper.updateWarnMedicalEarly(warnMedicalEarly);
            }
            if (warnTemplateEarlyForm.getWarnTemplateEarlyConditionForms() != null
                    && !warnTemplateEarlyForm.getWarnTemplateEarlyConditionForms().isEmpty()){
                warnTemplateEarlyForm.getWarnTemplateEarlyConditionForms().forEach(x->{
                    WarnMedicalEarlyCondition warnMedicalEarlyCondition = new WarnMedicalEarlyCondition();
                    //230601 导入模板时清空原模板id
                    x.setId(null);
                    BeanUtils.copyProperties(x, warnMedicalEarlyCondition);
                    warnMedicalEarlyCondition.setFollowId(followId);
                    warnMedicalEarlyCondition.setPropertyId(propertyId);
                    warnMedicalEarlyCondition.setMedicalEarlyWarnId(warnMedicalEarly.getId());
                    if (warnMedicalEarlyCondition.getId() == null){
                        warnMedicalEarlyCondition.setCreateTime(now);
                        warnMedicalEarlyConditionMapper.insertWarnMedicalEarlyCondition(warnMedicalEarlyCondition);
                    }else{
                        warnMedicalEarlyConditionMapper.updateWarnMedicalEarlyCondition(warnMedicalEarlyCondition);
                    }
                });
            }
        });
        return 1;
    }

    @Override
    @Transactional
    public int deleteWarnMedical(Long warnMedicalId) {
        WarnMedicalEarly warnMedicalEarly = warnMedicalEarlyMapper.selectWarnMedicalEarlyById(warnMedicalId);
        if (warnMedicalEarly != null){
            if ("1".equals(warnMedicalEarly.getIsCondition())){
                warnMedicalEarlyConditionMapper.deleteWarnMedicalEarlyConditionByWarnMedicalId(warnMedicalId);
            }
            warnMedicalEarlyMapper.deleteWarnMedicalEarlyById(warnMedicalId);
        }
        return 1;
    }

    @Override
    public int deleteWarnMedicalFollowId(Long followId) {
        warnMedicalEarlyConditionMapper.deleteWarnMedicalEarlyConditionByFollowId(followId);
        warnMedicalEarlyMapper.deleteWarnMedicalEarlyByFollowId(followId);
        return 1;
    }

    @Override
    public int deleteWarnMedicalByPropertyIds(List<Long> propertyIds) {
        if(propertyIds == null || propertyIds.size() == 0)
            return 0;
        warnMedicalEarlyConditionMapper.deleteWarnMedicalEarlyConditionByPropertyIds(propertyIds);
        warnMedicalEarlyMapper.deleteWarnMedicalEarlyByPropertyIds(propertyIds);
        return 1;
    }

    @Override
    public List<WarnTemplateEarlyVO> selectWarnTemplateEarlys(Long templateId) {
        List<WarnTemplateEarlyVO> warnTemplateEarlyVOS = new ArrayList<>();
        WarnTemplateEarly params = new WarnTemplateEarly();
        params.setTemplateId(templateId);
        List<WarnTemplateEarly> warnTemplateEarlies = warnTemplateEarlyMapper.selectWarnTemplateEarlyList(params);
        WarnTemplateEarlyCondition conditionParams = new WarnTemplateEarlyCondition();
        conditionParams.setTemplateId(templateId);
        List<WarnTemplateEarlyCondition> warnTemplateEarlyConditions = warnTemplateEarlyConditionMapper.selectWarnTemplateEarlyConditionList(conditionParams);
        if (warnTemplateEarlies != null && !warnTemplateEarlies.isEmpty()){
            Map<Long, List<WarnTemplateEarlyCondition>> conds = warnTemplateEarlyConditions == null || warnTemplateEarlyConditions.isEmpty() ?
                    null : warnTemplateEarlyConditions.stream().collect(Collectors.groupingBy(WarnTemplateEarlyCondition :: getTemplateEarlyWarnId));
            warnTemplateEarlies.forEach(w->{
                WarnTemplateEarlyVO warnTemplateEarlyVO = new WarnTemplateEarlyVO();
                BeanUtils.copyProperties(w, warnTemplateEarlyVO);
                if (conds.containsKey(w.getId())){
                    List<WarnTemplateEarlyCondition> templateEarlyConditions = conds.get(w.getId());
                    List<WarnTemplateEarlyConditionVO> warnTemplateEarlyConditionVOS = new ArrayList<>();
                    templateEarlyConditions.forEach(e->{
                        WarnTemplateEarlyConditionVO conditionVO = new WarnTemplateEarlyConditionVO();
                        BeanUtils.copyProperties(e, conditionVO);
                        warnTemplateEarlyConditionVOS.add(conditionVO);
                    });
                    warnTemplateEarlyVO.setWarnTemplateEarlyConditionVOS(warnTemplateEarlyConditionVOS);
                }
                warnTemplateEarlyVOS.add(warnTemplateEarlyVO);
            });
        }
        return warnTemplateEarlyVOS;
    }

    @Override
    public List<WarnTemplateEarlyVO> selectWarnTemplateEarlysByFormId(Long formId) {
        List<WarnTemplateEarlyVO> warnTemplateEarlyVOS = new ArrayList<>();
        WarnTemplateEarly params = new WarnTemplateEarly();
        params.setFormId(formId);
        List<WarnTemplateEarly> warnTemplateEarlies = warnTemplateEarlyMapper.selectWarnTemplateEarlyList(params);
        if (warnTemplateEarlies != null && !warnTemplateEarlies.isEmpty()){
            WarnTemplateEarlyCondition conditionParams = new WarnTemplateEarlyCondition();
            conditionParams.setTemplateId(warnTemplateEarlies.get(0).getTemplateId());
            List<WarnTemplateEarlyCondition> warnTemplateEarlyConditions = warnTemplateEarlyConditionMapper.selectWarnTemplateEarlyConditionList(conditionParams);
            Map<Long, List<WarnTemplateEarlyCondition>> conds = warnTemplateEarlyConditions == null || warnTemplateEarlyConditions.isEmpty() ?
                    null : warnTemplateEarlyConditions.stream().collect(Collectors.groupingBy(WarnTemplateEarlyCondition :: getTemplateEarlyWarnId));
            warnTemplateEarlies.forEach(w->{
                WarnTemplateEarlyVO warnTemplateEarlyVO = new WarnTemplateEarlyVO();
                BeanUtils.copyProperties(w, warnTemplateEarlyVO);
                if (conds.containsKey(w.getId())){
                    List<WarnTemplateEarlyCondition> templateEarlyConditions = conds.get(w.getId());
                    List<WarnTemplateEarlyConditionVO> warnTemplateEarlyConditionVOS = new ArrayList<>();
                    templateEarlyConditions.forEach(e->{
                        WarnTemplateEarlyConditionVO conditionVO = new WarnTemplateEarlyConditionVO();
                        BeanUtils.copyProperties(e, conditionVO);
                        warnTemplateEarlyConditionVOS.add(conditionVO);
                    });
                    warnTemplateEarlyVO.setWarnTemplateEarlyConditionVOS(warnTemplateEarlyConditionVOS);
                }
                warnTemplateEarlyVOS.add(warnTemplateEarlyVO);
            });
        }
        return warnTemplateEarlyVOS;
    }

    @Override
    public List<WarnTemplateEarlyForm> selectWarnTemplateEarlysForm(Long templateId) {
        List<WarnTemplateEarlyForm> warnTemplateEarlyForms = new ArrayList<>();
        WarnTemplateEarly params = new WarnTemplateEarly();
        params.setTemplateId(templateId);
        List<WarnTemplateEarly> warnTemplateEarlies = warnTemplateEarlyMapper.selectWarnTemplateEarlyList(params);
        WarnTemplateEarlyCondition conditionParams = new WarnTemplateEarlyCondition();
        conditionParams.setTemplateId(templateId);
        List<WarnTemplateEarlyCondition> warnTemplateEarlyConditions = warnTemplateEarlyConditionMapper.selectWarnTemplateEarlyConditionList(conditionParams);
        if (warnTemplateEarlies != null && !warnTemplateEarlies.isEmpty()){
            Map<Long, List<WarnTemplateEarlyCondition>> conds = warnTemplateEarlyConditions == null || warnTemplateEarlyConditions.isEmpty() ?
                    null : warnTemplateEarlyConditions.stream().collect(Collectors.groupingBy(WarnTemplateEarlyCondition :: getTemplateEarlyWarnId));
            warnTemplateEarlies.forEach(w->{
                WarnTemplateEarlyForm warnTemplateEarlyForm = new WarnTemplateEarlyForm();
                BeanUtils.copyProperties(w, warnTemplateEarlyForm);
                if (conds.containsKey(w.getId())){
                    List<WarnTemplateEarlyCondition> templateEarlyConditions = conds.get(w.getId());
                    List<WarnTemplateEarlyConditionForm> warnTemplateEarlyConditionForms = new ArrayList<>();
                    templateEarlyConditions.forEach(e->{
                        WarnTemplateEarlyConditionForm warnTemplateEarlyConditionForm = new WarnTemplateEarlyConditionForm();
                        BeanUtils.copyProperties(e, warnTemplateEarlyConditionForm);
                        warnTemplateEarlyConditionForms.add(warnTemplateEarlyConditionForm);
                    });
                    warnTemplateEarlyForm.setWarnTemplateEarlyConditionForms(warnTemplateEarlyConditionForms);
                }
                warnTemplateEarlyForms.add(warnTemplateEarlyForm);
            });
        }
        return warnTemplateEarlyForms;
    }

    @Override
    @Transactional
    public int saveWarnTemplate(Long templateId, Long formId, Long propertyId, List<WarnTemplateEarlyForm> warnTemplateEarlyForms) {
        warnTemplateEarlyForms.forEach(warnTemplateEarlyForm -> {
            warnTemplateEarlyForm.setFormId(formId);
            warnTemplateEarlyForm.setTemplateId(templateId);
            warnTemplateEarlyForm.setPropertyId(propertyId);
            WarnTemplateEarly warnTemplateEarly = new WarnTemplateEarly();
            BeanUtils.copyProperties(warnTemplateEarlyForm, warnTemplateEarly);
            Date now = new Date();
            if (warnTemplateEarly.getId() == null){
                warnTemplateEarly.setCreateTime(now);
                warnTemplateEarlyMapper.insertWarnTemplateEarly(warnTemplateEarly);
            }else{
                warnTemplateEarlyMapper.updateWarnTemplateEarly(warnTemplateEarly);
            }
            if (warnTemplateEarlyForm.getWarnTemplateEarlyConditionForms() != null
                    && !warnTemplateEarlyForm.getWarnTemplateEarlyConditionForms().isEmpty()){
                warnTemplateEarlyForm.getWarnTemplateEarlyConditionForms().forEach(x->{
                    x.setTemplateId(templateId);
                    x.setPropertyId(propertyId);
                    WarnTemplateEarlyCondition warnTemplateEarlyCondition = new WarnTemplateEarlyCondition();
                    BeanUtils.copyProperties(x, warnTemplateEarlyCondition);
                    warnTemplateEarlyCondition.setTemplateEarlyWarnId(warnTemplateEarly.getId());
                    if (warnTemplateEarlyCondition.getId() == null){
                        warnTemplateEarlyCondition.setCreateTime(now);
                        warnTemplateEarlyConditionMapper.insertWarnTemplateEarlyCondition(warnTemplateEarlyCondition);
                    }else{
                        warnTemplateEarlyConditionMapper.updateWarnTemplateEarlyCondition(warnTemplateEarlyCondition);
                    }
                });
            }
        });
        return 1;
    }

    @Override
    public int importWarnTemplate(Long templateId, Long formId, Long propertyId, List<WarnMedicalEarlyForm> warnMedicalEarlyForms) {
        warnMedicalEarlyForms.forEach(warnMedicalEarlyForm -> {
            WarnTemplateEarly warnTemplateEarly = new WarnTemplateEarly();
            //20230531 增加 修复预警条件不保存问题
            warnMedicalEarlyForm.setId(null);
            BeanUtils.copyProperties(warnMedicalEarlyForm, warnTemplateEarly);
            warnTemplateEarly.setTemplateId(templateId);
            warnTemplateEarly.setFormId(formId);
            warnTemplateEarly.setPropertyId(propertyId);
            Date now = new Date();
            if (warnTemplateEarly.getId() == null){
                warnTemplateEarly.setCreateTime(now);
                warnTemplateEarlyMapper.insertWarnTemplateEarly(warnTemplateEarly);
            }else{
                warnTemplateEarlyMapper.updateWarnTemplateEarly(warnTemplateEarly);
            }
            if (warnMedicalEarlyForm.getWarnMedicalEarlyConditionForms() != null
                    && !warnMedicalEarlyForm.getWarnMedicalEarlyConditionForms().isEmpty()){
                warnMedicalEarlyForm.getWarnMedicalEarlyConditionForms().forEach(x->{
                    WarnTemplateEarlyCondition warnTemplateEarlyCondition = new WarnTemplateEarlyCondition();
                    //20230531 增加 修复预警条件不保存问题
                    x.setId(null);
                    BeanUtils.copyProperties(x, warnTemplateEarlyCondition);
                    warnTemplateEarlyCondition.setTemplateId(templateId);
                    warnTemplateEarlyCondition.setPropertyId(propertyId);
                    warnTemplateEarlyCondition.setTemplateEarlyWarnId(warnTemplateEarly.getId());
                    if (warnTemplateEarlyCondition.getId() == null){
                        warnTemplateEarlyCondition.setCreateTime(now);
                        warnTemplateEarlyConditionMapper.insertWarnTemplateEarlyCondition(warnTemplateEarlyCondition);
                    }else{
                        warnTemplateEarlyConditionMapper.updateWarnTemplateEarlyCondition(warnTemplateEarlyCondition);
                    }
                });
            }
        });
        return 1;
    }

    @Override
    public int deleteWarnTemplate(Long warnTemplateId) {
        WarnTemplateEarly warnTemplateEarly = warnTemplateEarlyMapper.selectWarnTemplateEarlyById(warnTemplateId);
        if (warnTemplateEarly != null){
            if ("1".equals(warnTemplateEarly.getIsCondition())){
                warnTemplateEarlyConditionMapper.deleteWarnTemplateEarlyConditionByWarnTemplateEarlyId(warnTemplateId);
            }
            warnTemplateEarlyMapper.deleteWarnTemplateEarlyById(warnTemplateId);
        }
        return 1;
    }
}
