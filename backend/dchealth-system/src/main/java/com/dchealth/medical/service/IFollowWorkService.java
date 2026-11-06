package com.dchealth.medical.service;

import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.model.LoginUser;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.form.*;
import com.dchealth.medical.domain.vo.*;
import com.dchealth.research.domain.ResearchInfo;

import java.util.List;
import java.util.Map;

/**
 * 随访工作站业务层
 */
public interface IFollowWorkService {

    /**
     * 详情
     * @param followWorkDetailsForm
     * @return
     */
    public PatientDetailVO details(FollowWorkDetailsForm followWorkDetailsForm);

    public PatientRecordDetailVo getRecordDetails(FollowWorkDetailsForm detailsForm);

    /**
     * 任务树
     * @return
     */
    public FollowWorkTree tree(Long userId);

//    //lys
//    /**
//     * 任务树根节点
//     * @return
//     */
//    FollowWorkTree treeRoot(Long userId);
//
//    /**
//     * 任务树子节点
//     * @param userId
//     * @param parentNodeId
//     * @return
//     */
//    List<FollowWorkTree> treeChildren(Long userId, String parentNodeId);
//    //lys

    //lys2
    /**
     * 根节点和病种节点树
     * @return
     */
    public FollowWorkTree rootTree(Long userId,String nodeName);

    /**
     * 根据病种ID加载子树
     * @return
     */
    public FollowWorkTree diseaseTree(Long userId, Long diseaseId);
    //lys2

    /**
     * 评价任务树
     * @return
     */
    public EvaluateTree evaluateTree(String type, Long userId);

    /**
     * 评价列表
     * @param evaluateForm
     * @return
     */
    public List<EvaluateVO> evaluateList(EvaluateForm evaluateForm);

    /**
     * 评价工作
     * @param evaluateWorkForm
     * @return
     */
    public int evaluateWork(EvaluateWorkForm evaluateWorkForm);

    /**
     * 入组列表数据
     * @param followWorkListForm
     * @return
     */
    public List<FollowWorkListVO> enrollmentList(FollowWorkListForm followWorkListForm);

    /**
     * 基线
     * @param followWorkListForm
     * @return
     */
    public List<FollowWorkListVO> baseList(FollowWorkListForm followWorkListForm);

    /**
     * 随访处理(质疑)
     * @param followWorkListForm
     * @return
     */
    public List<FollowWorkListVO> handleList(FollowWorkListForm followWorkListForm);

    public List<String> handleStageList(FollowWorkListForm followWorkListForm);

    /**
     * 超窗
     * @param followWorkListForm
     * @return
     */
    public List<FollowWorkListVO> overList(FollowWorkListForm followWorkListForm);

    public List<String> overStageList(FollowWorkListForm followWorkListForm);

    /**
     * 预警
     * @param followWorkListForm
     * @return
     */
    public List<FollowWorkListVO> warnList(FollowWorkListForm followWorkListForm);

    public List<String> warnStageList(FollowWorkListForm followWorkListForm);

    /**
     * 待分配
     * @param followWorkListForm
     * @return
     */
    public List<FollowWorkListVO> allocatedList(FollowWorkListForm followWorkListForm);

    /**
     * 确认入组/修改入组/待分配入组
     * @param workEnrollmentHandleForm
     * @return
     */
    public int enrollmentHandle(FollowWorkEnrollmentHandleForm workEnrollmentHandleForm);

    public String isCall(Long recordId);

    /**
     * 待分配入组操作
     * @param workEnrollmentHandleForm
     * @return
     */
    public int allocatedHandle(FollowWorkEnrollmentHandleForm workEnrollmentHandleForm);

    /**
     * 病种队列下拉
     * @return
     */
    public List<ResearchDiseasesSelect> diseasesResearchSelect(Long userId);

    /**
     * 随访超窗处理
     * @param followOverTimeHandleForm
     * @return
     */
    public int visitTimeHandle(FollowOverTimeHandleForm followOverTimeHandleForm);

    /**
     * 提交随访意见
     * @param followWorkHandleForm
     * @return
     */
    public int submitOpinionHandle(FollowWorkHandleForm followWorkHandleForm);


    List<String> getResearchInfosOfperson(Long patientId);

    /**
     * 医生代填，保存与提交功能
     * @param infoForm 表中数据
     * @return
     */
    public AjaxResult saveOrUpdatePatientRecord(PatientRecordSaveUpdateInfoForm infoForm, LoginUser loginUser);


    public void createPopNotice(ResearchInfo researchInfo, String patientId);

    public void clearFollowRecord(PatientInfo patientInfo);

    /**
     * 档案管理-随访管理：查询某患者的待随访与已完成随访节点
     *
     * @param cardId 身份证号（mp_person.card_id）
     * @return key=pending 待随访节点集合 / key=completed 已完成随访节点集合
     */
    public Map<String, List<FollowWorkListVO>> patientFollowNodes(String cardId);

    /**
     * 查询患者联系方式（手机号、公众号 openId）
     *
     * @param patientId 患者ID
     * @return 联系方式
     */
    public PatientContactVO getPatientContact(Long patientId);

    /**
     * 一键拨打电话（语音盒）
     *
     * @param patientId 患者ID
     * @return true 拨号请求已触发
     */
    public boolean callPatient(Long patientId);

    /**
     * 发送微信随访提醒
     *
     * @param patientId 患者ID
     * @param content   提醒内容
     * @return true 发送成功
     */
    public boolean sendWechatFollowReminder(Long patientId, String content);
}
