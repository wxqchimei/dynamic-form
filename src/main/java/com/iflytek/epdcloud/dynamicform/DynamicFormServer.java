/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
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
public class DynamicFormServer
        implements ServletContextAware, ApplicationContextAware, IDynamicFormServer {
    /**
     * HTTP提交时的动态字段的前缀 <br>
     * injecting property
     */
    public static String       DYNAMICFIELDHTTPPARAMETERPREFIX = "custom_";
    /**
     * 模板form的code标识
     */
    public static final String TEMPLATE_FLAG                   = "模板";
    /**
     * 全局form的entityName标识
     */
    public static final String TEMPLATE_ENTITY_SCOPE_FLAG      = "全局";

    public static String       BASE_PATH                       =
            "http://localhost:8080/sz-edu-archive-web/";
    // injecting property
    private String             fieldTypeConfigLocation;
    // injecting property
    private String             templateLocation                = null;
    // injecting property
    private DataSource         dataSource                      = null;

    // context
    private ServletContext     servletContext;
    // context
    private ApplicationContext applicationContext;

    // component
    private FieldTypeHolder    fieldTypeHolder;

    // data access object
    private DynamicFormDao     dynamicFormDao;

    /**
     * 
     * @Description:加载字段类型定义文件
     * @param resourceLocation
     * @param defaultResourceLocation
     * @return
     */
    protected List<Resource> loadResource(String resourceLocation, String defaultResourceLocation) {
        List<Resource> resources = new ArrayList<>();

        Resource defaultFieldTypeDefineResource =
                applicationContext.getResource("classpath:fieldTypes.xml");
        resources.add(defaultFieldTypeDefineResource);
        if (StringUtils.isEmpty(fieldTypeConfigLocation)) {
            Resource customsFieldTypeDefineResource =
                    applicationContext.getResource(fieldTypeConfigLocation);
            resources.add(customsFieldTypeDefineResource);
        }
        return resources;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#init()
     */
    @Override
    public void init() {
        // loading the fieldTypeDefineResource
        List<Resource> fieldTypeResources =
                loadResource(fieldTypeConfigLocation, "classpath:fieldTypes.xml");
        fieldTypeHolder = new FieldTypeHolder(fieldTypeResources);

        // construct the dao access object
        dynamicFormDao = new DynamicFormDao(dataSource);
        // 初始化freemarkerRender

        FreemarkerRender.init(servletContext, templateLocation);
    }

    /*
     * (non-Javadoc)
     * 
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
     * 
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
     * 
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#all()
     */
    @Override
    public List<FieldType> all() {
        return fieldTypeHolder.getFieldTypes();
    }

    /*
     * (non-Javadoc)
     * 
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
     * 
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
     * 
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

    /**
     * 获取有效选中的表单fields
     * @param formId
     * @return
     */
    @Override
    public Form getAddForm(String formId) {
        // 先查询表单表,
        // 再查询表单所属的字段表
        Form form = dynamicFormDao.getForm(formId);
        if (form == null) {
            return null;
        }
        List<Field> fields = dynamicFormDao.listCheckedField(form.getId());
        for (Field f : fields) {
            f.setFieldType(getFieldTypeByCode(f.getFieldType().getCode()));
        }
        form.setFields(fields);
        return form;
    }

    /*
     * (non-Javadoc)
     * 
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
     * 
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#getField(java.lang.String)
     */
    @Override
    public Field getField(String fieldId) {
        Field entity = dynamicFormDao.getField(fieldId);
        if(entity!=null){
            String fieldTypeCode = entity.getFieldType().getCode();
            String formId = entity.getForm().getId();
            entity.setFieldType(getFieldTypeByCode(fieldTypeCode));
            entity.setForm(dynamicFormDao.getForm(formId));
        }
        return entity;
    }



    /*
     * (non-Javadoc)
     * 
     * @see
     * com.iflytek.epdcloud.dynamicform.IDynamicFormServer#appendField2Form(com.iflytek.epdcloud.
     * dynamicform.entity.Field, java.lang.String)
     */
    @Override
    public int appendField2Form(Field field, String formId) {
        //首先获取该表单有无该code，有就不允许插入
        int codeCount = dynamicFormDao.getCodeCount(field.getCode(),formId);
        if(codeCount>0){
            return -1;
        }
        int count = dynamicFormDao.countField(formId);
        field.setForm(new Form(formId));
        field.setSequence((byte) (count + 1));
        return dynamicFormDao.addField(field);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#removeField(java.lang.String)
     */
    @Override
    public int removeField(String fieldId) {
        dynamicFormDao.removeFieldValue(fieldId);
        return dynamicFormDao.removeField(fieldId);
    }

    @Override
    public int removeGlobleField(String code){
        return dynamicFormDao.removeGlobleField(code);
    }
    /*
     * (non-Javadoc)
     * 
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#list(java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<FieldValue> getValue(String entityName, String entityId) {
        List<FieldValue> fieldValues = dynamicFormDao.listFieldValue(entityName, entityId);
        List<FieldValue> newLf = new ArrayList<>();
        for (FieldValue fv : fieldValues) {
            String fieldId = fv.getField().getId();
            Field f = getField(fieldId);
            if(f!=null){
                fv.setField(getField(fieldId));
                newLf.add(fv);
            }
        }
        return newLf;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#set(java.lang.String,
     * java.lang.String, java.util.Map, java.lang.String)
     */
    @Override
    public void setValue(String entityId, String entityName, Map<String, String> customs,
            String formId) {

        Form form = getForm(formId);
        Iterator<String> keysIterator = customs.keySet().iterator();

        List<FieldValue> parameters = new LinkedList<>();
        FieldValue fieldValue = null;
        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            String val = customs.get(key);
            boolean hasPrefix = StringUtils.startsWith(key, DYNAMICFIELDHTTPPARAMETERPREFIX);
            if (hasPrefix) {
                key = key.replace(DYNAMICFIELDHTTPPARAMETERPREFIX, "");
            }

            Field matchField = form.findFieldBy(key);

            Assert.notNull(matchField, "找不到与code[" + key + "]匹配的Field");

            fieldValue = new FieldValue();
            fieldValue.setEntityName(entityName);
            fieldValue.setEntityId(entityId);
            fieldValue.setKey(key);
            fieldValue.setField(matchField);
            fieldValue.setVal(val);
            parameters.add(fieldValue);
        }
        // 清除字段值
        dynamicFormDao.cleanFieldValue(entityName, entityId);
        dynamicFormDao.addFieldValue(parameters);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.iflytek.epdcloud.dynamicform.IDynamicFormServer#collectFieldProperties(javax.servlet.http
     * .HttpServletRequest)
     */
    @Override
    public Map<String, String> collectFieldProperties(HttpServletRequest httpRequest) {
        return Servlets.getParameterMap(httpRequest, DYNAMICFIELDHTTPPARAMETERPREFIX, false);
    }



    /*
     * (non-Javadoc)
     * 
     * @see
     * com.iflytek.epdcloud.dynamicform.IDynamicFormServer#setDynamicFieldHttpParameterPrefix(java.
     * lang.String)
     */
    @Override
    public void setDynamicFieldHttpParameterPrefix(String dynamicFieldHttpParameterPrefix) {
        DynamicFormServer.DYNAMICFIELDHTTPPARAMETERPREFIX = dynamicFieldHttpParameterPrefix;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#setDataSource(javax.sql.DataSource)
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.iflytek.epdcloud.dynamicform.IDynamicFormServer#setTemplateLocation(java.lang.String)
     */
    @Override
    public void setTemplateLocation(String templateLocation) {
        this.templateLocation = templateLocation;
    }

    /*
     * (non-Javadoc)
     * 
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
     * 
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
     * 更新全局模板字段
     * 
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#updateField(com.iflytek.epdcloud.
     * dynamicform.entity.Field)
     */
    @Override
    public int updateField(Field field) {
        int effectRows = dynamicFormDao.updateField(field);
        dynamicFormDao.updateCopiedField(field);
        return effectRows;
    }

    /*
    * 更新模块模板字段
    *
    * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#updateField(com.iflytek.epdcloud.
    * dynamicform.entity.Field)
    */
    @Override
    public int modifyField(Field field) {
        int effectRows = dynamicFormDao.updateField(field);
        return effectRows;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#setServletContext(javax.servlet.
     * ServletContext)
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.iflytek.epdcloud.dynamicform.IDynamicFormServer#swapSequence(java.lang.String,
     * java.lang.String)
     */
    @Override
    public int sortFields(String[] fieldIds) {
        return dynamicFormDao.sortField(fieldIds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework
     * .context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @param bASE_PATH the bASE_PATH to set
     */
    public static void setBasePath(String basePath) {
        if (!basePath.endsWith("/")) {
            basePath = basePath + "/";
        }
        BASE_PATH = basePath;
    }

    @Override
    public int getFieldCountByCode(String code) {
        return dynamicFormDao.getFieldCountByCode(code);
    }

    /**
     * 保存模块字段
     *
     * @param formId
     * @param ids
     * @return
     */
    @Override
    public int addFieldBatch(String formId, List<String> ids){
        int res = 1;
        if(StringUtils.isNotBlank(formId)){
            dynamicFormDao.clearField(formId);
            if(CollectionUtils.isNotEmpty(ids)){
                res = dynamicFormDao.addFieldBatch(ids);
            }
        }
        return res;
    }
}
