package com.iflytek.epdcloud.dynamicform.entity;

public class AttachmentTextAreaField extends TextAreaField {

    /**
     * 
     */
    private static final long serialVersionUID = 6393219206395906726L;
    private int     fileNum = 6;
    public int getFileNum() {
        return fileNum;
    }
    public void setFileNum(int fileNum) {
        this.fileNum = fileNum;
    }
}
