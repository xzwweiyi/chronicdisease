package com.dchealth.web.controller.medical;

import cn.hutool.core.collection.CollUtil;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.core.redis.RedisCache;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.compatibility.domain.MpDrugExposure;
import com.dchealth.compatibility.mapper.MpDrugExposureMapper;
import com.dchealth.compatibility.vo.MpDrugExposureVO;
import com.dchealth.listener.FollowTreeEvent;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.vo.EnrollmentListVo;
import com.dchealth.medical.domain.vo.MoveEnrollmentVo;
import com.dchealth.medical.domain.vo.PatientEnrollmentVo;
import com.dchealth.medical.dto.MoveEnrollmentBatchReqDto;
import com.dchealth.medical.service.IPatientInfoService;
import com.dchealth.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 患者信息Controller
 *
 * @author ruoyi
 * @date 2022-03-21
 */
@RestController
@RequestMapping("/medical/patientInfo")
public class PatientInfoController extends BaseController {
    @Autowired
    private IPatientInfoService patientInfoService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MpDrugExposureMapper mpDrugExposureMapper;

    /**
     * 查询患者信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(PatientInfo patientInfo) {
        patientInfo.setEnrollmentHandlerStatus("3");
        SysUser sysUser = sysUserService.selectUserById(getUserId());
//        patientInfo.setDeptId(sysUser.getDeptId());
        if ("1".equals(sysUser.getIsAlldeptResearch())) {
            patientInfo.setUserId(null);
            patientInfo.setDeptId(sysUser.getDeptId());
        } else {
            patientInfo.setUserId(sysUser.getUserId());
        }
        startPage();
        List<PatientInfo> list = patientInfoService.selectPatientInfoListByAuth(patientInfo);
        return getDataTable(list);
    }

    /**
     * 导出患者信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:info:export')")
    @Log(title = "患者信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PatientInfo patientInfo) {
        List<PatientInfo> list = patientInfoService.selectPatientInfoList(patientInfo);
        ExcelUtil<PatientInfo> util = new ExcelUtil<PatientInfo>(PatientInfo.class);
        util.exportExcel(response, list, "患者信息数据");
    }

    /**
     * 获取患者信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:query')")
    @GetMapping(value = "/{patientId}")
    public AjaxResult getInfo(@PathVariable("patientId") Long patientId) {
        return AjaxResult.success(patientInfoService.selectPatientInfoByPatientId(patientId));
    }

    /**
     * 新增患者信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:add')")
    @Log(title = "患者信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PatientInfo patientInfo) {
        return toAjax(patientInfoService.insertPatientInfo(patientInfo));
    }

    /**
     * 修改患者信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:edit')")
    @Log(title = "患者信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PatientInfo patientInfo) {
        return toAjax(patientInfoService.updatePatientInfo(patientInfo));
    }

    /**
     * 删除患者信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:remove')")
    @Log(title = "患者信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{patientIds}")
    public AjaxResult remove(@PathVariable Long[] patientIds) {
        return toAjax(patientInfoService.deletePatientInfoByPatientIds(patientIds));
    }

    /**
     * 查询患者信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:info:treeList')")
    @GetMapping("/treeList")
    public AjaxResult treeList(PatientInfo patientInfo) {
        return AjaxResult.success(patientInfoService.selectPatientInfoTreeList(patientInfo));
    }

    /**
     * 查询入组患者
     */
    @PreAuthorize("@ss.hasPermi('system:info:list')")
    @GetMapping("/enrollment")
    public TableDataInfo enrollment(PatientInfo patientInfo) {
        startPage();
        List<PatientEnrollmentVo> list = patientInfoService.selectEnrollmentPatientInfoList(patientInfo);
        return getDataTable(list);
    }
    /**
     * 查询入组患者信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:list')")
    @GetMapping("/patientEnrollmentInformation")
    public AjaxResult patientEnrollmentInformation(PatientInfo patientInfo) {
        List<PatientEnrollmentVo> list = patientInfoService.patientEnrollmentInformation(patientInfo);
        return AjaxResult.success(list);
    }
    /**
     * 查询未入组患者
     */
    @PreAuthorize("@ss.hasPermi('system:info:list')")
    @GetMapping("/noEnrollment")
    public TableDataInfo noEnrollment(PatientInfo patientInfo) {
        startPage();
        List<PatientEnrollmentVo> list = patientInfoService.selectNoEnrollmentPatientInfoList(patientInfo);
        return getDataTable(list);
    }

    /**
     * 查看患者用药信息
     */
    @PostMapping("/drugInfo")
    public AjaxResult drugInfo(@Validated @RequestBody MpDrugExposureVO mpDrugExposureVO) {
        MpDrugExposure mpDrugExposure = new MpDrugExposure();
        mpDrugExposure.setPersonId(mpDrugExposureVO.getPersonId());
        logger.info("查看患者用药信息 {}", mpDrugExposure);
        List<MpDrugExposure> list = mpDrugExposureMapper.selectPatientDrugInfoList(mpDrugExposure);
        return AjaxResult.success(list);
    }

    /**
     * 计划入组患者确认入组按钮
     *
     * @param patientInfo
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:info:add')")
    @PostMapping("/confirmEnrollment")
    public AjaxResult confirmEnrollment(@RequestBody PatientInfo patientInfo) {
        return patientInfoService.confirmEnrollment(patientInfo);
    }


    /**
     * 导入入组患者
     *
     * @param patientBaseInfos
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:info:add')")
    @PostMapping("/importEnrollment")
    public AjaxResult importEnrollment(@RequestBody List<MpPerson> patientBaseInfos) {
//        Long userId = getUserId();
//        redisCache.deleteObject(Constants.FOLLOW_TREE_KEY + userId);
        // todo 若是速度慢, 异步添加缓存, 并吧添加的能力提出出来 (定时&事件触发)
        int num = patientInfoService.importEnrollment(patientBaseInfos, getUsername());
        applicationContext.publishEvent(new FollowTreeEvent(getUserId(), getUserId(), false));
        return AjaxResult.success("入组" + num + "个患者");

    }

    /**
     * 删除入组患者
     *
     * @param patientIds
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:info:delete')")
    @DeleteMapping("/deleteEnrollment/{patientIds}")
    public AjaxResult deleteEnrollment(@PathVariable Long[] patientIds) {
        // 判断权限
        if (!patientInfoService.canDelete(getUserId(), patientIds)) {
            return AjaxResult.error("当前患者只能由队列创建者删除或者管理员删除");
        }
        int num = patientInfoService.deleteEnrollment(patientIds);
        applicationContext.publishEvent(new FollowTreeEvent(getUserId(), getUserId(), false));
        return AjaxResult.success("删除入组" + num + "个患者");

    }

    @PostMapping("/moveEnrollment")
    public AjaxResult moveEnrollment(@RequestBody MoveEnrollmentBatchReqDto reqDto) {
        int success = patientInfoService.batchMoveEnrollment(reqDto.getSelectedList());
        return AjaxResult.success("部分移动入组完成：成功 " + success + " 个");
    }

    @PostMapping("/enrollmentList")
    public AjaxResult enrollmentList(@RequestBody MoveEnrollmentVo moveEnrollmentVo) {

        List<EnrollmentListVo> list = patientInfoService.enrollmentList(moveEnrollmentVo);
        return AjaxResult.success(list);
    }
}
