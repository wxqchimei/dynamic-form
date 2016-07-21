/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description：
 * 
 * @author suenlai
 * @date 2016年7月16日
 */
public class DynamicFormServerTest {
    private IDynamicFormServer dynamicFormServer;

    @Before
    public void before() {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("/META-INF/spring-config-demo.xml");
    }

    @After
    public void after() {

    }

    @Test
    public void testAll() {
        dynamicFormServer.all();
    }
}
