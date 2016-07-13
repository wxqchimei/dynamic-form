/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.iflytek.epdcloud.dynamicform.entity.DateTimeField;
import com.iflytek.epdcloud.dynamicform.entity.Field;
import com.iflytek.epdcloud.dynamicform.entity.Form;
import com.iflytek.epdcloud.dynamicform.entity.TextField;

/**
 * @description：
 * 
 * @author suenlai
 * @date 2016年7月13日
 */
public class RowMapperFactory {
    static RowMapper<Form>          FormRowMapper          = new RowMapper<Form>() {
                                                               @Override
                                                               public Form mapRow(ResultSet rs,
                                                                       int rowNum)
                                                                       throws SQLException {
                                                                   Form entity = new Form();
                                                                   return entity;
                                                               }

                                                           };
    static RowMapper<Field>         fieldRowMapper         = new RowMapper<Field>() {
                                                               @Override
                                                               public Field mapRow(ResultSet rs,
                                                                       int rowNum)
                                                                       throws SQLException {
                                                                   return null;
                                                               }

                                                           };
    static RowMapper<TextField>     textFieldRowMapper     = new RowMapper<TextField>() {
                                                               @Override
                                                               public TextField mapRow(ResultSet rs,
                                                                       int rowNum)
                                                                       throws SQLException {
                                                                   return null;

                                                               }

                                                           };
    static RowMapper<DateTimeField> dateTimeFieldRowMapper = new RowMapper<DateTimeField>() {
                                                               @Override
                                                               public DateTimeField mapRow(
                                                                       ResultSet rs, int rowNum)
                                                                       throws SQLException {
                                                                   return null;

                                                               }

                                                           };

}
