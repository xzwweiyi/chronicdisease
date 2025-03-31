package com.dchealth.redis.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import java.util.Objects;

/**
 * 【请填写功能名称】对象 sys_redis_config
 *
 * @author ruoyi
 * @date 2022-06-20
 */
public class SysRedisConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 主机
     */
    @Excel(name = "主机")
    private String host;

    /**
     * 端口
     */
    @Excel(name = "端口")
    private int port;

    /**
     * 密码
     */
    @Excel(name = "密码")
    private String password;

    private String newPassword;

    /**
     * 别名
     */
    @Excel(name = "别名")
    private String name;

    /**
     * 状态 1禁用0启用
     */
    @Excel(name = "状态 1禁用0启用")
    private Integer status;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public SysRedisConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public SysRedisConfig() {
    }

    @Override
    public String toString() {
        return "SysRedisConfig{" +
                "id=" + id +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }

    public SysRedisConfig copyHostAndPort() {
        return new SysRedisConfig(host, port);
    }


    public void maskPassword() {
        String maskPassword = "";
        this.setNewPassword(maskPassword);
        this.setPassword(maskPassword);
    }

    public boolean isUsable() {
        return Objects.isNull(status) ? true : status == 0;
    }
}

