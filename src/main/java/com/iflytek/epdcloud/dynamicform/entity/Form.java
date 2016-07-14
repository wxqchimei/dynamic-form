/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform.entity;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.iflytek.epdcloud.dynamicform.FreemarkerRender;

import freemarker.template.TemplateException;

/**
 * @description：
 * 
 * @author suenlai
 * @date 2016年7月11日
 */
public class Form extends Entity {
    /**
     * 
     */
    private static final long serialVersionUID = 4307770216706319520L;
    /**
     * 分类,活动还是动态
     */
    private String            entityName;
    /**
     * 在category中唯一的编号, 在此指所关联的指标ID
     */
    private String            code;
    /**
     * 字段列表
     */
    private List<Field>       fields           = new LinkedList<Field>();

    public Form(String id) {
        super(id);
    }

    public Form(String entityName, String code) {
        super();
        this.entityName = entityName;
        this.code = code;
    }

    /**
     * 
     * 
     * @Description:预览表单的模板
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public void displayViewHtml(PrintWriter printWriter) throws IOException, TemplateException {
        for (Field f : this.fields) {
            f.displayViewHtml(printWriter);
        }
    }

    /**
     * 
     * 
     * @Description:编辑表单的模板
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public void displayEditHtml(PrintWriter printWriter) throws IOException, TemplateException {
        for (Field f : this.fields) {
            f.displayEditHtml(printWriter);
        }
    }

    /**
     * 
     * 
     * @Description:配置表单的模板
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public void displayConfigHtml(PrintWriter printWriter) throws IOException, TemplateException {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("form", this);
        FreemarkerRender.render(printWriter, "form/config.html", root);
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
     * @return the fields
     */
    public List<Field> getFields() {
        return this.fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }



}
