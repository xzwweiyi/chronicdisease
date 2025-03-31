package com.dchealth.cro.form.mapper;

import com.dchealth.cro.form.domain.DynamicCommonProperty;
import com.dchealth.cro.form.domain.form.DynamicForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DynamicCommonMapper {

    /**
     * 动态查询数据执行sql
     * @param dynamicForm
     * @return
     */
    public List<DynamicCommonProperty> selectBySql(DynamicForm dynamicForm);

    /**
     * 执行新增SQL
     * @param sql
     */
    public int executeInsertSql(@Param("sql") String sql);

    /**
     * 执行删除SQL
     * @param sql
     * @return
     */
    public int executeDeleteSql(@Param("sql") String sql);

}
