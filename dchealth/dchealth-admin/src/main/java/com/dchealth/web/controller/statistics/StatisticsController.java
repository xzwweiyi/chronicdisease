package com.dchealth.web.controller.statistics;

import com.alibaba.fastjson2.JSONObject;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.domain.model.LoginUser;
import com.dchealth.statistics.domain.PatientDistribute;
import com.dchealth.statistics.domain.ResearchStatusOfNumStatisticsVo;
import com.dchealth.statistics.domain.StatisticsParam;
import com.dchealth.statistics.domain.StatisticsVo;
import com.dchealth.statistics.service.FollowStatisticsService;
import com.dchealth.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {

    @Autowired
    private FollowStatisticsService statisticsService;
    @Autowired
    private ISysUserService sysUserService;


    /**
     * 随访分布统计
     * @return
     */
    @PreAuthorize("@ss.hasPermi('statistics:followDistribute:list')")
    @GetMapping("/followDistribute")
    public Map<String, List<StatisticsVo>> followDistribute(StatisticsParam statisticsParam) {
        log.info("followDistribute param:{}", JSONObject.toJSONString(statisticsParam));
        LoginUser loginUser = getLoginUser();
        SysUser sysUser = loginUser.getUser();

        if (!loginUser.getUser().isAdmin()) {
            statisticsParam.setRoleDeptId(getDeptId());
            //新增部门权限判断逻辑 如果用户有部门权限则不设置用户id查询部门数据  否则设置用户id查询自己的数据
            if (!"1".equals(sysUser.getIsAlldeptResearch())) {
                statisticsParam.setUserId(sysUser.getUserId());
            }
        }
        return statisticsService.followDistribute(statisticsParam);
    }

    /**
     * 患者分布统计
     * @return
     */
    @PreAuthorize("@ss.hasPermi('statistics:patientDistribute:list')")
    @GetMapping("/patientDistribute")
    public Map<String, List<StatisticsVo>> patientDistribute(StatisticsParam statisticsParam) {
        log.info("patientDistribute param:{}", JSONObject.toJSONString(statisticsParam));
        LoginUser loginUser = getLoginUser();
        //1.如果用户是管理员不设置部门和用户信息查询所有数据
        //2.如果用户不是管理员
        //   前端有进行部门设置则查询设置的部门的数据
        //   前端没进行部门设置则查询自己部门的数据
        //   如果用户有部门权限则不设置用户id查询整个部门数据
        //   如果用户没有部门权限则设置用户id查询自己的数据
        if(!loginUser.getUser().isAdmin()) {
            if(Objects.isNull(statisticsParam.getDeptId())) {
                statisticsParam.setDeptId(getDeptId());
            }
            if(!"1".equals(loginUser.getUser().getIsAlldeptResearch())) {
                statisticsParam.setUserId(loginUser.getUserId());
            }
        }
        return statisticsService.patientDistribute(statisticsParam);
    }

    /**
     * 院级全景图
     * @return
     */
    @PreAuthorize("@ss.hasPermi('statistics:patientDistribute:list')")
    @GetMapping("/hospitalView")
    public Map<String, Object> hospitalView() {
        return statisticsService.hospitalView();
    }


    /**
     * 统计进行中的队列和已关闭的队列数
     *
     * @param statisticsParam
     * @return
     */
    @PreAuthorize("@ss.hasPermi('statistics:patientDistribute:list')")
    @GetMapping("/researchStatusNum")
    public List<ResearchStatusOfNumStatisticsVo> researchStatusNum(StatisticsParam statisticsParam) {
        log.info("researchStatusNum param:{}", JSONObject.toJSONString(statisticsParam));
        LoginUser loginUser = getLoginUser();
        //1.如果用户是管理员科室的用户 不设置部门和用户信息查询所有数据
        //2.如果用户不是管理员科室的用户
        //   如果用户有部门权限或者用户是管理员 则不设置用户id查询整个部门数据
        //   如果用户没有部门权限且不是管理员 则设置用户id查询自己的数据
        //   前端有进行部门设置则查询设置的部门的数据
        //   前端没进行部门设置则查询自己部门的数据
        SysUser sysUser = loginUser.getUser();
        //不是管理员科室查看自己的数据
        if(!sysUser.getDeptId().equals(100L)){
            if (Objects.equals("1", sysUser.getIsAlldeptResearch()) || Objects.equals(loginUser.getUserId(), 100L)) {
                //如果有部门权限 或是管理员可以查看科室下所有数据不设置userid可查看所有数据
            } else {
                //没有部门权限且不是管理员设置userId查询用户自己的数据
                statisticsParam.setUserId(loginUser.getUserId());
            }
            if(statisticsParam.getDeptId() == null){
                statisticsParam.setDeptId(sysUser.getDeptId());
            }
        } else {
            //管理员可是查看所有科室数据信息
        }
        return statisticsService.researchStatusNum(statisticsParam);
    }


    /**
     * 统计患者来源省市
     *
     * @param distributeParam
     * @return
     */
    @PreAuthorize("@ss.hasPermi('statistics:queryPatientDistribute:list')")
    @GetMapping("/queryPatientDistribute")
    public AjaxResult queryPatientDistribute(PatientDistribute distributeParam) {
        log.info("queryPatientDistribute param:{}", JSONObject.toJSONString(distributeParam));
        return AjaxResult.success(statisticsService.queryPatientDistribute(distributeParam));
    }
}
