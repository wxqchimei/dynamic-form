/**
 * Copyright © 2016 科大讯飞股份有限公司. All rights reserved.
 */
package com.iflytek.epdcloud.dynamicform;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletContext;

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
    private static Configuration cfg  = null;
    private static boolean       init = false;

    static void init(ServletContext servletContext, String templateLocation) {
        Assert.isTrue(!init, "FreemarkerRender has been inited");
        // 1.创建配置实例Cofiguration
        cfg = new Configuration();
        cfg.setTemplateUpdateDelay(2);
        ClassTemplateLoader classTemplateLoader =
                new ClassTemplateLoader(FreemarkerRender.class, "/template/");

        WebappTemplateLoader webappTemplateLoader =
                new WebappTemplateLoader(servletContext, "/template/");
        TemplateLoader[] loaders = new TemplateLoader[] {webappTemplateLoader, classTemplateLoader};
        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        cfg.setTemplateLoader(mtl);

        init = true;
    }


    public static String render(String templateName, Object root)
            throws IOException, TemplateException {
        if (!init) {
            throw new RuntimeException("freemarker未初始化");
        }
        StringWriter stringWriter = new StringWriter();
        render(stringWriter, templateName, root);
        return stringWriter.toString();
    }

    public static void render(Writer writer, String templateName, Object root)
            throws IOException, TemplateException {
        if (!init) {
            throw new RuntimeException("freemarker未初始化");
        }
        Template template = cfg.getTemplate(templateName);
        template.process(root, writer);
        writer.flush();
    }
}
