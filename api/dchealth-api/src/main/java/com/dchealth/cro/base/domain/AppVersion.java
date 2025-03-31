package com.dchealth.cro.base.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * app版本对象 t_app_version
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-03
 */
public class AppVersion  implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** null */
    private Integer id;

    /** 类型 */
    private String appType;

    /** 版本号 */
    private String appVersion;

    /** 备注 */
    private String remarks;

    /** 下载路径 */
    private String downloadUrl;

    /** 强制更新 */
    private String status;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setAppType(String appType) 
    {
        this.appType = appType;
    }

    public String getAppType() 
    {
        return appType;
    }
    public void setAppVersion(String appVersion) 
    {
        this.appVersion = appVersion;
    }

    public String getAppVersion() 
    {
        return appVersion;
    }
    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
    }
    public void setDownloadUrl(String downloadUrl) 
    {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadUrl() 
    {
        return downloadUrl;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("appType", getAppType())
            .append("appVersion", getAppVersion())
            .append("remarks", getRemarks())
            .append("downloadUrl", getDownloadUrl())
            .append("status", getStatus())
            .toString();
    }
}
