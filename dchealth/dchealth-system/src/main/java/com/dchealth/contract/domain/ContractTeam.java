package com.dchealth.contract.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 签约团队对象 contract_team
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class ContractTeam extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 团队名称 */
    private String teamName;

    /** 团队负责人姓名 */
    private String leaderName;

    /** 负责人电话 */
    private String leaderPhone;

    /** 团队成员（多人逗号分隔） */
    private String members;

    /** 服务区域 */
    private String serviceArea;

    /** 团队介绍 */
    private String teamIntro;

    /** 状态（0停用 1启用） */
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getTeamIntro() {
        return teamIntro;
    }

    public void setTeamIntro(String teamIntro) {
        this.teamIntro = teamIntro;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ContractTeam{id=" + id + "}";
    }
}
