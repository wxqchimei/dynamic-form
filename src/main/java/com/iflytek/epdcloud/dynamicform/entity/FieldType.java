/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform.entity;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.iflytek.epdcloud.dynamicform.FreemarkerRender;

import freemarker.template.TemplateException;

/**
 * @description：字段元信息类型
 * 
 * @author suenlai
 * @date 2016年7月11日
 */
public class FieldType extends Entity {

    /**
     * 
     */
    private static final long serialVersionUID = -2849371327865416883L;

    private String            name;
    private String            code;
    private String            description;
    private String            fieldClassName;
    private String            configTemplate;
    private String            editTemplate;
    private String            viewTemplate;

    /**
     * @param name
     * @param description
     * @param className
     */
    public FieldType(String name, String code, String description, String fieldClassName,
            String configTemplate, String editTemplate, String viewTemplate) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.fieldClassName = fieldClassName;
        this.configTemplate = configTemplate;
        this.editTemplate = editTemplate;
        this.viewTemplate = viewTemplate;
    }

    public FieldType(String code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the fieldClassName
     */
    public String getFieldClassName() {
        return this.fieldClassName;
    }

    /**
     * @param fieldClassName the fieldClassName to set
     */
    public void setFieldClassName(String fieldClassName) {
        this.fieldClassName = fieldClassName;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * @return the configTemplate
     */
    public String getConfigTemplate() {
        return this.configTemplate;
    }

    /**
     * @param configTemplate the configTemplate to set
     */
    public void setConfigTemplate(String configTemplate) {
        this.configTemplate = configTemplate;
    }

    /**
     * @return the editTemplate
     */
    public String getEditTemplate() {
        return this.editTemplate;
    }

    /**
     * @param editTemplate the editTemplate to set
     */
    public void setEditTemplate(String editTemplate) {
        this.editTemplate = editTemplate;
    }

    /**
     * @return the viewTemplate
     */
    public String getViewTemplate() {
        return this.viewTemplate;
    }

    /**
     * @param viewTemplate the viewTemplate to set
     */
    public void setViewTemplate(String viewTemplate) {
        this.viewTemplate = viewTemplate;
    }


    /**
     * 
     * @Description:显示配置字段表单
     * @param printWriter
     * @throws TemplateException
     * @throws IOException
     */
    public void displayConfigHtml(PrintWriter printWriter) throws IOException, TemplateException {
        printWriter.print(configHtmlPlain());
    }

    public String configHtmlPlain() throws IOException, TemplateException {
        Map<String, Object> root = new HashMap<String, Object>();
        String templateName = this.getConfigTemplate();
        return FreemarkerRender.render(templateName, root);
    }
}
