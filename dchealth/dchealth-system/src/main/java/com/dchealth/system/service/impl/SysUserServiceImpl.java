package com.dchealth.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Validator;

import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordOperLog;
import com.dchealth.medical.domain.vo.PatientEnrollmentVo;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.medical.mapper.PatientRecordOperLogMapper;
import com.dchealth.research.domain.ResearchUserRole;
import com.dchealth.research.mapper.ResearchUserRoleMapper;
import com.dchealth.system.domain.vo.MedicalWorkloadVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.dchealth.common.annotation.DataScope;
import com.dchealth.common.constant.UserConstants;
import com.dchealth.common.core.domain.entity.SysRole;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.SecurityUtils;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.common.utils.bean.BeanValidators;
import com.dchealth.common.utils.spring.SpringUtils;
import com.dchealth.system.domain.SysPost;
import com.dchealth.system.domain.SysUserPost;
import com.dchealth.system.domain.SysUserRole;
import com.dchealth.system.mapper.SysPostMapper;
import com.dchealth.system.mapper.SysRoleMapper;
import com.dchealth.system.mapper.SysUserMapper;
import com.dchealth.system.mapper.SysUserPostMapper;
import com.dchealth.system.mapper.SysUserRoleMapper;
import com.dchealth.system.service.ISysConfigService;
import com.dchealth.system.service.ISysUserService;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    protected Validator validator;

    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private ResearchUserRoleMapper researchUserRoleMapper;
    @Autowired
    private PatientRecordOperLogMapper patientRecordOperLogMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;

    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
//    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user)
    {
        return userMapper.selectUserList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user)
    {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     * @param user
     * @return
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList2(SysUser user)
    {
        return userMapper.selectAllocatedList2(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user)
    {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName)
    {
        return userMapper.selectUserByUserName(userName);
    }

    @Override
    public SysUser selectUserByMobile(String mobile)
    {
        return userMapper.selectUserByMobile(mobile);
    }

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId)
    {
        SysUser sysUser = userMapper.selectUserById(userId);

        SysUserPost sysUserPost = userPostMapper.selectPostByUserId(userId);
        if(sysUserPost!=null){
            sysUser.setPostId(sysUserPost.getPostId());
        }
        return sysUser;
    }

    /**
     * 查询用户所属角色组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName)
    {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list))
        {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 查询用户所属岗位组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName)
    {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list))
        {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    /**
     * 校验用户名称是否唯一
     * 
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName)
    {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0)
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     * 
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users))
            {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    /**
     * 新增保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user)
    {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 注册用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user)
    {
        return userMapper.insertUser(user) > 0;
    }

    /**
     * 修改保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user)
    {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 用户授权角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds)
    {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     * 
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar)
    {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password)
    {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     * 
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user)
    {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     * 
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user)
    {
//        Long[] posts = user.getPostIds();
//        if (StringUtils.isNotNull(posts))
//        {
//            // 新增用户与岗位管理
//            List<SysUserPost> list = new ArrayList<SysUserPost>();
//            for (Long postId : posts)
//            {
//                SysUserPost up = new SysUserPost();
//                up.setUserId(user.getUserId());
//                up.setPostId(postId);
//                list.add(up);
//            }
//            if (list.size() > 0)
//            {
//                userPostMapper.batchUserPost(list);
//            }
//        }

        if (user.getPostId()!=null){
            SysUserPost up = new SysUserPost();
            up.setUserId(user.getUserId());
            up.setPostId(user.getPostId());
            userPostMapper.insertSysUserPost(up);
        }
    }

    /**
     * 新增用户角色信息
     * 
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds)
    {
        if (StringUtils.isNotNull(roleIds))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roleIds)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId)
    {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds)
    {
        for (Long userId : userIds)
        {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPost(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList)
        {
            try
            {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u))
                {
                    BeanValidators.validateWithException(validator, user);
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, user);
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public List<MedicalWorkloadVo> medicalWorkloadList(SysUser user) {
        List<MedicalWorkloadVo> list = userMapper.medicalWorkloadList(user);
        //查询所有的患者
        PatientInfo pio = new PatientInfo();
        pio.setEnrollmentHandlerStatus("3");
        List<PatientEnrollmentVo> patientEnrollmentVoList = patientInfoMapper.selectEnrollmentPatientInfoList(pio);
        Map<Long,List<PatientEnrollmentVo>> patietnsMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(patientEnrollmentVoList)){
            patietnsMap = patientEnrollmentVoList.stream().filter(x -> x.getResearchId()!=null).collect(Collectors.groupingBy(PatientEnrollmentVo::getResearchId));
        }
        List<ResearchUserRole> researchUserRoleList = researchUserRoleMapper.selectListGroupByUserIdAndResearchId(new ResearchUserRole());
        Map<Long,List<ResearchUserRole>> userRolesMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(researchUserRoleList)){
            userRolesMap = researchUserRoleList.stream().filter(x -> x.getUserId()!=null).collect(Collectors.groupingBy(ResearchUserRole::getUserId));
        }


        PatientRecord pr = new PatientRecord();
        pr.setFollowStatus("3");
        pr.setStartTime(user.getStartTime());
        pr.setEndTime(user.getEndTime());
        List<PatientRecord> patientRecordList = patientRecordMapper.selectList(pr);
        Map<Long,List<PatientRecord>> patientRecordsMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(patientRecordList)){
            patientRecordsMap = patientRecordList.stream().filter(x -> x.getFollowOpinionUserId()!=null).collect(Collectors.groupingBy(PatientRecord::getFollowOpinionUserId));
        }

        if(!CollectionUtils.isEmpty(list)){
            PatientRecordOperLog patientRecordOperLog = new PatientRecordOperLog();
            patientRecordOperLog.setStartTime(user.getStartTime());
            patientRecordOperLog.setEndTime(user.getEndTime());
            List<PatientRecordOperLog> operLogList = patientRecordOperLogMapper.selectPatientRecordOperLogList(patientRecordOperLog);
            Map<Long,List<PatientRecordOperLog>> operLogMap = new HashMap<>();
            if(!CollectionUtils.isEmpty(operLogList)){
                operLogMap = operLogList.stream().filter(x -> x.getUserId() !=null).collect(Collectors.groupingBy(PatientRecordOperLog::getUserId));
            }

            for(MedicalWorkloadVo m:list){
                int patientNums = 0;
                if(userRolesMap.containsKey(m.getUserId())){
                    List<ResearchUserRole> roleList = userRolesMap.get(m.getUserId());
                    if(roleList.size()>0){
                        for(ResearchUserRole r:roleList){
                            if(patietnsMap.containsKey(r.getResearchId())){
                                List<PatientEnrollmentVo> enrollmentVoList = patietnsMap.get(r.getResearchId());
                                patientNums = patientNums + enrollmentVoList.size();
                            }
                        }
                    }

                }
                m.setPatientNums(patientNums);

                //处理随访量
                if(patientRecordsMap.containsKey(m.getUserId())){
                    List<PatientRecord> patientRecords = patientRecordsMap.get(m.getUserId());
                    m.setSetOpinionHandleNums(patientRecords.size());

                    //质效评价-回复速度均分
                    Integer prescriptionBackNum=0;
                    BigDecimal prescriptionBackBd = new BigDecimal(0);

                    //质效评价-回复质量均分
                    Integer qualityBackNum=0;
                    BigDecimal qualityBackBd = new BigDecimal(0);

                    //患者评价-回复速度均分
                    Integer prescriptionNum=0;
                    BigDecimal prescriptionBd = new BigDecimal(0);

                    //患者评价-回复质量均分
                    Integer qualityNum=0;
                    BigDecimal qualityBd = new BigDecimal(0);

                    for(PatientRecord r:patientRecords){
                        if(StringUtils.isNotBlankAndNotEmpty(r.getEvaluatePrescriptionBack()) && !"0".equals(r.getEvaluatePrescriptionBack())){
                            prescriptionBackNum ++;
                            prescriptionBackBd = prescriptionBackBd.add(new BigDecimal(r.getEvaluatePrescriptionBack()));
                        }
                        if(StringUtils.isNotBlankAndNotEmpty(r.getEvaluateQualityBack()) && !"0".equals(r.getEvaluateQualityBack())){
                            qualityBackNum ++;
                            qualityBackBd = qualityBackBd.add(new BigDecimal(r.getEvaluateQualityBack()));
                        }

                        if(StringUtils.isNotBlankAndNotEmpty(r.getEvaluatePrescription()) && !"0".equals(r.getEvaluatePrescription())){
                            prescriptionNum ++;
                            prescriptionBd = prescriptionBd.add(new BigDecimal(r.getEvaluatePrescription()));
                        }
                        if(StringUtils.isNotBlankAndNotEmpty(r.getEvaluateQuality()) && !"0".equals(r.getEvaluateQuality())){
                            qualityNum ++;
                            qualityBd = qualityBd.add(new BigDecimal(r.getEvaluateQuality()));
                        }
                    }
                    if(prescriptionBackNum>0){
                        Double prescriptionBackBdResult = prescriptionBackBd.divide(new BigDecimal(prescriptionBackNum),1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        m.setPrescriptionBackScore(prescriptionBackBdResult.toString());
                    }

                    if(qualityBackNum>0){
                        Double qualityBackResult = qualityBackBd.divide(new BigDecimal(qualityBackNum),1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        m.setQualityBackScore(qualityBackResult.toString());
                    }

                    if(prescriptionNum>0){
                        Double prescriptionBdResult = prescriptionBd.divide(new BigDecimal(prescriptionNum),1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        m.setPrescriptionScore(prescriptionBdResult.toString());
                    }

                    if(qualityNum>0){
                        Double qualityResult = qualityBd.divide(new BigDecimal(qualityNum),1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        m.setQualityScore(qualityResult.toString());
                    }

                }

                if(operLogMap.containsKey(m.getUserId())){
                    List<PatientRecordOperLog> patientRecordOperLogList = operLogMap.get(m.getUserId());
                    if(patientRecordOperLogList.size()>0){
                        //入组审批
                        int enrollmentHandleNums = patientRecordOperLogList.stream().filter( s-> s.getFormType().equals("1")).collect(Collectors.toList()).size();
                        //处理预警量
                        int warnGuideNums = patientRecordOperLogList.stream().filter( s-> s.getFormType().equals("2")).collect(Collectors.toList()).size();
                        //处理超窗量
                        int visitTimeHandleNums = patientRecordOperLogList.stream().filter( s-> s.getFormType().equals("3")).collect(Collectors.toList()).size();
                        m.setEnrollmentHandleNums(enrollmentHandleNums);
                        m.setWarnGuideNums(warnGuideNums);
                        m.setVisitTimeHandleNums(visitTimeHandleNums);
                    }
                }
            }
        }
        return list;
    }

    /*public List<MedicalWorkloadVo> medicalWorkloadList(SysUser user) {
        List<MedicalWorkloadVo> list = userMapper.medicalWorkloadList(user);
        //查询所有的患者
        PatientInfo pio = new PatientInfo();
        pio.setEnrollmentHandlerStatus("3");
        List<PatientEnrollmentVo> patientEnrollmentVoList = patientInfoMapper.selectEnrollmentPatientInfoList(pio);
        Map<Long,List<PatientEnrollmentVo>> patietnsMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(patientEnrollmentVoList)){
            patietnsMap = patientEnrollmentVoList.stream().filter(x -> x.getResearchId()!=null).collect(Collectors.groupingBy(PatientEnrollmentVo::getResearchId));
        }
        List<ResearchUserRole> researchUserRoleList = researchUserRoleMapper.selectListGroupByUserIdAndResearchId(new ResearchUserRole());
        Map<Long,List<ResearchUserRole>> userRolesMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(researchUserRoleList)){
            userRolesMap = researchUserRoleList.stream().filter(x -> x.getUserId()!=null).collect(Collectors.groupingBy(ResearchUserRole::getUserId));
        }


        PatientRecord pr = new PatientRecord();
        pr.setFollowStatus("3");
        List<PatientRecord> patientRecordList = patientRecordMapper.selectList(pr);
        Map<Long,List<PatientRecord>> patientRecordsMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(patientRecordList)){
            patientRecordsMap = patientRecordList.stream().filter(x -> x.getFollowOpinionUserId()!=null).collect(Collectors.groupingBy(PatientRecord::getFollowOpinionUserId));
        }

        if(!CollectionUtils.isEmpty(list)){
            List<PatientRecordOperLog> operLogList = patientRecordOperLogMapper.selectPatientRecordOperLogList(new PatientRecordOperLog());
            Map<Long,List<PatientRecordOperLog>> operLogMap = new HashMap<>();
            if(!CollectionUtils.isEmpty(operLogList)){
                operLogMap = operLogList.stream().filter(x -> x.getUserId() !=null).collect(Collectors.groupingBy(PatientRecordOperLog::getUserId));
            }

            for(MedicalWorkloadVo m:list){
                int patientNums = 0;
                if(userRolesMap.containsKey(m.getUserId())){
                    List<ResearchUserRole> roleList = userRolesMap.get(m.getUserId());
                    if(roleList.size()>0){
                        for(ResearchUserRole r:roleList){
                            if(patietnsMap.containsKey(r.getResearchId())){
                                List<PatientEnrollmentVo> enrollmentVoList = patietnsMap.get(r.getResearchId());
                                patientNums = patientNums + enrollmentVoList.size();
                            }
                        }
                    }

                }
                m.setPatientNums(patientNums);

                //处理随访量
                if(patientRecordsMap.containsKey(m.getUserId())){
                    List<PatientRecord> patientRecords = patientRecordsMap.get(m.getUserId());
                    m.setSetOpinionHandleNums(patientRecords.size());

                    //质效评价-回复速度均分
                    Integer prescriptionBackNum=0;
                    BigDecimal prescriptionBackBd = new BigDecimal(0);

                    //质效评价-回复质量均分
                    Integer qualityBackNum=0;
                    BigDecimal qualityBackBd = new BigDecimal(0);

                    //患者评价-回复速度均分
                    Integer prescriptionNum=0;
                    BigDecimal prescriptionBd = new BigDecimal(0);

                    //患者评价-回复质量均分
                    Integer qualityNum=0;
                    BigDecimal qualityBd = new BigDecimal(0);

                    for(PatientRecord r:patientRecords){
                        if(StringUtils.isNotBlankAndNotEmpty(r.getEvaluatePrescriptionBack()) && !"0".equals(r.getEvaluatePrescriptionBack())){
                            prescriptionBackNum ++;
                            prescriptionBackBd = prescriptionBackBd.add(new BigDecimal(r.getEvaluatePrescriptionBack()));
                        }
                        if(StringUtils.isNotBlankAndNotEmpty(r.getEvaluateQualityBack()) && !"0".equals(r.getEvaluateQualityBack())){
                            qualityBackNum ++;
                            qualityBackBd = qualityBackBd.add(new BigDecimal(r.getEvaluateQualityBack()));
                        }

                        if(StringUtils.isNotBlankAndNotEmpty(r.getEvaluatePrescription()) && !"0".equals(r.getEvaluatePrescription())){
                            prescriptionNum ++;
                            prescriptionBd = prescriptionBd.add(new BigDecimal(r.getEvaluatePrescription()));
                        }
                        if(StringUtils.isNotBlankAndNotEmpty(r.getEvaluateQuality()) && !"0".equals(r.getEvaluateQuality())){
                            qualityNum ++;
                            qualityBd = qualityBd.add(new BigDecimal(r.getEvaluateQuality()));
                        }
                    }
                    if(prescriptionBackNum>0){
                        Double prescriptionBackBdResult = prescriptionBackBd.divide(new BigDecimal(prescriptionBackNum),1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        m.setPrescriptionBackScore(prescriptionBackBdResult.toString());
                    }

                    if(qualityBackNum>0){
                        Double qualityBackResult = qualityBackBd.divide(new BigDecimal(qualityBackNum),1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        m.setQualityBackScore(qualityBackResult.toString());
                    }

                    if(prescriptionNum>0){
                        Double prescriptionBdResult = prescriptionBd.divide(new BigDecimal(prescriptionNum),1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        m.setPrescriptionScore(prescriptionBdResult.toString());
                    }

                    if(qualityNum>0){
                        Double qualityResult = qualityBd.divide(new BigDecimal(qualityNum),1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        m.setQualityScore(qualityResult.toString());
                    }

                }

                if(operLogMap.containsKey(m.getUserId())){
                    List<PatientRecordOperLog> patientRecordOperLogList = operLogMap.get(m.getUserId());
                    if(patientRecordOperLogList.size()>0){
                        //入组审批
                        int enrollmentHandleNums = patientRecordOperLogList.stream().filter( s-> s.getFormType().equals("1")).collect(Collectors.toList()).size();
                        //处理预警量
                        int warnGuideNums = patientRecordOperLogList.stream().filter( s-> s.getFormType().equals("2")).collect(Collectors.toList()).size();
                        //处理超窗量
                        int visitTimeHandleNums = patientRecordOperLogList.stream().filter( s-> s.getFormType().equals("3")).collect(Collectors.toList()).size();
                        m.setEnrollmentHandleNums(enrollmentHandleNums);
                        m.setWarnGuideNums(warnGuideNums);
                        m.setVisitTimeHandleNums(visitTimeHandleNums);
                    }
                }
            }
        }
        return list;
    }*/
}
