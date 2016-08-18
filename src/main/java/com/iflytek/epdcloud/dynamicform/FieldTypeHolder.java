package com.iflytek.epdcloud.dynamicform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.iflytek.epdcloud.dynamicform.entity.FieldType;


/***
 * 
 * @author elsu@iflytek.com
 * @date 2016年4月6日 下午6:37:01
 */
class FieldTypeHolder {
    private static final Logger LOGGER     = LoggerFactory.getLogger(FieldTypeHolder.class);
    private List<Resource>      fieldTypeDefineResources;
    private List<FieldType>     fieldTypes = new ArrayList<FieldType>();

    /**
     *
     * @param fieldTypeDefineResources
     */
    public FieldTypeHolder(List<Resource> fieldTypeDefineResources) {
        this.fieldTypeDefineResources = fieldTypeDefineResources;
        init();
    }

    public void init() {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        for (Resource resource : fieldTypeDefineResources) {
            if (!resource.exists()) {
                continue;
            }
            try {
                document = saxReader.read(resource.getInputStream());
            } catch (DocumentException | IOException e) {
                LOGGER.warn("加载字段类型定义资源" + resource.getFilename() + "失败", e);
            }
            @SuppressWarnings("unchecked")
            List<Element> items = document.selectNodes("/fieldTypes/fieldType");
            Iterator<Element> rootIterator = items.iterator();
            String name = null;
            String code = null;
            String description = null;
            String fieldClassName = null;
            String configTemplate = null;
            String editTemplate = null;
            String viewTemplate = null;
            String addTemplate = null;
            FieldType fieldType = null;
            while (rootIterator.hasNext()) {
                Element element = rootIterator.next();
                name = element.element("name").getText();
                code = element.element("code").getText();
                description = element.element("description").getText();
                fieldClassName = element.element("fieldClassName").getText();
                configTemplate = element.element("configTemplate").getText();
                editTemplate = element.element("editTemplate").getText();
                viewTemplate = element.element("viewTemplate").getText();
                addTemplate = element.element("addTemplate").getText();

                fieldType = new FieldType(name, code, description, fieldClassName, configTemplate,
                        editTemplate, viewTemplate, addTemplate);
                fieldTypes.add(fieldType);
            }
        }
    }

    /**
     * 
     * @Description:根据编码获得字段类型
     * @param code
     * @return
     */
    public FieldType get(String code) {
        for (FieldType ft : fieldTypes) {
            if (StringUtils.equals(ft.getCode(), code)) {
                return ft;
            }
        }
        return null;
    }


    public List<FieldType> getFieldTypes() {
        return fieldTypes;
    }
}
