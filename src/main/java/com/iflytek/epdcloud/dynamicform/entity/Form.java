/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform.entity;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
     * @Description:表单添加的模板
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public void displayAddHtml(Writer writer) throws IOException, TemplateException {
        for (Field f : this.fields) {
            f.displayAddHtml(writer);
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
    public void displayConfigHtml(Writer writer) throws IOException, TemplateException {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("form", this);
        FreemarkerRender.render(writer, "form/config.html", root);
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

    /**
     * @Description:根据字段编码来查找相应的字段
     * @param key
     * @return
     */
    public Field findFieldBy(String fieldCode) {
        for (Field f : fields) {
            if (StringUtils.equals(f.getCode(), fieldCode)) {
                return f;
            }
        }
        return null;
    }



}
