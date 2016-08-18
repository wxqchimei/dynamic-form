/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform.entity;

/**
 * @description：
 * 
 * @author suenlai
 * @date 2016年7月11日
 */
public class TextField extends Field {
    /**
     * 
     */
    private static final long serialVersionUID = 9129633277768057353L;
    private int               maxSize          = 20;
    /**
     * jquery校验规则
     */
    private String            validation;
    /**
     * 单位
     */
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the maxSize
     */
    public int getMaxSize() {
        return this.maxSize;
    }

    /**
     * @param maxSize the maxSize to set
     */
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * @return the validation
     */
    public String getValidation() {
        return this.validation;
    }

    /**
     * @param validation the validation to set
     */
    public void setValidation(String validation) {
        this.validation = validation;
    }


}
