package com.dchealth.base.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 文件管理对象 t_fast_file
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-01-07
 */
public class FastFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** md5密钥 */
    @Excel(name = "md5密钥")
    private String fileMd5;

    /** 渠道 1 */
    @Excel(name = "渠道 1")
    private String channel;

    /** 下载路径 */
    @Excel(name = "下载路径")
    private String filePath;

    /** 组名 */
    @Excel(name = "组名")
    private String groupName;

    /** 远程路径 */
    @Excel(name = "远程路径")
    private String remoteFileName;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFileMd5(String fileMd5) 
    {
        this.fileMd5 = fileMd5;
    }

    public String getFileMd5() 
    {
        return fileMd5;
    }
    public void setChannel(String channel) 
    {
        this.channel = channel;
    }

    public String getChannel() 
    {
        return channel;
    }
    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }
    public void setGroupName(String groupName) 
    {
        this.groupName = groupName;
    }

    public String getGroupName() 
    {
        return groupName;
    }
    public void setRemoteFileName(String remoteFileName) 
    {
        this.remoteFileName = remoteFileName;
    }

    public String getRemoteFileName() 
    {
        return remoteFileName;
    }
    public void setCreateDate(Date createDate) 
    {
        this.createDate = createDate;
    }

    public Date getCreateDate() 
    {
        return createDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("fileMd5", getFileMd5())
            .append("channel", getChannel())
            .append("filePath", getFilePath())
            .append("groupName", getGroupName())
            .append("remoteFileName", getRemoteFileName())
            .append("createDate", getCreateDate())
            .toString();
    }
}
