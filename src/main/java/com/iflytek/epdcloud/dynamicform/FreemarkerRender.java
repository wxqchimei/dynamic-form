/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @description：
 * 
 * @author suenlai
 * @date 2016年7月11日
 */
public class FreemarkerRender {
    private static final Logger  LOGGER = LoggerFactory.getLogger(FreemarkerRender.class);
    private static Configuration cfg    = null;
    private static boolean       init   = false;

    static void init(ServletContext servletContext, String templateLocation) {
        Assert.isTrue(!init, "FreemarkerRender has been inited");
        // 1.创建配置实例Cofiguration
        cfg = new Configuration();
        cfg.setTemplateUpdateDelay(2);
        ClassTemplateLoader classTemplateLoader =
                new ClassTemplateLoader(FreemarkerRender.class, "/template/");

        WebappTemplateLoader webappTemplateLoader =
                new WebappTemplateLoader(servletContext, templateLocation);
        TemplateLoader[] loaders = new TemplateLoader[] {webappTemplateLoader, classTemplateLoader};
        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        cfg.setTemplateLoader(mtl);
        cfg.setDefaultEncoding("utf-8");

        init = true;
    }


    public static String render(String templateName, Object root) {
        if (!init) {
            throw new RuntimeException("freemarker未初始化");
        }
        StringWriter stringWriter = new StringWriter();
        render(stringWriter, templateName, root);
        return stringWriter.toString();
    }

    public static void render(Writer writer, String templateName, Object root) {
        if (!init) {
            throw new RuntimeException("freemarker未初始化");
        }
        Template template;
        try {
            template = cfg.getTemplate(templateName);
            template.process(root, writer);
            writer.flush();
        } catch (FileNotFoundException e) {
            LOGGER.error("找不到对应[" + templateName + "]模板", e);
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            LOGGER.error("模板[" + templateName + "]解析错误", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            LOGGER.error("模板[" + templateName + "]IO操作错误", e);
            throw new RuntimeException(e);
        }


    }
}
