package com.dchealth.dynamic.templete;

import com.dchealth.template.domain.vo.FMedicalValueVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.dchealth.dynamic.DynamicConstant.*;
import static com.dchealth.dynamic.DynamicConstant.COLUMN_TYPE_TIME;

/**
 * 动态表单
 */
@Service
public class DynamicFroms extends DynamicBase{

    String TABLE_NAME_PRE = "res_dynamic_";

    String TABLE_NAME_PRE_LOG = "res_dynamic_log_";

    @Override
    public void buildDynamicTableName() {
        tableNamePre = TABLE_NAME_PRE;
        List<String> tableNames = dynamicTableMapper.querySamePreTable(TABLE_NAME_PRE);
        samePreTables.addAll(tableNames.stream().filter(s -> isNum(s.replace(TABLE_NAME_PRE, "")))
                .collect(Collectors.toList()));
    }

    /**
     * 构建表字段
     */
    @Override
    public void buildDynamicColumn() {
        if (CollectionUtils.isNotEmpty(samePreTables)) {
            return;
        }
        columnList.add(buildColumnPrimaryKey("id", COLUMN_TYPE_BIGINT, true));
        columnList.add(buildColumnDefaultNull("record_id", COLUMN_TYPE_BIGINT, "档案Id", true));
        columnList.add(buildColumnDefaultNull("form_id", COLUMN_TYPE_BIGINT, "表单ID", true));
        columnList.add(buildColumnDefaultNull("type", COLUMN_TYPE_CHAR, "来源(1-患者，2-EDC)"));
        columnList.add(buildColumnDefaultNull("number", COLUMN_TYPE_VARCHAR64, "序号"));
        columnList.add(buildColumnDefaultNull("property_id", COLUMN_TYPE_BIGINT, "属性Id"));
        columnList.add(buildColumnDefaultNull("property_code", COLUMN_TYPE_VARCHAR64, "属性code"));
        columnList.add(buildColumnDefaultNull("property_value", COLUMN_TYPE_VARCHAR256, "属性值"));
        columnList.add(buildColumnDefaultNull("other", COLUMN_TYPE_VARCHAR256, "其它属性值"));
        columnList.add(buildColumnWithDefaultValue("create_by", COLUMN_TYPE_VARCHAR64, " ", "创建者"));
        columnList.add(buildColumnDefaultNull("create_time", COLUMN_TYPE_TIME, "创建时间"));
        columnList.add(buildColumnWithDefaultValue("update_by", COLUMN_TYPE_VARCHAR64, " ", "更新者"));
        columnList.add(buildColumnDefaultNull("update_time", COLUMN_TYPE_TIME, "更新时间"));
    }

    /**
     * 查询动态表单信息
     * @param researchId
     * @param fromId
     * @return
     */
    public String buildSelectSql(Long researchId, Long fromId, Long recordId){
        StringBuilder builder = new StringBuilder();
        builder.append("select id,record_id recordId,form_id formId,type,number,property_id propertyId,property_code propertyCode,property_value propertyValue ,other from ");
        builder.append(TABLE_NAME_PRE + researchId);
        builder.append(" where ");
        builder.append(" form_id = " + fromId);
        builder.append(" and record_id = " + recordId);
        return builder.toString();
    }

    public String buildSelectSql(Long researchId, Long fromId, Long recordId,String type){
        StringBuilder builder = new StringBuilder();
        builder.append("select id,record_id recordId,form_id formId,type,number,property_id propertyId,property_code propertyCode,property_value propertyValue ,other from ");
        builder.append(TABLE_NAME_PRE + researchId);
        builder.append(" where ");
        builder.append(" form_id = " + fromId);
        builder.append(" and type = " + type);
        builder.append(" and record_id = " + recordId);
        return builder.toString();
    }

    public String buildSelectSql(Long researchId, Long recordId){
        StringBuilder builder = new StringBuilder();
        builder.append("select id,record_id recordId,form_id formId,type,number,property_id propertyId,property_code propertyCode,property_value propertyValue ,other from ");
        builder.append(TABLE_NAME_PRE + researchId);
        builder.append(" where ");
        builder.append(" record_id = " + recordId);
        return builder.toString();
    }

    /**
     * 构建存储语句
     * @param vo
     */
    public String buildInsertSql(FMedicalValueVo vo){
        StringBuilder builder = new StringBuilder();
        builder.append("insert into " + TABLE_NAME_PRE + vo.getResearchId());
        builder.append("(record_id, form_id, type, number, property_id, property_code, property_value, other, create_by, create_time) values(");
        builder.append(vo.getRecordId() + ", ");
        builder.append(vo.getFromId() + ", ");
        builder.append("'" + vo.getType() + "', ");
        builder.append("'" + vo.getNumber() + "', ");
        builder.append(vo.getPropertyId() + ", ");
        builder.append("'" + vo.getPropertyCode() + "', ");
        builder.append("'" + vo.getPropertyValue() + "', ");
        builder.append("'" + vo.getOther() + "', ");
        builder.append("'" + vo.getCreateBy() + "', ");
        builder.append("now())");
        return builder.toString();
    }

    /**
     * 构建log存储语句
     * @param vo
     */
    public String buildInsertLogSql(FMedicalValueVo vo){
        StringBuilder builder = new StringBuilder();
        builder.append("insert into " + TABLE_NAME_PRE_LOG + vo.getResearchId());
        builder.append("(record_id, form_id, type, number, property_id, property_code, property_value, create_by, create_time, update_by, update_time) values(");
        builder.append(vo.getRecordId() + ", ");
        builder.append(vo.getFromId() + ", ");
        builder.append("'" + vo.getType() + "', ");
        builder.append("'" + vo.getNumber() + "', ");
        builder.append(vo.getPropertyId() + ", ");
        builder.append("'" + vo.getPropertyCode() + "', ");
        builder.append("'" + vo.getPropertyValue() + "', ");
        // builder.append("'" + vo.getOther() + "', ");
        builder.append("'" + vo.getCreateBy() + "', ");
        builder.append("now(),");
        builder.append("'" + vo.getUpdateBy() + "', ");
        builder.append("now())");
        return builder.toString();
    }

    /**
     * 查询动态表单信息
     * @param researchId
     * @param fromId
     * @return
     */
    public String buildSelectSql1(Long researchId, Long fromId, Long recordId,Long propertyId){
        StringBuilder builder = new StringBuilder();
        builder.append("select id,record_id recordId,form_id formId,type,number,property_id propertyId,property_code propertyCode,property_value propertyValue ,other from ");
        builder.append(TABLE_NAME_PRE + researchId);
        builder.append(" where ");
        builder.append(" form_id = " + fromId);
        builder.append(" and record_id = " + recordId);
        builder.append(" and property_id = " + propertyId);
        return builder.toString();
    }

    public String buildSelectSql1(Long researchId, Long fromId, Long recordId,Long propertyId,String type){
        StringBuilder builder = new StringBuilder();
        builder.append("select id,record_id recordId,form_id formId,type,number,property_id propertyId,property_code propertyCode,property_value propertyValue ,other from ");
        builder.append(TABLE_NAME_PRE + researchId);
        builder.append(" where ");
        builder.append(" form_id = " + fromId);
        builder.append(" and type = " + type);
        builder.append(" and record_id = " + recordId);
        builder.append(" and property_id = " + propertyId);
        return builder.toString();
    }

    /**
     * 删除小文本信息
     * @param researchId
     * @param fromId
     * @return
     */
    public String buildDelSql(Long researchId, Long fromId, Long recordId,Long propertyId){
        StringBuilder builder = new StringBuilder();
        builder.append(" delete from ");
        builder.append(TABLE_NAME_PRE + researchId);
        builder.append(" where ");
        builder.append(" form_id = " + fromId);
        builder.append(" and record_id = " + recordId);
        builder.append(" and property_id = " + propertyId);
        return builder.toString();
    }

    public String buildDelSql(Long researchId, Long fromId, Long recordId,Long propertyId,String type){
        StringBuilder builder = new StringBuilder();
        builder.append(" delete from ");
        builder.append(TABLE_NAME_PRE + researchId);
        builder.append(" where ");
        builder.append(" form_id = " + fromId);
        builder.append(" and type = " + type);
        builder.append(" and record_id = " + recordId);
        builder.append(" and property_id = " + propertyId);
        return builder.toString();
    }

}
