/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.iflytek.epdcloud.dynamicform.IDynamicFormServer;
import com.iflytek.epdcloud.dynamicform.entity.FieldType;

/**
 * @description：
 * 
 * @author suenlai
 * @date 2016年7月15日
 */
public class FieldTypeTag extends BodyTagSupport {
    /**
     * 
     */
    private static final long serialVersionUID = 7339629512069413193L;


    private String            code;
    private String            var;

    /*
     * (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
     */
    @Override
    public int doAfterBody() throws JspException {
        return super.doAfterBody();
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        pageContext.removeAttribute(var);
        return EVAL_PAGE;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        WebApplicationContext context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(pageContext.getServletContext());
        IDynamicFormServer dynamicFormServer =
                context.getBean(com.iflytek.epdcloud.dynamicform.IDynamicFormServer.class);
        FieldType fieldType = dynamicFormServer.getFieldTypeByCode(code);
        pageContext.setAttribute(var, fieldType);
        return TagSupport.EVAL_BODY_INCLUDE;
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
        this.code = code.trim();
    }

    /**
     * @return the var
     */
    public String getVar() {
        return this.var;
    }

    /**
     * @param var the var to set
     */
    public void setVar(String var) {
        this.var = var;
    }



}
