/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform.tag;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.iflytek.epdcloud.dynamicform.DynamicFormServer;
import com.iflytek.epdcloud.dynamicform.IDynamicFormServer;
import com.iflytek.epdcloud.dynamicform.entity.Form;

import freemarker.template.TemplateException;

/**
 * @description：显示表单编辑界面的TAG
 * 
 * @author suenlai
 * @date 2016年7月15日
 */
public class FormEditTag extends TagSupport {
    /**
     * 
     */
    private static final long serialVersionUID = 7339629512069413193L;

    private String            formId;
    private boolean           asyn             = false;

    private String            bindingElementId = "targetId";
    private String            containerId      = null;



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
        return EVAL_PAGE;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        String content = "FormEditTag输出异常";

        if (StringUtils.isEmpty(formId)) {
            asyn = true;
        }

        if (asyn) {
            content = getAsynHtml();
        } else {
            content = getStaticHtml();
        }

        try {
            pageContext.getOut().write(content);
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    private String getStaticHtml() {
        WebApplicationContext context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(pageContext.getServletContext());
        IDynamicFormServer dynamicFormServer =
                context.getBean(com.iflytek.epdcloud.dynamicform.IDynamicFormServer.class);
        Form form = dynamicFormServer.getForm(getFormId().trim());
        StringWriter stringWriter = new StringWriter();
        try {
            form.displayEditHtml(stringWriter);
        } catch (IOException | TemplateException e) {
            return e.getMessage();
        }
        return stringWriter.toString();
    }

    private String getAsynHtml() {
        StringBuilder content = new StringBuilder();
        content.append("<script type=\"text/javascript\">");
        content.append(
                "var renderDynamicForm = function(currentFormId,container){if (currentFormId==''){return;};");
        content.append("$.post(\"" + DynamicFormServer.BASE_PATH
                + "back/dynamicForm/show.do\", {formId : ");
        content.append("currentFormId");
        content.append("}, function(framgetHtml) {$(\"");
        content.append(containerId);
        content.append("\").html(framgetHtml);}, \"html\");};");



        content.append("$(\"");
        content.append(bindingElementId);
        content.append(
                "\").change(function(){var currentFormId = $(this).val(); renderDynamicForm(currentFormId,\""
                        + containerId + "\"); ");
        content.append("})");

        content.append("</script>");
        return content.toString();


    }

    /**
     * @return the asyn
     */
    public boolean isAsyn() {
        return this.asyn;
    }

    /**
     * @param asyn the asyn to set
     */
    public void setAsyn(boolean asyn) {
        this.asyn = asyn;
    }

    /**
     * @return the bindingElementId
     */
    public String getBindingElementId() {
        return this.bindingElementId;
    }

    /**
     * @param bindingElementId the bindingElementId to set
     */
    public void setBindingElementId(String bindingElementId) {
        this.bindingElementId = bindingElementId;
    }

    /**
     * @return the formId
     */
    public String getFormId() {
        return this.formId;
    }

    /**
     * @param formId the formId to set
     */
    public void setFormId(String formId) {
        this.formId = formId;
    }

    /**
     * @return the containerId
     */
    public String getContainerId() {
        return this.containerId;
    }

    /**
     * @param containerId the containerId to set
     */
    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }


}
