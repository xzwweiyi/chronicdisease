package com.dchealth.cro.core.domain;

import lombok.Data;

import java.util.Date;

/**
 * 部门对象 sys_dept
 * 
 * @author lchbu
 * @date 2022-09-13
 */
@Data
public class SysDept
{
    private static final long serialVersionUID = 1L;

    /** 部门id */
    private Long deptId;

    /** 父部门id */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 部门名称/中心名称 */
    private String deptName;

    /** 显示顺序 */
    private Long orderNum;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 部门状态（0正常 1停用） */
    private String status;

    /** 部门类型 0 根节点 1中心医院 2 科室 3单位 4部门 */
    private String deptType;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;
}
