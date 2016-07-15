/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform.entity;

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
    private String            fieldTypeCode;


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
     * @return the fieldTypeCode
     */
    public String getFieldTypeCode() {
        return this.fieldTypeCode;
    }

    /**
     * @param fieldTypeCode the fieldTypeCode to set
     */
    public void setFieldTypeCode(String fieldTypeCode) {
        this.fieldTypeCode = fieldTypeCode;
    }

}
