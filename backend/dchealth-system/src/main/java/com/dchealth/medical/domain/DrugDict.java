package com.dchealth.medical.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 药品字典对象 drug_dict
 * 
 * @author wangpf
 * @date 2025-08-13
 */
@Data
public class DrugDict extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 药品代码 */
    @Excel(name = "药品代码")
    private String drugCode;

    /** 药品名称 */
    @Excel(name = "药品名称")
    private String drugName;

    /** 反映药品的含量信息 */
    @Excel(name = "反映药品的含量信息")
    private String drugSpec;

    /** 如片、支等 */
    @Excel(name = "如片、支等")
    private String drugUnit;

    /** 如针剂、片剂等，使用规范描述 */
    @Excel(name = "如针剂、片剂等，使用规范描述")
    private String drugForm;

    /** 按药品的毒理分类，如普通、毒麻、精神等 */
    @Excel(name = "按药品的毒理分类，如普通、毒麻、精神等")
    private String pharmacologyType;

    /** 剂量的单位，如MG、ML等 */
    @Excel(name = "剂量的单位，如MG、ML等")
    private String doseUnit;

    /** 反映是否药品及何类药品：1-西药2-中草药 3-中成药 5-辅料 6-试剂 9-其它 */
    @Excel(name = "反映是否药品及何类药品：1-西药2-中草药 3-中成药 5-辅料 6-试剂 9-其它")
    private String drugMaterialType;

    /** 价格 */
    @Excel(name = "价格")
    private String costs;

    /** 厂商 */
    @Excel(name = "厂商")
    private String manufacturer;

    /** 主要成分 */
    @Excel(name = "主要成分")
    private String mainComponents;

    /** 商品名 */
    @Excel(name = "商品名")
    private String tradeName;

    /** 供应商 */
    @Excel(name = "供应商")
    private String supplier;

    /** 数据提供商表ID */
    @Excel(name = "数据提供商表ID")
    private Long dataVendorId;

    /** 溯源标识 */
    @Excel(name = "溯源标识")
    private String atlasFlag;



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("drugCode", getDrugCode())
            .append("drugName", getDrugName())
            .append("drugSpec", getDrugSpec())
            .append("drugUnit", getDrugUnit())
            .append("drugForm", getDrugForm())
            .append("pharmacologyType", getPharmacologyType())
            .append("doseUnit", getDoseUnit())
            .append("drugMaterialType", getDrugMaterialType())
            .append("costs", getCosts())
            .append("manufacturer", getManufacturer())
            .append("mainComponents", getMainComponents())
            .append("tradeName", getTradeName())
            .append("supplier", getSupplier())
            .append("dataVendorId", getDataVendorId())
            .append("atlasFlag", getAtlasFlag())
            .toString();
    }
}
