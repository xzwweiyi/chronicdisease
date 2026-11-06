package com.dchealth.research.domain;

import com.dchealth.common.core.domain.BaseEntity;
import com.dchealth.research.domain.vo.RoleDeptVo;
import com.dchealth.research.domain.vo.RoleModuleVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResearchRole extends BaseEntity {

    private Long roleId;//角色id
    private Long researchId;//队列id
    private String roleName;//角色名称
    private String roleKey;//角色权限字符串
    private Integer roleSort;//显示顺序
    private String dataScope;//数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
    private String status;//角色状态（0正常 1停用）
    private String delFlag;//删除标志（0代表存在 2代表删除）

    private Integer userQuatity;//角色下账号数量

    /** 部门组（数据权限） */
    private Long[] deptIds;

    /** app资源组 */
    private Long[] sourceIds;

    List<RoleDeptVo> roleDepts  = new ArrayList<>();//修改保存部门权限

    List<RoleModuleVo> roleModuleVos = new ArrayList<>();;//修改保存模块权限

}
