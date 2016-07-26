/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.iflytek.epdcloud.dynamicform.entity.Field;
import com.iflytek.epdcloud.dynamicform.entity.FieldType;
import com.iflytek.epdcloud.dynamicform.entity.FieldValue;
import com.iflytek.epdcloud.dynamicform.entity.Form;


/**
 * @description：
 * 
 * @author suenlai
 * @date 2016年7月21日
 */
public interface IDynamicFormServer {

    void init();

    /**
     * 查找匹配fieldTypeCode的fieldType
     * 
     * @Description:
     * @param fieldTypeCode
     * @return
     */
    FieldType getFieldTypeByCode(String fieldTypeCode);

    /**
     * @param fieldTypeConfigLocation the fieldTypeConfigLocation to set
     */
    void setFieldTypeConfigLocation(String fieldTypeConfigLocation);

    /**
     * @Description: 列出所有的字段类型
     * @return 所有的字段类型
     */
    List<FieldType> all();

    /**
     * 验证code是否唯一，true唯一，false不唯一
     * 
     * @Description:
     * @param code
     * @return
     */
    boolean checkCodeOnly(String code, String formId);

    /**
     * 添加表单定义
     * 
     * @Description:
     * @param form
     * @return
     */
    int create(Form form);

    /**
     * 
     * 
     * @Description: 删除表单
     * @param formId
     * @return
     */
    int delete(String formId);

    /**
     * 
     * @Description:查询formId指定的Form定义,内含动态字段列表
     * @param formId
     * @return
     */
    Form getForm(String formId);

    /**
     * @Description:根据实体名称和实体名称内的编码查找表单
     * @param entityName
     * @param code
     * @return
     */
    Form getForm(String entityName, String code);

    /**
     * @Description:
     * @param fieldId
     * @return
     */
    Field getField(String fieldId);

    /**
     * 
     * @Description:添加新的字段到此表单定义中
     * @param field 待添加的字段
     * @param formId 待操作表单定义的主键ID
     * @return
     */
    int appendField2Form(Field field, String formId);

    /**
     * 从表单定义中去除此字段
     * 
     * @Description:
     * @param field 待去除的字段
     * @param formId 待操作的表单定义的主键ID
     * @return
     */
    int removeField(String fieldId);

    /**
     * 
     * @Description:根据业务模型主键和动态表单获取所有的字段的键值
     * @param identity
     * @param form 须设置category值和指标ID
     * @return 字段键值列表
     */
    List<FieldValue> getValue(String entityName, String entityId);

    /**
     * 
     * @Description: 设置动态表单字段的键值对
     * @param identity 业务模型的ID
     * @param form 须设置category值和指标ID
     * @param customs 字段键值列表
     */
    void setValue(String entityId, String entityName, Map<String, String> customs, String formId);

    /**
     * 
     * @Description:收集httpServletRequest中的符合前缀的参数
     * @param httpRequest 待收集的httpServletRequest
     * @return 参数key-value集合
     */
    Map<String, String> collectFieldProperties(HttpServletRequest httpRequest);

    /**
     * @param dynamicFieldHttpParameterPrefix the dynamicFieldHttpParameterPrefix to set
     */
    void setDynamicFieldHttpParameterPrefix(String dynamicFieldHttpParameterPrefix);

    /**
     * @param dataSource the dataSource to set
     */
    void setDataSource(DataSource dataSource);

    /**
     * @param templateLocation the templateLocation to set
     */
    void setTemplateLocation(String templateLocation);

    List<Field> getFieldsByIds(List<String> ids);

    int appendFields2Form(List<Field> lf, String formId);

    /**
     * @Description:
     * @param field
     * @return
     */
    int updateField(Field field);

    /**
     * @Description:
     * @param fieldIds
     * @return
     */
    int sortFields(String[] fieldIds);

    /**
     * 删除表单对应的fields
     * 
     * @Description:
     * @param id
     * @return
     */
    int deleteFieldsByFormId(String id);

    
    int removeFieldsInForm(Field lf, String formId);

}
