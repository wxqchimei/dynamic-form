/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.iflytek.epdcloud.dynamicform.entity.Form;

/**
 * @description：
 * 
 * @author suenlai
 * @date 2016年7月11日
 */
public class DynamicFormDao extends JdbcDaoSupport {


    @SuppressWarnings("unused")
    private DynamicFormDao() {};

    public DynamicFormDao(DataSource dataSource) {

    }

    /**
     * @Description:
     * @param formId
     * @return
     */
    public Form get(String formId) {
        String sql =
                "SELECT t_form.id,t_form.category,t_form.indicator_id FROM t_form where id=:formId";
        Form entity = getJdbcTemplate().queryForObject(sql, new Object[] {formId},
                RowMapperFactory.FormRowMapper);
        return entity;
    }

    /**
     * @Description:
     * @param formId
     * @return
     */
    public int delete(String formId) {
        String sql = "DELETE FROM t_form where id=:formId";
        int effectedRows = getJdbcTemplate().update(sql, new Object[] {formId});
        return effectedRows;
    }



}
