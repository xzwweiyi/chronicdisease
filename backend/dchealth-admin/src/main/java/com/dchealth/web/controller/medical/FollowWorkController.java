package com.dchealth.web.controller.medical;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.dchealth.base.service.TencentService;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.constant.Constants;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.domain.model.LoginUser;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.core.redis.RedisCache;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.listener.FollowTreeEvent;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.form.*;
import com.dchealth.medical.domain.vo.EvaluateTree;
import com.dchealth.medical.domain.vo.FollowWorkTree;
import com.dchealth.medical.service.IFollowWorkService;
import com.dchealth.medical.service.IPatientInfoService;
import com.dchealth.medical.service.IPatientRecordService;
import com.dchealth.research.domain.constant.ResearchContants;
import com.dchealth.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 随访工作站
 */
@RestController
@RequestMapping("/follow/work")
public class FollowWorkController extends BaseController {

    @Autowired
    private IFollowWorkService followWorkService;
    @Autowired
    private IPatientInfoService patientInfoService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IPatientRecordService patientRecordService;
    @Autowired
    private TencentService tencentService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ApplicationContext applicationContext;


    /**
     * 详情
     *
     * @return
     */
    @GetMapping("/details")
    public AjaxResult details(FollowWorkDetailsForm followWorkDetailsForm) {
        return AjaxResult.success(followWorkService.details(followWorkDetailsForm));
    }

    /**
     * 随访工作台-详情（医生代填）
     *
     * @param detailsForm
     * @return
     */
    @GetMapping(value = "/recordDetails")
    public AjaxResult recordDetails(FollowWorkDetailsForm detailsForm) {
        return AjaxResult.success(followWorkService.getRecordDetails(detailsForm));
    }

    /**
     * 随访工作台-填写与提交（医生代填）
     *
     * @param detailsForm
     * @return
     */
    @PostMapping(value = "/saveOrUpdatePatientRecord")
    @Log(title = "随访工作台-填写与提交（医生代填）", businessType = BusinessType.INSERT)
    public AjaxResult saveOrUpdatePatientRecord(@RequestBody PatientRecordSaveUpdateInfoForm detailsForm) {
        LoginUser loginUser = getLoginUser();
        return AjaxResult.success(followWorkService.saveOrUpdatePatientRecord(detailsForm, loginUser));
    }

//	/**
//	 * 随访任务树
//	 *
//	 * @return
//	 */
//	@GetMapping("/tree")
//	public AjaxResult tree() {
//		Long userId = getUserId();
//		FollowWorkTree followWorkTree = redisCache.getCacheObject(Constants.FOLLOW_TREE_KEY + userId);
////		FollowWorkTree followWorkTree = null;
//
//		if (Objects.isNull(followWorkTree)){
//			logger.info("followWorkTree:"+userId+"查询数据库");
//			followWorkTree = followWorkService.tree(userId);
//			redisCache.setCacheObject(Constants.FOLLOW_TREE_KEY + userId,followWorkTree);
//		}
//		return AjaxResult.success(followWorkTree);
//
//	}

    /*//lys
     *//**
     * 随访任务树节点子节点
     *
     * @return
     *//*
	@GetMapping("/tree/children")
	public AjaxResult treeChildren(@RequestParam String parentNodeId) {
		Long userId = getUserId();
		List<FollowWorkTree> children = followWorkService.treeChildren(userId, parentNodeId);
		return AjaxResult.success(children);
	}

	*//**
     * 随访任务树根节点
     *
     * @return
     *//*
	@GetMapping("/tree/root")
	public AjaxResult treeRoot() {
		Long userId = getUserId();
		FollowWorkTree followWorkTree = redisCache.getCacheObject(Constants.FOLLOW_TREE_KEY + userId);
		//FollowWorkTree followWorkTree = null;
		if (Objects.isNull(followWorkTree)){
			logger.info("followWorkTree:"+userId+"查询数据库");
			followWorkTree = followWorkService.treeRoot(userId);
			redisCache.setCacheObject(Constants.FOLLOW_TREE_KEY + userId, followWorkTree);
		}
		return AjaxResult.success(followWorkTree);
	}
	//lys*/
    //lys2

    /**
     * 随访任务树根节点和病种节点
     *
     * @return
     */
    @GetMapping("/rootTree")
    public AjaxResult rootTree(String diseasesName) {
        Long userId = getUserId();
//		FollowWorkTree followWorkTree = redisCache.getCacheObject(Constants.FOLLOW_TREE_ROOT_KEY + userId);

//		if (Objects.isNull(followWorkTree)){
        logger.info("followWorkTree root:" + userId + "查询数据库");
        FollowWorkTree followWorkTree = followWorkService.rootTree(userId,diseasesName);

        // 获取子节点的条数
        List<FollowWorkTree> childrenList = followWorkTree.getChildrenList();
//        Object total = redisCache.getCacheObject(Constants.TOTAL_TREE_LEVEL_NUM + userId);
//        if (total != null) {
//            followWorkTree.setHandleNum(Integer.valueOf(total.toString()));
//        } else {
//            applicationContext.publishEvent(new FollowTreeEvent(this, userId, Boolean.TRUE));
//        }
        childrenList.forEach(children -> {
            Object one = redisCache.getCacheObject(Constants.ONE_TREE_LEVEL_NUM + userId + ":" + children.getDiseaseId());
            if (ObjectUtil.isNotNull(one)) {
                children.setHandleNum(Integer.valueOf(one.toString()));
            } else {
                applicationContext.publishEvent(new FollowTreeEvent(this, userId, Boolean.FALSE));
            }
        });


//		}
        return AjaxResult.success(followWorkTree);
    }

    /**
     * 根据病种ID加载子树
     *
     * @return
     */
    @GetMapping("/diseaseTree/{diseaseId}")
    public AjaxResult diseaseTree(@PathVariable Long diseaseId) {
        Long userId = getUserId();
//        String cacheKey = Constants.FOLLOW_TREE_DISEASE_KEY + userId + ":" + diseaseId;
//        FollowWorkTree diseaseSubTree = redisCache.getCacheObject(cacheKey);

//        if (Objects.isNull(diseaseSubTree)) {
        logger.info("diseaseTree:" + userId + "-" + diseaseId + "查询数据库");
        FollowWorkTree diseaseSubTree = followWorkService.diseaseTree(userId, diseaseId);
//            redisCache.setCacheObject(cacheKey, diseaseSubTree);
//        }
        return AjaxResult.success(diseaseSubTree);
    }
    //lys2

    /**
     * 评价管理树
     *
     * @return
     */
    @GetMapping("/evaluate/tree/{type}")
    public AjaxResult evaluateTree(@PathVariable("type") String type) {
        Long userId = getUserId();
        String cacheValue = redisCache.getCacheString(Constants.FOLLOW_TREE_KEY + userId + ":" + type);
        if (StringUtils.isNotBlankAndNotEmpty(cacheValue)) {
            return AjaxResult.success(JSON.parseObject(cacheValue, EvaluateTree.class));
        } else {
            EvaluateTree followWorkTree = followWorkService.evaluateTree(type, userId);
            if (followWorkTree != null) {
                redisCache.cacheString(Constants.FOLLOW_TREE_KEY + userId + ":" + type, JSON.toJSONString(followWorkTree),
                        ResearchContants.CACHE_TIME, TimeUnit.MINUTES);
            }
            return AjaxResult.success(followWorkTree);
        }

    }

    /**
     * 评价列表
     *
     * @return
     */
    @GetMapping("/evaluate/list")
    public TableDataInfo evaluateList(EvaluateForm evaluateForm) {
        startPage();
        return getDataTable(followWorkService.evaluateList(evaluateForm));
    }

    /**
     * 评价提交
     *
     * @param evaluateWorkForm
     * @return
     */
    @PreAuthorize("@ss.hasPermi('follow:evaluate:handle')")
    @PostMapping("/evaluate/handle")
    public AjaxResult evaluateWork(@RequestBody EvaluateWorkForm evaluateWorkForm) {
        evaluateWorkForm.setUpdateBy(getUsername());
        return toAjax(followWorkService.evaluateWork(evaluateWorkForm));
    }

    /**
     * 入组列表 enrollmentHandlerStatus: 入组处理状态1待处理3已完成 researchId: 2037
     *
     * @return
     */
    @GetMapping("/enrollment/list")
    public TableDataInfo enrollmentList(FollowWorkListForm followWorkListForm) {
        startPage();
        return getDataTable(followWorkService.enrollmentList(followWorkListForm));
    }

    /**
     * 基线列表
     *
     * @return
     */
    @GetMapping("/base/list")
    public TableDataInfo baseList(FollowWorkListForm followWorkListForm) {
        startPage();
        return getDataTable(followWorkService.baseList(followWorkListForm));
    }

    /**
     * 处理列表
     *
     * @return
     */
    @GetMapping("/handle/list")
    public TableDataInfo handleList(FollowWorkListForm followWorkListForm) {
        startPage();
        return getDataTable(followWorkService.handleList(followWorkListForm));
    }

    /**
     * 随访  随访阶段下拉框集合
     *
     * @return
     */
    @GetMapping("/handle/stageList")
    public AjaxResult handleStageList(FollowWorkListForm followWorkListForm) {
        return AjaxResult.success(followWorkService.handleStageList(followWorkListForm));
    }

    /**
     * 档案管理-随访管理：查询某患者的待随访与已完成随访节点
     *
     * @param cardId 身份证号（mp_person.card_id）
     * @return {pending:[...], completed:[...]}
     */
    @GetMapping("/patient/nodes")
    public AjaxResult patientNodes(@RequestParam String cardId) {
        return AjaxResult.success(followWorkService.patientFollowNodes(cardId));
    }

    /**
     * 重点关注患者打标签 / 取消重点关注
     *
     * @param patientId 患者ID
     * @param focusStatus 0取消 1重点关注
     * @return 结果
     */
    @PostMapping("/focus/toggle")
    public AjaxResult focusToggle(@RequestParam Long patientId, @RequestParam String focusStatus) {
        if (patientId == null || (!"0".equals(focusStatus) && !"1".equals(focusStatus))) {
            return AjaxResult.error("参数不合法");
        }
        return toAjax(patientInfoService.updateFocusStatus(patientId, focusStatus));
    }

    /**
     * 超窗列表
     *
     * @return
     */
    @GetMapping("/over/list")
    public TableDataInfo overList(FollowWorkListForm followWorkListForm) {
        startPage();
        return getDataTable(followWorkService.overList(followWorkListForm));
    }

    /**
     * 超窗  随访阶段下拉框集合
     *
     * @return
     */
    @GetMapping("/over/stageList")
    public AjaxResult overStageList(FollowWorkListForm followWorkListForm) {
        return AjaxResult.success(followWorkService.overStageList(followWorkListForm));
    }

    /**
     * 预警列表
     *
     * @return
     */
    @GetMapping("/warn/list")
    public TableDataInfo warnList(FollowWorkListForm followWorkListForm) {
        startPage();
        return getDataTable(followWorkService.warnList(followWorkListForm));
    }

    /**
     * 预警  随访阶段下拉框集合
     *
     * @return
     */
    @GetMapping("/warn/stageList")
    public AjaxResult warnStageList(FollowWorkListForm followWorkListForm) {
        return AjaxResult.success(followWorkService.warnStageList(followWorkListForm));
    }

    /**
     * 待分配患者列表
     *
     * @return
     */
    @GetMapping("/allocated/list")
    public TableDataInfo allocatedList(FollowWorkListForm followWorkListForm) {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        followWorkListForm.setDeptId(sysUser.getDeptId());
        startPage();
        return getDataTable(followWorkService.allocatedList(followWorkListForm));
    }

    /**
     * 确认入组/修改入组/待分配入组
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('follow:patient_enrollment:handle')")
    @PostMapping("/enrollment/handle")
    @Log(title = "确认入组", businessType = BusinessType.INSERT)
    public AjaxResult enrollmentHandle(@RequestBody FollowWorkEnrollmentHandleForm workEnrollmentHandleForm) {
        workEnrollmentHandleForm.setUserId(getUserId());
        workEnrollmentHandleForm.setUpdateBy(getUsername());
        PatientInfo patientInfo = patientInfoService
                .selectPatientInfoByPatientId(workEnrollmentHandleForm.getPatientId());

        if ("2".equals(workEnrollmentHandleForm.getHandleType())) {
            // 更换队列时候需要判断是否符合入该队列的条件
            if (patientInfo != null) {
                List<PatientInfo> list = patientInfoService.selectPatientInfoByPersonId(patientInfo.getPersonId());
                if (list != null && list.size() > 0) {
                    Optional<PatientInfo> infoOptional = list.stream().filter(
                                    x -> x.getResearchId().longValue() == workEnrollmentHandleForm.getResearchId().longValue())
                            .findAny();
                    if (infoOptional.isPresent()) {
                        return AjaxResult.error("入组失败，已在该队列中");
                    }
                }
            }
        }

        if (!Objects.isNull(patientInfo) && "3".equals(workEnrollmentHandleForm.getHandleType())
                && !Objects.isNull(patientInfo.getRefuseCount()) && patientInfo.getRefuseCount() > 2
                && "1".equals(patientInfo.getChannelType())) {
            return error("当前科室当前病种当前队列拒绝入组的次数上限2次，超过2次的患者可以纳排但是不能确认/修改入组，可以取消入组，只能扫码入组");
        }
        return toAjax(followWorkService.enrollmentHandle(workEnrollmentHandleForm));
    }

    @PostMapping("/enrollment/walkup")
    @Log(title = "发送短信", businessType = BusinessType.INSERT)
    public AjaxResult enrollmentHandleWalkUp(String patientIds) {
        List<String> patientIdList = Arrays.asList(patientIds.split(","));
        for (String patientId : patientIdList) {
            tencentService.sendSmsLimitOne(patientId);
        }
        logger.info("patientId={}进行了短信提醒", patientIds);
        return AjaxResult.success();
    }

    @GetMapping("/enrollment/getResarchInfo/{patientId}")
    public AjaxResult enrollmentGetResarchInfo(@PathVariable("patientId") Long patientId) {
        return AjaxResult.success(followWorkService.getResearchInfosOfperson(patientId));
    }

    @GetMapping(value = "/isCall/{recordId}")
    public AjaxResult isCall(@PathVariable("recordId") Long recordId) {
        return AjaxResult.success(followWorkService.isCall(recordId));
    }

    /**
     * 一键拨打电话（语音盒）
     *
     * @param patientId 患者ID
     */
    @PostMapping("/call")
    @Log(title = "随访工作站拨打电话", businessType = BusinessType.OTHER)
    public AjaxResult call(@RequestParam Long patientId) {
        return toAjax(followWorkService.callPatient(patientId));
    }

    /**
     * 发送短信（复用腾讯云短信）
     *
     * @param patientId 患者ID
     */
    @PostMapping("/sendSms")
    @Log(title = "随访工作站发送短信", businessType = BusinessType.INSERT)
    public AjaxResult sendSms(@RequestParam Long patientId) {
        tencentService.sendSms(String.valueOf(patientId));
        return AjaxResult.success();
    }

    /**
     * 发送微信随访提醒
     *
     * @param patientId 患者ID
     * @param content   提醒内容
     */
    @PostMapping("/sendWechat")
    @Log(title = "随访工作站发送微信提醒", businessType = BusinessType.INSERT)
    public AjaxResult sendWechat(@RequestParam Long patientId, @RequestParam(required = false) String content) {
        followWorkService.sendWechatFollowReminder(patientId, content);
        return AjaxResult.success();
    }

    /**
     * 待分配入组操作
     *
     * @param workEnrollmentHandleForm
     * @return
     */
    @PreAuthorize("@ss.hasPermi('follow:patient_enrollment:handle')")
    @PostMapping("/allocated/handle")
    public AjaxResult allocatedHandle(@RequestBody FollowWorkEnrollmentHandleForm workEnrollmentHandleForm) {
        return toAjax(followWorkService.allocatedHandle(workEnrollmentHandleForm));
    }

    /**
     * 病种及队列联动下拉框
     *
     * @return
     */
    @GetMapping("/diseases/research/select")
    public AjaxResult diseasesResearchSelect() {
        return AjaxResult.success(followWorkService.diseasesResearchSelect(getUserId()));
    }

    /**
     * 超窗处理
     *
     * @param followOverTimeHandleForm
     * @return
     */
    @PreAuthorize("@ss.hasPermi('follow:over_time:handle')")
    @PostMapping("/over/time/handle")
    public AjaxResult visitTimeHandle(@RequestBody FollowOverTimeHandleForm followOverTimeHandleForm) {
        followOverTimeHandleForm.setUserId(getUserId());
        followOverTimeHandleForm.setUpdateBy(getUsername());
        return toAjax(followWorkService.visitTimeHandle(followOverTimeHandleForm));
    }

    /**
     * 提交意见
     *
     * @param followWorkHandleForm
     * @return
     */
    @PreAuthorize("@ss.hasPermi('follow:opinion:handle')")
    @PostMapping("/submit/opinion/handle")
    public AjaxResult submitOpinionHandle(@RequestBody FollowWorkHandleForm followWorkHandleForm) {
        logger.info("followWorkHandleForm:{}", JSON.toJSONString(followWorkHandleForm));
        followWorkHandleForm.setUpdateBy(getUsername());
        followWorkHandleForm.setUpdateUserId(getUserId());

        if ("2".equals(followWorkHandleForm.getAdjustType())) {
            // 更换队列
            PatientInfo patientInfo = patientInfoService
                    .selectPatientInfoByPatientId(followWorkHandleForm.getPatientInfoId());
            List<PatientInfo> list = patientInfoService.selectPatientInfoByPersonId(patientInfo.getPersonId());
            if (list != null && list.size() > 0) {
                Optional<PatientInfo> infoOptional = list.stream()
                        .filter(x -> x.getResearchId().longValue() == followWorkHandleForm.getResearchId().longValue())
                        .findAny();
                if (infoOptional.isPresent()) {
                    return AjaxResult.error("入组失败，已在该队列中");
                }
            }
        }

        PatientRecord prd = patientRecordService.selectPatientRecordByRecordId(followWorkHandleForm.getRecordId());
        if ("1".equals(prd.getWarnStatus()) || "2".equals(prd.getWarnStatus())) {
            return AjaxResult.error("此档案存在未处理预警,不能发表随访意见");
        }

        return toAjax(followWorkService.submitOpinionHandle(followWorkHandleForm));
    }


}
