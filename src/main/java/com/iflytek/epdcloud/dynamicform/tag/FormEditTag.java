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
        if (CollectionUtils.isEmpty(fieldValues)) {
            throw new JspException("fieldValues不能为空");
        }
        StringWriter stringWriter = new StringWriter();
        try {
            for (FieldValue fieldValue : fieldValues) {
                fieldValue.displayEditHtml(stringWriter);
            }
            stringWriter.write("var collecteDynamicField = function(){" + "var customs = {};"
                    + " $(\"[name^='" + DynamicFormServer.DYNAMICFIELDHTTPPARAMETERPREFIX
                    + "']\").each(function(i,e){" + "   var name = $(this).attr(\"name\");"
                    + " var val = $(this).val();" + "customs[name]=val;});return customs;};");
        } catch (IOException e) {
            throw new JspException("读取显示模板异常", e);
        }

        try {
            pageContext.getOut().write(stringWriter.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
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


}
