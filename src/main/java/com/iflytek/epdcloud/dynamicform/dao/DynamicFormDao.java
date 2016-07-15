/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.iflytek.epdcloud.dynamicform.entity.CheckBoxField;
import com.iflytek.epdcloud.dynamicform.entity.DateTimeField;
import com.iflytek.epdcloud.dynamicform.entity.Field;
import com.iflytek.epdcloud.dynamicform.entity.FieldValue;
import com.iflytek.epdcloud.dynamicform.entity.Form;
import com.iflytek.epdcloud.dynamicform.entity.TextAreaField;
import com.iflytek.epdcloud.dynamicform.entity.TextField;
import com.iflytek.epdcloud.eduarchive.common.utils.UUIDUtils;

/**
 * @description：
 * 
 * @author suenlai
 * @date 2016年7月11日
 */
public class DynamicFormDao extends NamedParameterJdbcDaoSupport {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DynamicFormDao.class);

    @SuppressWarnings("unused")
    private DynamicFormDao() {};

    public DynamicFormDao(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    /**
     * @Description:查询表单定义
     * @param formId
     * @return
     */
    public Form get(String formId) {
        String sql = "SELECT t_form.id,t_form.entityName,t_form.code FROM t_form where id=:formId";
        Map<String, Object> args = new HashMap<>();
        args.put("formId", formId);
        try {
            Form entity = getNamedParameterJdbcTemplate().queryForObject(sql, args,
                    RowMapperFactory.FormRowMapper);
            return entity;
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("结果集为空", e);
            return null;
        }

    }

    /**
     * @Description:
     * @param entityName
     * @param code
     * @return
     */
    public Form getForm(String entityName, String code) {
        String sql =
                "SELECT t_form.id,t_form.entityName,t_form.code FROM t_form where entityName=:entityName and code=:code";
        Map<String, Object> args = new HashMap<>();
        args.put("entityName", entityName);
        args.put("code", code);
        try {
            Form entity = getNamedParameterJdbcTemplate().queryForObject(sql, args,
                    RowMapperFactory.FormRowMapper);
            return entity;
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("结果集为空", e);
            return null;
        }
    }

    /**
     * @Description:创建表单定义
     * @param formId
     * @return
     */
    public int save(Form form) {
        form.setId(UUIDUtils.getUUID());
        String sql = "INSERT into t_form (id,entityName,code) VALUES(:id,:entityName,:code)";
        Map<String, Object> args = new HashMap<>();
        args.put("id", form.getId());
        args.put("entityName", form.getEntityName());
        args.put("code", form.getCode());
        int effectRow = getNamedParameterJdbcTemplate().update(sql, args);
        return effectRow;
    }

    /**
     * @Description:只删除表单
     * @param formId
     * @return
     */
    public int deleteForm(String formId) {
        String sql = "DELETE FROM t_form where id=:formId";
        Map<String, Object> args = new HashMap<>();
        args.put("formId", formId);
        int effectedRows = getNamedParameterJdbcTemplate().update(sql, args);
        return effectedRows;
    }

    /**
     * 
     * 
     * @Description:清除表单关联所属字段定义
     * @param formId
     * @return
     */
    public int clearField(String formId) {
        String sql = "DELETE FROM t_field where formId=:formId";
        Map<String, Object> args = new HashMap<>();
        args.put("formId", formId);
        int effectedRows = getNamedParameterJdbcTemplate().update(sql, args);
        return effectedRows;
    }

    /**
     * 
     * @Description:添加动态字段定义
     * @param field
     * @return
     */
    public int addField(Field field) {
        field.setId(UUIDUtils.getUUID());
        String sql = "INSERT INTO t_field"
                + "(id,fieldTypeCode,formId,code,label,columns,required,defaultValue,sequence,height,width,options,dateFormat,maxSize)"
                + "VALUES(:id,:fieldTypeCode,:formId,:code,:label,:columns,:required,:defaultValue,:sequence,:height,:width,:options,:dateFormat,:maxSize);";


        Map<String, Object> args = new HashMap<>();
        args.put("id", field.getId());
        args.put("fieldTypeCode", field.getFieldType().getCode());
        args.put("formId", field.getForm().getId());
        args.put("code", field.getCode());
        args.put("label", field.getLabel());
        args.put("columns", field.getColumns());
        args.put("required", field.isRequired());
        args.put("defaultValue", field.getDefaultValue());
        args.put("sequence", field.getSequence());
        switch (field.getFieldType().getCode()) {
            case "datetime":
                DateTimeField dtf = (DateTimeField) field;
                args.put("dataFormat", dtf.getDateFormat());
                break;
            case "text":
                TextField tf = (TextField) field;
                args.put("maxSize", tf.getMaxSize());
                break;
            case "textarea":
                TextAreaField taf = (TextAreaField) field;
                args.put("maxSize", taf.getMaxSize());
                args.put("height", taf.getHeight());
                args.put("width", taf.getWidth());

                break;
            case "checkbox":
                CheckBoxField cbf = (CheckBoxField) field;
                args.put("options", cbf.getOptions());
                break;
            default:
                break;
        }

        int effectedRows = getNamedParameterJdbcTemplate().update(sql, args);
        return effectedRows;
    }


    /**
     * 
     * @Description: 列出指定的动态表单的所有的字段定义
     * @param formId
     * @return
     */
    public List<Field> listField(String formId) {
        String sql =
                "SELECT id,fieldTypeCode,formId,code,label,columns,required,defaultValue,sequence,height,width,options,dateFormat,maxSize FROM t_field where formId=:formId";
        Map<String, Object> args = new HashMap<>();
        args.put("formId", formId);
        List<Field> result =
                getNamedParameterJdbcTemplate().query(sql, args, RowMapperFactory.fieldRowMapper);
        return result;
    }

    /**
     * @Description:
     * @param field
     * @return
     */
    public int removeField(String fieldId) {
        String sql = "DELETE FROM t_field where  id=:id";
        Map<String, Object> args = new HashMap<>();
        args.put("id", fieldId);
        int effectedRows = getNamedParameterJdbcTemplate().update(sql, args);
        return effectedRows;
    }

    /**
     * @Description:
     * @param entityName
     * @param entityId
     * @return
     */
    public List<FieldValue> listFieldValue(String entityName, String entityId) {
        String sql =
                "SELECT id,fieldTypeCode,entityName,entityId,key,val FROM t_fieldValue where entityName=:entityName and entityId=:entityId";
        Map<String, Object> args = new HashMap<>();
        args.put("entityName", entityName);
        args.put("entityId", entityId);
        List<FieldValue> result = getNamedParameterJdbcTemplate().query(sql, args,
                RowMapperFactory.fieldValueRowMapper);
        return result;
    }

    /**
     * 
     * @Description:清除业务实体关联的动态字段值
     * @param entityName
     * @param entityId
     * @return
     */
    public int cleanFieldValue(String entityName, String entityId) {
        String sql = "DELETE FROM t_fieldValue where entityName=:entityName and entityId=:entityId";
        Map<String, Object> args = new HashMap<>();
        args.put("entityName", entityName);
        args.put("entityId", entityId);
        int effectedRows = getNamedParameterJdbcTemplate().update(sql, args);
        return effectedRows;
    }

    /**
     * 设置业务实体关联的动态字段值
     * 
     * @Description:
     * @param fieldValues
     * @return
     */
    public int addFieldValue(List<FieldValue> fieldValues) {
        @SuppressWarnings("unchecked")
        HashMap<String, String>[] argsArray = new HashMap[fieldValues.size()];

        HashMap<String, String> args = null;
        int idx = 0;
        for (FieldValue fv : fieldValues) {
            fv.setId(UUIDUtils.getUUID());
            args = new HashMap<>();
            args.put("id", fv.getId());
            args.put("fieldTypeCode", fv.getFieldTypeCode());
            args.put("entityName", fv.getEntityName());
            args.put("entityId", fv.getEntityId());
            args.put("key", fv.getKey());
            args.put("val", fv.getVal());
            argsArray[idx++] = args;
        }
        String sql =
                "INSERT INTO t_fieldValue (id,entityId,entityName,key,val) VALUES(:id,:entityId,:entityName,:key,:val)";
        int[] effectedRows = getNamedParameterJdbcTemplate().batchUpdate(sql, argsArray);
        return effectedRows.length;
    }



}
