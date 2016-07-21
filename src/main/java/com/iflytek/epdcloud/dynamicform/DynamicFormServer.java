/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

import com.iflytek.epdcloud.dynamicform.dao.DynamicFormDao;
import com.iflytek.epdcloud.dynamicform.entity.Field;
import com.iflytek.epdcloud.dynamicform.entity.FieldType;
import com.iflytek.epdcloud.dynamicform.entity.FieldValue;
import com.iflytek.epdcloud.dynamicform.entity.Form;
import com.iflytek.epdcloud.dynamicform.util.Servlets;



/**
 * @description：动态表单对外服务接口类
 * 
 * @author suenlai
 * @date 2016年7月11日
 */
@Transactional
public class DynamicFormServer implements ServletContextAware, IDynamicFormServer {
    /**
     * HTTP提交时的动态字段的前缀 <br>
     * injecting property
     */
    public static String       dynamicFieldHttpParameterPrefix = "custom_";
    public static final String TEMPLATE_FLAG                   = "模板";
    public static final String TEMPLATE_ENTITY_SCOPE_FLAG      = "全局";
    // injecting property
    private String             fieldTypeConfigLocation;
    // injecting property
    private String             templateLocation                = null;
    // injecting property
    private DataSource         dataSource                      = null;
    // injecting property
    private ServletContext     servletContext;
    private FieldTypeHolder    fieldTypeHolder;
    private DynamicFormDao     dynamicFormDao;

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#init()
     */
    @Override
    public void init() {
        fieldTypeHolder = new FieldTypeHolder(fieldTypeConfigLocation);
        dynamicFormDao = new DynamicFormDao(dataSource);
        // 初始化freemarkerRender
        FreemarkerRender.init(servletContext, templateLocation);
    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#getFieldTypeByCode(java.lang.String)
     */
    @Override
    public FieldType getFieldTypeByCode(String fieldTypeCode) {
        FieldType fieldType = fieldTypeHolder.get(fieldTypeCode);
        Assert.notNull(fieldType,
                "找不到" + fieldTypeCode + "对应的字段类型, 请查看fieldType.xml中的code与数据库是否一致");
        return fieldType;

    }

    /*
     * (non-Javadoc)
     * @see
     * com.iflytek.epdcloud.dynamicform.IDynamicFormServer#setFieldTypeConfigLocation(java.lang.
     * String)
     */
    @Override
    public void setFieldTypeConfigLocation(String fieldTypeConfigLocation) {
        this.fieldTypeConfigLocation = fieldTypeConfigLocation;
    }


    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#all()
     */
    @Override
    public List<FieldType> all() {
        return fieldTypeHolder.getFieldTypes();
    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#checkCodeOnly(java.lang.String,
     * java.lang.String)
     */
    @Override
    public boolean checkCodeOnly(String code, String formId) {
        int r = dynamicFormDao.getCodeCount(code, formId);
        return r == 0;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.iflytek.epdcloud.dynamicform.IDynamicFormServer#create(com.iflytek.epdcloud.dynamicform.
     * entity.Form)
     */
    @Override
    public int create(Form form) {
        // 添加表单表
        return dynamicFormDao.saveForm(form);
    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#delete(java.lang.String)
     */
    @Override
    public int delete(String formId) {
        int formEffectRow = dynamicFormDao.deleteForm(formId);
        // 先删除表单表,
        // 再删除表单所属的字段表
        dynamicFormDao.clearField(formId);
        return formEffectRow;
    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#getForm(java.lang.String)
     */
    @Override
    public Form getForm(String formId) {
        // 先查询表单表,
        // 再查询表单所属的字段表
        Form form = dynamicFormDao.getForm(formId);
        if (form == null) {
            return null;
        }
        List<Field> fields = dynamicFormDao.listField(form.getId());
        for (Field f : fields) {
            f.setFieldType(getFieldTypeByCode(f.getFieldType().getCode()));
        }
        form.setFields(fields);
        return form;
    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#getForm(java.lang.String,
     * java.lang.String)
     */
    @Override
    public Form getForm(String entityName, String code) {
        // 先查询表单表,

        Form form = dynamicFormDao.getForm(entityName, code);
        if (form == null) {
            return null;
        }
        // 再查询表单所属的字段表

        List<Field> fields = dynamicFormDao.listField(form.getId());
        for (Field f : fields) {
            f.setFieldType(getFieldTypeByCode(f.getFieldType().getCode()));
        }
        form.setFields(fields);
        return form;
    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#getField(java.lang.String)
     */
    @Override
    public Field getField(String fieldId) {
        Field entity = dynamicFormDao.getField(fieldId);
        String fieldTypeCode = entity.getFieldType().getCode();
        String formId = entity.getForm().getId();
        entity.setFieldType(getFieldTypeByCode(fieldTypeCode));
        entity.setForm(dynamicFormDao.getForm(formId));
        return entity;
    }



    /*
     * (non-Javadoc)
     * @see
     * com.iflytek.epdcloud.dynamicform.IDynamicFormServer#appendField2Form(com.iflytek.epdcloud.
     * dynamicform.entity.Field, java.lang.String)
     */
    @Override
    public int appendField2Form(Field field, String formId) {
        field.setForm(new Form(formId));
        return dynamicFormDao.addField(field);
    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#removeField(java.lang.String)
     */
    @Override
    public int removeField(String fieldId) {
        return dynamicFormDao.removeField(fieldId);
    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#list(java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<FieldValue> list(String entityName, String entityId) {
        return dynamicFormDao.listFieldValue(entityName, entityId);
    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#set(java.lang.String,
     * java.lang.String, java.util.Map, java.lang.String)
     */
    @Override
    public void set(String entityId, String entityName, Map<String, String> customs,
            String formId) {

        Form form = getForm(formId);
        Iterator<String> keysIterator = customs.keySet().iterator();

        List<FieldValue> parameters = new LinkedList<>();
        FieldValue fieldValue = null;
        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            String val = customs.get(key);
            Field matchField = form.findFieldBy(key);
            if (matchField == null) {
                continue;
            }
            String fieldTypeCode = matchField.getFieldType().getCode();

            fieldValue = new FieldValue();
            fieldValue.setEntityName(entityName);
            fieldValue.setEntityId(entityId);
            fieldValue.setKey(key);
            fieldValue.setFieldTypeCode(fieldTypeCode);
            fieldValue.setVal(val);
            parameters.add(fieldValue);
        }
        dynamicFormDao.addFieldValue(parameters);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.iflytek.epdcloud.dynamicform.IDynamicFormServer#collectFieldProperties(javax.servlet.http
     * .HttpServletRequest)
     */
    @Override
    public Map<String, String> collectFieldProperties(HttpServletRequest httpRequest) {
        return Servlets.getParameterMap(httpRequest, dynamicFieldHttpParameterPrefix, false);
    }



    /*
     * (non-Javadoc)
     * @see
     * com.iflytek.epdcloud.dynamicform.IDynamicFormServer#setDynamicFieldHttpParameterPrefix(java.
     * lang.String)
     */
    @Override
    public void setDynamicFieldHttpParameterPrefix(String dynamicFieldHttpParameterPrefix) {
        DynamicFormServer.dynamicFieldHttpParameterPrefix = dynamicFieldHttpParameterPrefix;
    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#setDataSource(javax.sql.DataSource)
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.iflytek.epdcloud.dynamicform.IDynamicFormServer#setTemplateLocation(java.lang.String)
     */
    @Override
    public void setTemplateLocation(String templateLocation) {
        this.templateLocation = templateLocation;
    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#getFieldsByIds(java.util.List)
     */
    @Override
    public List<Field> getFieldsByIds(List<String> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            return dynamicFormDao.getFieldsByIds(ids);
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#appendFields2Form(java.util.List,
     * java.lang.String)
     */
    @Override
    public int appendFields2Form(List<Field> lf, String formId) {
        int res = 0;
        for (Field f : lf) {
            f.setForm(new Form(formId));
        }
        if (CollectionUtils.isNotEmpty(lf)) {
            res = dynamicFormDao.addFields(lf);
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#updateField(com.iflytek.epdcloud.
     * dynamicform.entity.Field)
     */
    @Override
    public int updateField(Field field) {
        int effectRows = dynamicFormDao.updateField(field);
        return effectRows;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.
     * ServletContext)
     */
    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#setServletContext(javax.servlet.
     * ServletContext)
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;

    }

    /*
     * (non-Javadoc)
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#swapSequence(java.lang.String,
     * java.lang.String)
     */
    @Override
    public int swapSequence(String sourceFieldId, String targetFieldId) {
        Field sourceField = dynamicFormDao.getField(sourceFieldId);
        Field targetField = dynamicFormDao.getField(targetFieldId);

        int sourceSequence = sourceField.getSequence();
        int targetSequence = targetField.getSequence();

        if (sourceSequence == targetSequence) {
            sourceField.setSequence(targetSequence + 1);
            return dynamicFormDao.updateField(sourceField);
        } else {
            sourceField.setSequence(targetSequence);
            targetField.setSequence(sourceSequence);
            return dynamicFormDao.updateField(targetField)
                    + dynamicFormDao.updateField(sourceField);
        }

    }

}
