package com.dchealth.question.service.impl;

import com.dchealth.account.domain.AccountNotice;
import com.dchealth.account.mapper.AccountNoticeMapper;
import com.dchealth.account.service.impl.UmpushService;
import com.dchealth.common.constant.FollowTipStatusConstants;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.question.domain.QuestionRecord;
import com.dchealth.question.domain.QuestionRecordForm;
import com.dchealth.question.domain.form.SaveQuestionRecordVo;
import com.dchealth.question.mapper.QuestionRecordFormMapper;
import com.dchealth.question.mapper.QuestionRecordMapper;
import com.dchealth.question.service.IQuestionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 质疑Service业务层处理
 *
 * @author lchbu
 * @date 2023-02-23
 */
@Service
public class QuestionRecordServiceImpl implements IQuestionRecordService {
    @Autowired
    private QuestionRecordMapper questionRecordMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    @Autowired
    private QuestionRecordFormMapper questionRecordFormMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;

    @Autowired
    private AccountNoticeMapper accountNoticeMapper;
    @Autowired
    private UmpushService umpushService;
    /**
     * 查询质疑
     *
     * @param questionId 质疑主键
     * @return 质疑
     */
    @Override
    public QuestionRecord selectQuestionRecordByQuestionId(Long questionId) {
        return questionRecordMapper.selectQuestionRecordByQuestionId(questionId);
    }

    /**
     * 查询质疑列表
     *
     * @param questionRecord 质疑
     * @return 质疑
     */
    @Override
    public List<QuestionRecord> selectQuestionRecordList(QuestionRecord questionRecord) {
        return questionRecordMapper.selectQuestionRecordList(questionRecord);
    }

    /**
     * 新增质疑
     *
     * @param questionRecord 质疑
     * @return 结果
     */
    @Override
    public int insertQuestionRecord(QuestionRecord questionRecord) {
        return questionRecordMapper.insertQuestionRecord(questionRecord);
    }

    @Override
    @Transactional
    public int saveQuestionRecord(SaveQuestionRecordVo saveQuestionRecordVo) {
        QuestionRecord questionRecord = new QuestionRecord();
        questionRecord.setRecordId(Long.valueOf(saveQuestionRecordVo.getRecordId()));
        questionRecord.setFromUserId(Long.valueOf(saveQuestionRecordVo.getUserId()));
        questionRecord.setFromContent(saveQuestionRecordVo.getFromContent());
        questionRecord.setQuestionStatus("0");//待处理
        questionRecord.setFromTime(new Date());
        questionRecord.setType("1");
        questionRecordMapper.insertQuestionRecord(questionRecord);
        List<String> formIdArr = Arrays.asList(saveQuestionRecordVo.getMedicalFormIds().split(","));
        for(String s:formIdArr){
            QuestionRecordForm questionRecordForm = new QuestionRecordForm();
            questionRecordForm.setQuestionId(questionRecord.getQuestionId());
            questionRecordForm.setMedicalFormId(Long.valueOf(s));
            questionRecordForm.setRecordId(Long.valueOf(saveQuestionRecordVo.getRecordId()));
            //插入质疑表单关联表
            questionRecordFormMapper.insertQuestionRecordForm(questionRecordForm);
        }
        //更新随访质疑状态
        PatientRecord patientRecord = new PatientRecord();
        patientRecord.setRecordId(Long.valueOf(saveQuestionRecordVo.getRecordId()));
        patientRecord.setQuestionStatus("1");//质疑状态
        patientRecord.setQuestionTime(DateUtils.getNowDate());
        patientRecord.setFollowStatus("2");//进行中
        patientRecord.setFollowTipStatus(FollowTipStatusConstants.FOLLOW_UP_QUESTIONING_REMINDER);
        patientRecordMapper.updatePatientRecord(patientRecord);

        //患者随访质疑提醒
        PatientRecord prd = patientRecordMapper.selectPatientRecordByRecordId(saveQuestionRecordVo.getRecordId());
        PatientInfo pi = patientInfoMapper.selectPatientInfoByPatientId(prd.getPatientId());
        patientRecord.setAccountId(pi.getAccountId());
        String msgContent = "您有一条随访质疑请及时查看";
        AccountNotice accountNotice = new AccountNotice(patientRecord,msgContent,"10");
        accountNoticeMapper.insertAccountNotice(accountNotice);
//        umpushService.sendCustomizedcastMessage(2,String.valueOf(patientRecord.getAccountId()),msgContent,accountNotice.getExtendJson());

        //保存提醒消息
//        String msgContent = "您有一条随访质疑请及时查看";
//        msgCommonService.saveMessageForDoctor(6,msgContent,patientRecord);
        return 1;
    }

    /**
     * 修改质疑
     *
     * @param questionRecord 质疑
     * @return 结果
     */
    @Override
    public int updateQuestionRecord(QuestionRecord questionRecord) {
        return questionRecordMapper.updateQuestionRecord(questionRecord);
    }

    /**
     * 批量删除质疑
     *
     * @param questionIds 需要删除的质疑主键
     * @return 结果
     */
    @Override
    public int deleteQuestionRecordByQuestionIds(Long[] questionIds) {
        return questionRecordMapper.deleteQuestionRecordByQuestionIds(questionIds);
    }

    /**
     * 删除质疑信息
     *
     * @param questionId 质疑主键
     * @return 结果
     */
    @Override
    public int deleteQuestionRecordByQuestionId(Long questionId) {
        return questionRecordMapper.deleteQuestionRecordByQuestionId(questionId);
    }
}
