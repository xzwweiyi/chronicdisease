package com.dchealth.screening.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 院外筛查记录对象 screening_outside
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class ScreeningOutside extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 居民姓名 */
    private String residentName;

    /** 身份证号 */
    private String idCard;

    /** 联系电话 */
    private String phone;

    /** 筛查日期 */
    private Date screeningDate;

    /** 筛查地点 */
    private String screeningLocation;

    /** 筛查结果 */
    private String screeningResult;

    /** 病种类型 */
    private String diseaseType;

    /** 处理人 */
    private String handler;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Date screeningDate) {
        this.screeningDate = screeningDate;
    }

    public String getScreeningLocation() {
        return screeningLocation;
    }

    public void setScreeningLocation(String screeningLocation) {
        this.screeningLocation = screeningLocation;
    }

    public String getScreeningResult() {
        return screeningResult;
    }

    public void setScreeningResult(String screeningResult) {
        this.screeningResult = screeningResult;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    @Override
    public String toString() {
        return "ScreeningOutside{id=" + id + "}";
    }
}
