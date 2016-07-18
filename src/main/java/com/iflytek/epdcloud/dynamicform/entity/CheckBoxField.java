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
public class CheckBoxField extends Field {
    /**
     * 
     */
    private static final long serialVersionUID = 857082671538993802L;
    /**
     * json数组字符串
     */
    private String            options;

    /**
     * @return the options
     */
    public String getOptions() {
        return this.options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(String options) {
        this.options = options;
    }



}
