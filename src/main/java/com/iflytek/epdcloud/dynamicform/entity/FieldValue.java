/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform.entity;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.iflytek.epdcloud.dynamicform.DynamicFormServer;
import com.iflytek.epdcloud.dynamicform.FreemarkerRender;

import freemarker.template.TemplateException;

/**
 * @description：字段数据实体
 * @author suenlai
 * @date 2016年7月15日
 */
public class FieldValue extends Entity {
    /**
     * 
     */
    private static final long serialVersionUID = -1041534743663885589L;
    private String            entityName;
    private String            entityId;
    private String            key;
    private String            val;
    private Field             field;


    /**
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }



    /**
     * @return the val
     */
    public String getVal() {
        if (StringUtils.isEmpty(this.val)) {
            return getField().getDefaultValue();
        }
        return this.val;
    }

    /**
     * @param val the val to set
     */
    public void setVal(String val) {
        this.val = val;
    }

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return this.entityName;
    }

    /**
     * @param entityName the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * @return the entityId
     */
    public String getEntityId() {
        return this.entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }



    /**
     * @return the field
     */
    public Field getField() {
        return this.field;
    }

    /**
     * @param field the field to set
     */
    public void setField(Field field) {
        this.field = field;
    }

    /**
     * @Description:显示字段值模板(不能编辑)
     * @param printWriter
     * @throws IOException
     * @throws TemplateException
     */
    public void displayViewHtml(Writer writer) throws IOException {
        writer.write(getViewHtmlPlain());
    }

    @JSONField(serialize = false, deserialize = false)
    public String getViewHtmlPlain() {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("field", getField());
        root.put("fieldValue", this);
        root.put("field_name_prefix", DynamicFormServer.DYNAMICFIELDHTTPPARAMETERPREFIX);
        root.put("basePath", DynamicFormServer.BASE_PATH);

        String templateName = this.getField().getFieldType().getViewTemplate();
        return FreemarkerRender.render(templateName, root);
    }

    /**
     * @Description:显示字段值模板,可编辑
     * @param printWriter
     * @throws IOException
     * @throws TemplateException
     */
    public void displayEditHtml(Writer writer) throws IOException {
        writer.write(getEditHtmlPlain());
    }

    @JSONField(serialize = false, deserialize = false)
    public String getEditHtmlPlain() {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("field", getField());
        root.put("fieldValue", this);
        root.put("field_name_prefix", DynamicFormServer.DYNAMICFIELDHTTPPARAMETERPREFIX);
        root.put("basePath", DynamicFormServer.BASE_PATH);

        String templateName = this.getField().getFieldType().getEditTemplate();
        return FreemarkerRender.render(templateName, root);
    }

}
