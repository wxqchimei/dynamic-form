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
public class AttachmentField extends Field {
    /**
     * 
     */
    private static final long serialVersionUID = 857082671538993802L;
    private int     fileNum = 6;
    public int getFileNum() {
        return fileNum;
    }
    public void setFileNum(int fileNum) {
        this.fileNum = fileNum;
    }
    

}
