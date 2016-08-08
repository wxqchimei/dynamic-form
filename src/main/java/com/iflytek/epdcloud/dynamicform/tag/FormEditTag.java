/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform.tag;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.iflytek.epdcloud.dynamicform.DynamicFormServer;
import com.iflytek.epdcloud.dynamicform.entity.FieldValue;

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

    private List<FieldValue>  fieldValues      = null;

    private boolean           asyn             = false;

    private String            containerId      = null;
    private String            entityName       = null;
    private String            entityId         = null;


    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
     */
    @Override
    public int doAfterBody() throws JspException {
        return super.doAfterBody();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {

        String content = "FormEditTag输出异常";
        if (CollectionUtils.isEmpty(fieldValues)) {
            asyn = true;
        }
        if (asyn) {
            if (StringUtils.isEmpty(entityId) || StringUtils.isEmpty(entityId)) {
                content = "若asyn为true的话entityId与entityName为必填项";
            }
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

    private String getStaticHtml() throws JspException {
        StringWriter stringWriter = new StringWriter();
        try {
            for (FieldValue fieldValue : fieldValues) {
                fieldValue.displayEditHtml(stringWriter);
            }
            stringWriter
                    .write("<script type=\"text/javascript\">"
                            +"var renderDynamicForm = function(entityName,entityId,container,callback){if (currentFormId==''){return;};"
                            +"$.post(\"" + DynamicFormServer.BASE_PATH
                                    + "back/dynamicForm/showEditForm.do\", {entityName : "+entityName+",entityId:"+entityId
                            +"}, function(framgetHtml) {$(\""+containerId
                            +"\").html(framgetHtml);if(callback){callback();}}, \"html\");};"


                            +"var collecteDynamicField = function(){"
                            + "var customs = {};" + " $(\"[name^='"
                            + DynamicFormServer.DYNAMICFIELDHTTPPARAMETERPREFIX
                            + "']\").each(function(i,e){" + "   var name = $(this).attr(\"name\");"
                            + " var val = $(this).val();"
                            + "customs[name]=val;});return customs;};</script>");
        } catch (IOException e) {
            throw new JspException("读取显示模板异常", e);
        }
        return stringWriter.toString();
    }

    private String getAsynHtml() {
        StringBuilder content = new StringBuilder();
        content.append("<script type=\"text/javascript\">");
        content.append(
                "var renderDynamicForm = function(entityName,entityId,container,callback){");
        content.append("$.post(\"" + DynamicFormServer.BASE_PATH
                + "back/dynamicForm/showEditForm.do\", {entityName : ");
        content.append(entityName);
        content.append(",entityId:");
        content.append(entityId);
        content.append("}, function(framgetHtml) {$(\"");
        content.append(containerId);
        content.append("\").html(framgetHtml);if(callback){callback();}}, \"html\");};");

        content.append("var collecteDynamicField = function(){" + "var customs = {};"
                + " $(\"[name^='" + DynamicFormServer.DYNAMICFIELDHTTPPARAMETERPREFIX
                + "']\").each(function(i,e){" + "   var name = $(this).attr(\"name\");"
                + " var val = $(this).val();" + "customs[name]=val;});return customs;};");

        content.append("</script>");
        return content.toString();


    }

    /**
     * @return the fieldValues
     */
    public List<FieldValue> getFieldValues() {
        return this.fieldValues;
    }

    /**
     * @param fieldValues the fieldValues to set
     */
    public void setFieldValues(List<FieldValue> fieldValues) {
        this.fieldValues = fieldValues;
    }

    /**
     * @return the asyn
     */
    public boolean isAsyn() {
        return asyn;
    }

    /**
     * @param asyn the asyn to set
     */
    public void setAsyn(boolean asyn) {
        this.asyn = asyn;
    }

    /**
     * @return the containerId
     */
    public String getContainerId() {
        return containerId;
    }

    /**
     * @param containerId the containerId to set
     */
    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
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
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }



}
