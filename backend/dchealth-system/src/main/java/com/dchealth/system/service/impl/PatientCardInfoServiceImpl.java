package com.dchealth.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.dchealth.common.core.domain.entity.SysDictData;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.system.mapper.PatientCardInfoViewNoneMapper;
import com.dchealth.system.mapper.PatientCardInfoViewNullMapper;
import com.dchealth.system.mapper.SysDictDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.system.mapper.PatientCardInfoMapper;
import com.dchealth.system.domain.PatientCardInfo;
import com.dchealth.system.service.IPatientCardInfoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 患者主号管理Service业务层处理
 * 
 * @author a
 * @date 2024-10-30
 */
@Service
@Slf4j
public class PatientCardInfoServiceImpl implements IPatientCardInfoService 
{
    @Autowired
    private PatientCardInfoMapper patientCardInfoMapper;
    @Autowired
    private SysDictDataMapper sysDictDataMapper;
    @Autowired
    private PatientCardInfoViewNoneMapper patientCardInfoViewNoneMapper;
    @Autowired
    private PatientCardInfoViewNullMapper patientCardInfoViewNullMapper;

    /**
     * 查询患者主号管理
     * 
     * @param id 患者主号管理主键
     * @return 患者主号管理
     */
    @Override
    public PatientCardInfo selectPatientCardInfoById(Long id)
    {
        return patientCardInfoMapper.selectPatientCardInfoById(id);
    }

    /**
     * 查询患者主号管理列表
     * 
     * @param patientCardInfo 患者主号管理
     * @return 患者主号管理
     */
    @Override
    public List<PatientCardInfo> selectPatientCardInfoList(PatientCardInfo patientCardInfo)
    {
        return patientCardInfoMapper.selectPatientCardInfoList(patientCardInfo);
    }

    /**
     * 新增患者主号管理
     * 
     * @param patientCardInfo 患者主号管理
     * @return 结果
     */
    @Override
    public int insertPatientCardInfo(PatientCardInfo patientCardInfo)
    {
        patientCardInfo.setCreateTime(DateUtils.getNowDate());
        patientCardInfo.setUpdateTime(DateUtils.getNowDate());
        // 新增特殊处理
        patientCardInfo.setPatientMasterNo(0);
        patientCardInfo.setUpdateStatus(1);
        PatientCardInfo query = new PatientCardInfo();
        query.setIdCard(patientCardInfo.getIdCard());
        query.setPersonId(patientCardInfo.getPersonId());
        List<PatientCardInfo> patientCardInfos = patientCardInfoMapper.selectPatientCardInfoList(query);
        if (!CollectionUtils.isEmpty(patientCardInfos)) {
            throw new ServiceException("该身份证和患者号已存在");
        }
        return patientCardInfoMapper.insertPatientCardInfo(patientCardInfo);
    }

    /**
     * 修改患者主号管理
     * 
     * @param patientCardInfo 患者主号管理
     * @return 结果
     */
    @Override
    @Transactional
    public int updateMasterNumber(PatientCardInfo patientCardInfo)
    {
        String idCard = patientCardInfo.getIdCard();
        if ("000000000000000000".equals(idCard) || StringUtils.isEmpty(idCard)||idCard.length()!=18) {
            throw new ServiceException("身份证非法!");
        }
        if (StringUtils.isEmpty(patientCardInfo.getPersonId())) {
            throw new ServiceException("患者号不能为空!");
        }
        // 校验, 来自于app端的事件需要
        PatientCardInfo query = new PatientCardInfo();
        query.setIdCard(patientCardInfo.getIdCard());
        query.setPersonId(patientCardInfo.getPersonId());
        List<PatientCardInfo> patientCardInfos1 = patientCardInfoMapper.selectPatientCardInfoList(query);
        if (CollectionUtils.isEmpty(patientCardInfos1)) {
            throw new ServiceException("参数有误!");
        }
        List<Long> collect = patientCardInfos1.stream().filter(item -> patientCardInfo.getIdCard().equals(item.getIdCard()) && patientCardInfo.getPersonId().equals(item.getPersonId()))
                .map(item -> item.getId()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {
            Long id = collect.get(0);
            if (Objects.isNull(patientCardInfo.getId())) {
                patientCardInfo.setId(id);
            }
        }
        PatientCardInfo patientCardInfoQuery = new PatientCardInfo();
        patientCardInfoQuery.setIdCard(idCard);
        /*更新同身份证的就诊号初始化*/
        List<PatientCardInfo> patientCardInfos = patientCardInfoMapper.selectPatientCardInfoList(patientCardInfoQuery);
        for (PatientCardInfo cardInfo : patientCardInfos) {
            // 全部置为次号
            cardInfo.setPatientMasterNo(0);
            // 全部标记未处理
            cardInfo.setUpdateStatus(0);
            cardInfo.setUpdateTime(DateUtils.getNowDate());
            patientCardInfoMapper.updatePatientCardInfo(cardInfo);
        }
        // 设置主号, 未处理
        patientCardInfo.setUpdateTime(DateUtils.getNowDate());
        patientCardInfo.setUpdateStatus(0);
        patientCardInfo.setPatientMasterNo(1);
        patientCardInfo.setPersonIdGenerateTime(DateUtils.getNowDate());
        return patientCardInfoMapper.updatePatientCardInfo(patientCardInfo);
    }

    /**
     * 修改患者主号管理
     *
     * @param patientCardInfo 患者主号管理
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePatientCardInfo(PatientCardInfo patientCardInfo)
    {

        String idCard = patientCardInfo.getIdCard();
        String personId = patientCardInfo.getPersonId();
        PatientCardInfo patientCardInfoQuery = new PatientCardInfo();
        patientCardInfoQuery.setIdCard(idCard);
        patientCardInfoQuery.setPersonId(personId);
        /*更新同身份证的就诊号初始化*/
        List<PatientCardInfo> patientCardInfos = patientCardInfoMapper.selectPatientCardInfoList(patientCardInfoQuery);
        if (!CollectionUtils.isEmpty(patientCardInfos)&&patientCardInfos.size()>1) {
            throw new ServiceException("患者身份证号和患者id已存在");
        }
        if (!CollectionUtils.isEmpty(patientCardInfos)) {
            PatientCardInfo patientCardInfo1 = patientCardInfos.get(0);
            if (!patientCardInfo1.getId().equals(patientCardInfo.getId())) {
                throw new ServiceException("患者身份证号和患者id已存在");
            }
        }
        // 设置主号, 未处理
        patientCardInfo.setUpdateTime(DateUtils.getNowDate());
        patientCardInfo.setPersonIdGenerateTime(DateUtils.getNowDate());
        return patientCardInfoMapper.updatePatientCardInfo(patientCardInfo);
    }

    /**
     * 更新患者主号服务
     *
     * @param patientCardInfo 患者主号管理
     * @return 结果
     */
    @Override
    public Map<String, Integer> updatePatientAllPersonId()
    {
        long startTime = System.currentTimeMillis();
        Map<String, Integer> updateInfos = new HashMap<>();
        // 查询所有的表
        List<SysDictData> patientMasterRelated = sysDictDataMapper.selectDictDataByType("patient_master_related");
        int sum = 0;
        // 需要异步更新? todo
        for (SysDictData sysDictData : patientMasterRelated) {
            if ("patient_account".equals(sysDictData.getDictValue())) {
                // 影响表的行数
                int num = patientCardInfoMapper.updatePatientAllPersonIdOfPatientAccount();
                sum+=num;
                log.info("更新数据库表{}的personid, 影响行数{}, 耗时{}ms",sysDictData.getDictValue(),num, System.currentTimeMillis()-startTime);
                updateInfos.put(sysDictData.getDictValue(), num);

            }else{
                // 影响表的行数
                int num = patientCardInfoMapper.updatePatientAllPersonId(sysDictData);
                sum+=num;
                log.info("更新数据库表{}的personid, 影响行数{}, 耗时{}ms",sysDictData.getDictValue(),num, System.currentTimeMillis()-startTime);
                updateInfos.put(sysDictData.getDictValue(), num);
            }
        }
        //变更映射表状态
        PatientCardInfo patientCardInfo = new PatientCardInfo();
        patientCardInfo.setUpdateStatus(1);
        patientCardInfoMapper.updatePatientCardInfoAll(patientCardInfo);
        //日志中出现总计更新, 说明此次所有表批量成功了
        log.info("总计更新数据库表{}的personid, 影响总行数{}, 总耗时{}ms", JSON.toJSONString(patientMasterRelated),sum, System.currentTimeMillis()-startTime);
        return updateInfos;
    }
    /**
     * 把患者信息从patient_card_info_xxx同步到patient_card_info表中
     */
    @Override
    public void syncToMasterTable(){

//        PatientCardInfoViewNone patientCardInfoViewNone = new PatientCardInfoViewNone();
//        patientCardInfoViewNone.setCreateTime(DateUtils.addDays(DateUtils.getNowDate(),-1));
//        List<PatientCardInfoViewNone> patientCardInfoViewNones = patientCardInfoViewNoneMapper.selectPatientCardInfoViewNoneList(patientCardInfoViewNone);
//
//        List<PatientCardInfoViewNone> patientCardView = new ArrayList<>();
//        //近亲处理
//        if (!CollectionUtils.isEmpty(patientCardInfoViewNones)) {
//            for (PatientCardInfoViewNone cardInfoViewNone : patientCardInfoViewNones) {
//                // 相似因子
//                String name = cardInfoViewNone.getName(); //姓名必须相同
//                String sex = cardInfoViewNone.getSex(); // 同名的性别必须相同, 必填才可以!!!
//                Date dateOfBirth = cardInfoViewNone.getDateOfBirth();
//                String birthPlace = cardInfoViewNone.getBirthPlace();
//                String personId = cardInfoViewNone.getPersonId();
//                String nextOfKinPhone = cardInfoViewNone.getNextOfKinPhone(); //亲属电话或许相同
//                String idCard = cardInfoViewNone.getIdCard(); // 若有身份证, 相同的肯定是
//                String phoneNumberHome = cardInfoViewNone.getPhoneNumberHome();// 手机号相同的, 姓名相同必定是
//
//                PatientCardInfoViewNone queryNear = new PatientCardInfoViewNone();
//                queryNear.setName(name);
//                queryNear.setSex(sex);
//                List<PatientCardInfoViewNone> patientCardInfoNear = patientCardInfoViewNoneMapper.selectPatientCardInfoViewNoneList(queryNear);
//                if (!CollectionUtils.isEmpty(patientCardInfoNear)) {
//                    for (PatientCardInfoViewNone infoViewNone : patientCardInfoNear) {
//                        if (!StringUtils.isEmpty(infoViewNone.getIdCard()) && infoViewNone.getIdCard().equals(idCard)&&) {
//
//                        }
//
//                    }
//                }
//
//
//            }
//        }
//        //插入处理

    }


    /**
     * 选举主号
     */
    @Override
    public void updateMasterPeson(){
        patientCardInfoMapper.updateMasterPeson();
   }
    /**
     * 批量删除患者主号管理
     * 
     * @param ids 需要删除的患者主号管理主键
     * @return 结果
     */
    @Override
    public int deletePatientCardInfoByIds(Long[] ids)
    {
        return patientCardInfoMapper.deletePatientCardInfoByIds(ids);
    }

    /**
     * 删除患者主号管理信息
     * 
     * @param id 患者主号管理主键
     * @return 结果
     */
    @Override
    public int deletePatientCardInfoById(Long id)
    {
        return patientCardInfoMapper.deletePatientCardInfoById(id);
    }
}
