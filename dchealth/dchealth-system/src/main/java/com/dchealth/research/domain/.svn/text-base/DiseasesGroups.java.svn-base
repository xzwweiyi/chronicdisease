package com.dchealth.research.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class DiseasesGroups extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 病种id */
    private Long diseasesGroupsId;

    /** 病种名称 */
    @Excel(name = "病种名称")
    private String diseasesGroupsName;

    /** 是否删除 0未删除 1 删除 */
    @Excel(name = "是否删除 0未删除 1 删除")
    private String delFlag;

    //创建人userId
    private Long createUserId;

}
