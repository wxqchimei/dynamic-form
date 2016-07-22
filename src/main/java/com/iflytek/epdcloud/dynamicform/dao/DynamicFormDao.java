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
import com.iflytek.epdcloud.dynamicform.entity.RadioBoxField;
import com.iflytek.epdcloud.dynamicform.entity.SelectField;
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
    public Form getForm(String formId) {
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
    public int saveForm(Form form) {
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
                + "(id,fieldTypeCode,formId,code,label,columns,required,defaultValue,sequence,height,width,options,dateFormat,maxSize,validation)"
                + "VALUES(:id,:fieldTypeCode,:formId,:code,:label,:columns,:required,:defaultValue,:sequence,:height,:width,:options,:dateFormat,:maxSize,:validation);";


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
        args.put("dateFormat", null);
        args.put("maxSize", null);
        args.put("height", null);
        args.put("width", null);
        args.put("options", null);
        switch (field.getFieldType().getCode()) {
            case "datetime":
                DateTimeField dtf = (DateTimeField) field;
                args.put("dateFormat", dtf.getDateFormat());
                break;
            case "text":
                TextField tf = (TextField) field;
                args.put("maxSize", tf.getMaxSize());
                args.put("validation", tf.getValidation());
                break;
            case "textarea":
                TextAreaField taf = (TextAreaField) field;
                args.put("maxSize", taf.getMaxSize());
                args.put("height", taf.getHeight());
                args.put("width", taf.getWidth());

                break;
            case "radiobox":
                RadioBoxField rbf = (RadioBoxField) field;
                args.put("options", rbf.getOptions());
                break;
            case "checkbox":
                CheckBoxField cbf = (CheckBoxField) field;
                args.put("options", cbf.getOptions());
                break;
            case "select":
                SelectField sf = (SelectField) field;
                args.put("options", sf.getOptions());
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
                "SELECT id,fieldTypeCode,formId,code,label,columns,required,defaultValue,sequence,height,width,options,dateFormat,maxSize,validation FROM t_field where formId=:formId order by sequence asc,columns asc";
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
                "SELECT id,fieldTypeCode,entityName,fieldTypeCode,entityId,key,val FROM t_fieldValue where entityName=:entityName and entityId=:entityId";
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
            args.put("fieldTypeCode", fv.getFieldType().getCode());
            args.put("entityName", fv.getEntityName());
            args.put("entityId", fv.getEntityId());
            args.put("key", fv.getKey());
            args.put("val", fv.getVal());
            argsArray[idx++] = args;
        }
        String sql =
                "INSERT INTO t_fieldValue (id,entityId,entityName,fieldTypeCode,key,val) VALUES(:id,:entityId,:entityName,:fieldTypeCode,:key,:val)";
        int[] effectedRows = getNamedParameterJdbcTemplate().batchUpdate(sql, argsArray);
        return effectedRows.length;
    }

    /**
     * @Description:根据ID查Field
     * @return
     */
    public Field getField(String fieldId) {
        String sql =
                "SELECT id,fieldTypeCode,formId,code,label,columns,required,defaultValue,sequence,height,width,options,dateFormat,maxSize,validation FROM t_field where id=:fieldId";
        Map<String, Object> args = new HashMap<>();
        args.put("fieldId", fieldId);
        try {
            Field entity = getNamedParameterJdbcTemplate().queryForObject(sql, args,
                    RowMapperFactory.fieldRowMapper);
            return entity;
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("结果集为空", e);
            return null;
        }
    }

    /**
     * @Description:
     * @param formId
     * @param sw
     * @return
     */
    public List<Field> listField(String formId, String sw) {
        String sql =
                "SELECT id,fieldTypeCode,formId,code,label,columns,required,defaultValue,sequence,height,width,options,dateFormat,maxSize,validation FROM t_field where formId =:formId and label like '%:sw%' order by sequence asc";
        Map<String, Object> args = new HashMap<>();
        args.put("formId", formId);
        args.put("sw", sw);
        List<Field> result =
                getNamedParameterJdbcTemplate().query(sql, args, RowMapperFactory.fieldRowMapper);
        return result;
    }

    public int getCodeCount(String code, String formId) {
        String sql = "select count(0) ct from t_field where code=:code and formId=:formId";
        Map<String, String> args = new HashMap<String, String>();
        args.put("code", code);
        args.put("formId", formId);
        int res = getNamedParameterJdbcTemplate().queryForInt(sql, args);
        return res;
    }

    public List<Field> getFieldsByIds(List<String> ids) {
        String sql =
                "SELECT id,fieldTypeCode,formId,code,label,columns,required,defaultValue,sequence,height,width,options,dateFormat,maxSize,validation FROM t_field where id in (:ids)";
        Map<String, List<String>> args = new HashMap<String, List<String>>();
        args.put("ids", ids);
        List<Field> result =
                getNamedParameterJdbcTemplate().query(sql, args, RowMapperFactory.fieldRowMapper);
        return result;
    }

    public int addFields(List<Field> lf) {
        for (Field f : lf) {
            f.setId(UUIDUtils.getUUID());
        }
        String sql = "INSERT INTO t_field"
                + "(id,fieldTypeCode,formId,code,label,columns,required,defaultValue,sequence,height,width,options,dateFormat,maxSize,validation)"
                + "VALUES(:id,:fieldTypeCode,:formId,:code,:label,:columns,:required,:defaultValue,:sequence,:height,:width,:options,:dateFormat,:maxSize,:validation)";
        Map<String, Object>[] mss = new Map[lf.size()];
        for (int i = 0; i < lf.size(); i++) {
            Field field = lf.get(i);
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("id", field.getId());
            args.put("fieldTypeCode", field.getFieldType().getCode());
            args.put("formId", field.getForm().getId());
            args.put("code", field.getCode());
            args.put("label", field.getLabel());
            args.put("columns", field.getColumns());
            args.put("required", field.isRequired());
            args.put("defaultValue", field.getDefaultValue());
            args.put("sequence", field.getSequence());
            args.put("dateFormat", null);
            args.put("maxSize", null);
            args.put("height", null);
            args.put("width", null);
            args.put("options", null);
            switch (field.getFieldType().getCode()) {
                case "datetime":
                    DateTimeField dtf = (DateTimeField) field;
                    args.put("dateFormat", dtf.getDateFormat());
                    break;
                case "text":
                    TextField tf = (TextField) field;
                    args.put("maxSize", tf.getMaxSize());
                    args.put("validation", tf.getValidation());
                    break;
                case "textarea":
                    TextAreaField taf = (TextAreaField) field;
                    args.put("maxSize", taf.getMaxSize());
                    args.put("height", taf.getHeight());
                    args.put("width", taf.getWidth());

                    break;
                case "radiobox":
                    RadioBoxField rbf = (RadioBoxField) field;
                    args.put("options", rbf.getOptions());
                    break;
                case "checkbox":
                    CheckBoxField cbf = (CheckBoxField) field;
                    args.put("options", cbf.getOptions());
                    break;
                case "select":
                    SelectField sf = (SelectField) field;
                    args.put("options", sf.getOptions());
                    break;
                default:
                    break;
            }
            mss[i] = args;
        }
        int[] res = getNamedParameterJdbcTemplate().batchUpdate(sql, mss);
        return res[0];
    }

    /**
     * @Description:
     * @param field
     * @return
     */
    public int updateField(Field field) {
        StringBuilder sqlbuilder = new StringBuilder(
                "UPDATE t_field SET label=:label,columns=:columns,required=:required,defaultValue=:defaultValue");

        Map<String, Object> args = new HashMap<>();
        args.put("id", field.getId());
        args.put("label", field.getLabel());
        args.put("columns", field.getColumns());
        args.put("required", field.isRequired());
        args.put("defaultValue", field.getDefaultValue());
        switch (field.getFieldType().getCode()) {
            case "datetime":
                DateTimeField dtf = (DateTimeField) field;
                args.put("dateFormat", dtf.getDateFormat());
                sqlbuilder.append(" ,dateFormat=:dateFormat ");
                break;
            case "text":
                TextField tf = (TextField) field;
                args.put("maxSize", tf.getMaxSize());
                sqlbuilder.append(" ,maxSize=:maxSize ");
                break;
            case "textarea":
                TextAreaField taf = (TextAreaField) field;
                args.put("maxSize", taf.getMaxSize());
                args.put("height", taf.getHeight());
                args.put("width", taf.getWidth());
                sqlbuilder.append(" ,maxSize=:maxSize ");
                sqlbuilder.append(" ,height=:height ");
                sqlbuilder.append(" ,width=:width ");

                break;
            case "radiobox":
                RadioBoxField rbf = (RadioBoxField) field;
                args.put("options", rbf.getOptions());
                sqlbuilder.append(" ,options=:options ");
                break;
            case "checkbox":
                CheckBoxField cbf = (CheckBoxField) field;
                args.put("options", cbf.getOptions());
                sqlbuilder.append(" ,options=:options ");
                break;
            case "select":
                SelectField sf = (SelectField) field;
                args.put("options", sf.getOptions());
                sqlbuilder.append(" ,options=:options ");
                break;
            default:
                String errorMsg = "不存在字段类型:" + field.getFieldType().getCode();
                LOGGER.error(errorMsg);
                throw new RuntimeException(errorMsg);
        }
        sqlbuilder.append(" where id=:id");

        int effectedRows = getNamedParameterJdbcTemplate().update(sqlbuilder.toString(), args);
        return effectedRows;
    }

    /**
     * @Description:
     * @param formId
     * @return
     */
    public int countField(String formId) {
        String sql = "select count(0) ct from t_field where  formId=:formId";
        Map<String, String> args = new HashMap<String, String>();
        args.put("formId", formId);
        int res = getNamedParameterJdbcTemplate().queryForInt(sql, args);
        return res;
    }

    /**
     * @Description:
     * @param fieldIds
     * @return
     */
    public int sortField(String[] fieldIds) {
        @SuppressWarnings("unchecked")
        HashMap<String, Object>[] argsArray = new HashMap[fieldIds.length];

        HashMap<String, Object> args = null;
        int idx = 0;
        for (String fieldId : fieldIds) {
            args = new HashMap<>();
            args.put("id", fieldId);
            args.put("sequence", idx);
            argsArray[idx++] = args;
        }
        String sql = "UPDATE t_field SET sequence=:sequence WHERE id=:id";
        int[] effectedRows = getNamedParameterJdbcTemplate().batchUpdate(sql, argsArray);
        return effectedRows.length;
    }

}
