package com.dchealth.etl.domain.target;

import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 数据同步信息(TdSyncDataInfo)实体类
 *
 * @author makejava
 * @since 2023-03-27 16:17:57
 */
public class TdSyncDataInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 957794536322363719L;
    /**
     * 自增主键
     */
    private Integer id;
    /**
     * 同步表名
     */
    private String tableName;
    /**
     * 对应表的已同步数据id
     */
    private String dataRecords;
    /**
     * 数据日期
     */
    private String recordDate;
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

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
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

