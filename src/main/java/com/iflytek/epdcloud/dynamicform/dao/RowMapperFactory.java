/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.iflytek.epdcloud.dynamicform.entity.*;
import org.springframework.jdbc.core.RowMapper;

/**
 * @description：
 * 
 * @author suenlai
 * @date 2016年7月13日
 */
public class RowMapperFactory {
    static RowMapper<Form>       FormRowMapper       = new RowMapper<Form>() {
                                                         @Override
                                                         public Form mapRow(ResultSet rs,
                                                                 int rowNum) throws SQLException {
                                                             Form f = new Form(
                                                                     rs.getString("entityName"),
                                                                     rs.getString("code"));
                                                             f.setId(rs.getString("id"));
                                                             return f;
                                                         }

                                                     };
    static RowMapper<Field>      fieldRowMapper      = new RowMapper<Field>() {

                                                         @Override
                                                         public Field mapRow(ResultSet rs,
                                                                 int rowNum) throws SQLException {
                                                             Field entity = null;

                                                             String fieldTypeCode =
                                                                     rs.getString("fieldTypeCode");
                                                             switch (fieldTypeCode) {
                                                                 case "datetime":
                                                                     DateTimeField dateTimeField =
                                                                             new DateTimeField();
                                                                     dateTimeField.setDateFormat(
                                                                             rs.getString(
                                                                                     "dateFormat"));
                                                                     entity = dateTimeField;
                                                                     break;
                                                                 case "timeInterval":
                                                                     TimeIntervalField tif =
                                                                             new TimeIntervalField();
                                                                     tif.setDateFormat(
                                                                             rs.getString(
                                                                                     "dateFormat"));
                                                                     entity = tif;
                                                                     break;
                                                                 case "text":
                                                                     TextField textField =
                                                                             new TextField();
                                                                     textField.setUnit(rs.getString("unit"));
                                                                     textField.setMaxSize(
                                                                             rs.getInt("maxSize"));
                                                                     textField.setValidation(
                                                                             rs.getString(
                                                                                     "validation"));
                                                                     entity = textField;
                                                                     break;
                                                                 case "textarea":
                                                                     TextAreaField textAreaField =
                                                                             new TextAreaField();
                                                                     textAreaField.setHeight(
                                                                             rs.getShort("height"));
                                                                     textAreaField.setWidth(
                                                                             rs.getShort("width"));
                                                                     textAreaField.setMaxSize(
                                                                             rs.getInt("maxSize"));
                                                                     textAreaField.setValidation(
                                                                             rs.getString(
                                                                                     "validation"));
                                                                     entity = textAreaField;
                                                                     break;
                                                                 case "checkbox":
                                                                     CheckBoxField checkBoxField =
                                                                             new CheckBoxField();
                                                                     String options = rs
                                                                             .getString("options");

                                                                     checkBoxField
                                                                             .setOptions(options);
                                                                     entity = checkBoxField;
                                                                     break;
                                                                 case "radiobox":
                                                                     RadioBoxField radioBoxField =
                                                                             new RadioBoxField();
                                                                     String ops = rs
                                                                             .getString("options");

                                                                     radioBoxField.setOptions(ops);
                                                                     entity = radioBoxField;
                                                                     break;
                                                                 case "select":
                                                                     SelectField sf =
                                                                             new SelectField();
                                                                     String sop = rs
                                                                             .getString("options");
                                                                     sf.setOptions(sop);
                                                                     entity = sf;
                                                                     break;
                                                                 case "attachment":
                                                                     AttachmentField af =
                                                                             new AttachmentField();
                                                                     int fileNum = rs
                                                                             .getInt("fileNum");
                                                                     af.setFileNum(fileNum);
                                                                     entity = af;
                                                                     break;
                                                                 case "studentSelector":
                                                                     StudentSelectorField ssf =
                                                                             new StudentSelectorField();
                                                                     entity = ssf;
                                                                     break;
                                                                 case "institutionSelector":
                                                 					InstitutionSelectorField isf = 
                                                 							new InstitutionSelectorField();
                                                 					entity = isf;
                                                 					break;
                                                                 case "attachmentTextarea":
                                                                     AttachmentTextAreaField atf = 
                                                                             new AttachmentTextAreaField();
                                                                     atf.setFileNum(rs
                                                                             .getInt("fileNum"));
                                                                     atf.setHeight(
                                                                             rs.getShort("height"));
                                                                     atf.setWidth(
                                                                             rs.getShort("width"));
                                                                     atf.setMaxSize(
                                                                             rs.getInt("maxSize"));
                                                                     atf.setValidation(
                                                                             rs.getString(
                                                                                     "validation"));
                                                                     entity = atf;
                                                                     break;
                                                                 default:
                                                                     throw new RuntimeException(
                                                                             "未知或未加载的字段类型");
                                                             }
                                                             entity.setId(rs.getString("id"));
                                                             entity.setLabel(rs.getString("label"));
                                                             entity.setCode(rs.getString("code"));
                                                             entity.setColumns(
                                                                     rs.getByte("columns"));
                                                             entity.setDefaultValue(
                                                                     rs.getString("defaultValue"));
                                                             entity.setRequired(
                                                                     rs.getBoolean("required"));
                                                             entity.setSequence(
                                                                     rs.getByte("sequence"));
                                                             entity.setFieldType(
                                                                     new FieldType(fieldTypeCode));
                                                             entity.setForm(new Form(
                                                                     rs.getString("formId")));
                                                             return entity;
                                                         }

                                                     };

    static RowMapper<FieldValue> fieldValueRowMapper = new RowMapper<FieldValue>() {

                                                         @Override
                                                         public FieldValue mapRow(ResultSet rs,
                                                                 int rowNum) throws SQLException {
                                                             FieldValue entity = new FieldValue();
                                                             entity.setId(rs.getString("id"));
                                                             entity.setEntityName(
                                                                     rs.getString("entityName"));
                                                             entity.setEntityId(
                                                                     rs.getString("entityId"));
                                                             entity.setField(new Field(
                                                                     rs.getString("fieldId")));
                                                             entity.setKey(rs.getString("key"));
                                                             entity.setVal(rs.getString("val"));
                                                             return entity;
                                                         }

                                                     };

}
