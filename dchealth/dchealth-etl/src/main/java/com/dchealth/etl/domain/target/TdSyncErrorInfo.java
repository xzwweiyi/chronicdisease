package com.dchealth.etl.domain.target;

import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 唐都数据同步异常信息表(TdSyncErrorInfo)实体类
 *
 * @author makejava
 * @since 2023-03-27 16:22:02
 */
public class TdSyncErrorInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 626256644839066307L;
    
    private Integer id;
    /**
     * 同步表名
     */
    private String tableName;
    /**
     * 同步数据记录
     */
    private String dataRecords;
    /**
     * 异常信息
     */
    private String errorInfo;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 跟新时间
     */
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDataRecords() {
        return dataRecords;
    }

    public void setDataRecords(String dataRecords) {
        this.dataRecords = dataRecords;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}

