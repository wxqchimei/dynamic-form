/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.transaction.annotation.Transactional;

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
public class DynamicFormServer {
    /**
     * HTTP提交时的动态字段的前缀 <br>
     * injecting property
     */
    public static String    dynamicFieldHttpParameterPrefix = "custom_";
    // injecting property
    private String          fieldTypeConfigLocation;
    // injecting property
    private String          templateLocation                = null;
    // injecting property
    private DataSource      dataSource                      = null;
    private FieldTypeHolder fieldTypeHolder;
    private DynamicFormDao  dynamicFormDao;

    public void init() {
        fieldTypeHolder = new FieldTypeHolder(fieldTypeConfigLocation);
        dynamicFormDao = new DynamicFormDao(dataSource);
        // 初始化freemarkerRender
        FreemarkerRender.init(templateLocation);
    }

    /**
     * 查找匹配fieldTypeCode的fieldType
     * 
     * @Description:
     * @param fieldTypeCode
     * @return
     */
    public FieldType getFieldTypeByCode(String fieldTypeCode) {
        return fieldTypeHolder.get(fieldTypeCode);
    }

    /**
     * @param fieldTypeConfigLocation the fieldTypeConfigLocation to set
     */
    public void setFieldTypeConfigLocation(String fieldTypeConfigLocation) {
        this.fieldTypeConfigLocation = fieldTypeConfigLocation;
    }


    /**
     * @Description: 列出所有的字段类型
     * @return 所有的字段类型
     */
    public List<FieldType> all() {
        return fieldTypeHolder.getFieldTypes();
    }

    /**
     * 添加表单定义
     * 
     * @Description:
     * @param form
     * @return
     */
    public int create(Form form) {
        // 添加表单表
        return dynamicFormDao.save(form);
    }

    /**
     * 
     * 
     * @Description: 删除表单
     * @param formId
     * @return
     */
    public int delete(String formId) {
        int formEffectRow = dynamicFormDao.deleteForm(formId);
        // 先删除表单表,
        // 再删除表单所属的字段表
        dynamicFormDao.clearField(formId);
        return formEffectRow;
    }

    /**
     * 
     * @Description:查询formId指定的Form定义,内含动态字段列表
     * @param formId
     * @return
     */
    public Form get(String formId) {
        // 先查询表单表,
        // 再查询表单所属的字段表
        Form form = dynamicFormDao.get(formId);
        if (form == null) {
            return null;
        }
        form.setFields(dynamicFormDao.listField(formId));
        return form;
    }

    /**
     * @Description:根据实体名称和实体名称内的编码查找表单
     * @param entityName
     * @param code
     * @return
     */
    public Form getForm(String entityName, String code) {
        // 先查询表单表,

        Form form = dynamicFormDao.getForm(entityName, code);
        if (form == null) {
            return null;
        }
        // 再查询表单所属的字段表
        form.setFields(dynamicFormDao.listField(form.getId()));
        return form;
    }


    /**
     * 
     * @Description:添加新的字段到此表单定义中
     * @param field 待添加的字段
     * @param formId 待操作表单定义的主键ID
     * @return
     */
    public int appendField2Form(Field field, String formId) {
        field.setForm(new Form(formId));
        return dynamicFormDao.addField(field);
    }

    /**
     * 从表单定义中去除此字段
     * 
     * @Description:
     * @param field 待去除的字段
     * @param formId 待操作的表单定义的主键ID
     * @return
     */
    public int removeField(String fieldId) {
        return dynamicFormDao.removeField(fieldId);
    }

    /**
     * 
     * @Description:根据业务模型主键和动态表单获取所有的字段的键值
     * @param identity
     * @param form 须设置category值和指标ID
     * @return 字段键值列表
     */
    public List<FieldValue> get(String entityName, String entityId) {
        return dynamicFormDao.listFieldValue(entityName, entityId);
    }

    /**
     * 
     * @Description: 设置动态表单字段的键值对
     * @param identity 业务模型的ID
     * @param form 须设置category值和指标ID
     * @param customs 字段键值列表
     */
    public void set(String entityId, String entityName, Map<String, String> customs,
            String formId) {

        Form form = get(formId);
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

    /**
     * 
     * @Description:收集httpServletRequest中的符合前缀的参数
     * @param httpRequest 待收集的httpServletRequest
     * @return 参数key-value集合
     */
    public Map<String, String> collectFieldProperties(HttpServletRequest httpRequest) {
        return Servlets.getParameterMap(httpRequest, dynamicFieldHttpParameterPrefix, false);
    }



    /**
     * @param dynamicFieldHttpParameterPrefix the dynamicFieldHttpParameterPrefix to set
     */
    public void setDynamicFieldHttpParameterPrefix(String dynamicFieldHttpParameterPrefix) {
        DynamicFormServer.dynamicFieldHttpParameterPrefix = dynamicFieldHttpParameterPrefix;
    }

    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @param templateLocation the templateLocation to set
     */
    public void setTemplateLocation(String templateLocation) {
        this.templateLocation = templateLocation;
    }

}
