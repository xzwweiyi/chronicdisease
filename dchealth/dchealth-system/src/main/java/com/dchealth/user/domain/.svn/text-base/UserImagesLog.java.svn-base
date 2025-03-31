package com.dchealth.user.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 user_images_log
 *
 * @author ruoyi
 * @date 2022-06-13
 */
public class UserImagesLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户图片id */
    @Excel(name = "用户图片id")
    private Long userImagesId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 图片路径 */
    @Excel(name = "图片路径")
    private String imageUrl;

    /** 图片类型 1荣誉图片 */
    @Excel(name = "图片类型 1荣誉图片")
    private String type;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 记录创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记录创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createByTime;

    /** 记录更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记录更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateByTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserImagesId(Long userImagesId)
    {
        this.userImagesId = userImagesId;
    }

    public Long getUserImagesId()
    {
        return userImagesId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setSort(Long sort)
    {
        this.sort = sort;
    }

    public Long getSort()
    {
        return sort;
    }
    public void setCreateByTime(Date createByTime)
    {
        this.createByTime = createByTime;
    }

    public Date getCreateByTime()
    {
        return createByTime;
    }
    public void setUpdateByTime(Date updateByTime)
    {
        this.updateByTime = updateByTime;
    }

    public Date getUpdateByTime()
    {
        return updateByTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userImagesId", getUserImagesId())
                .append("userId", getUserId())
                .append("imageUrl", getImageUrl())
                .append("type", getType())
                .append("sort", getSort())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateBy", getUpdateBy())
                .append("createByTime", getCreateByTime())
                .append("updateByTime", getUpdateByTime())
                .toString();
    }
}
