package com.iflytek.epdcloud.dynamicform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.iflytek.epdcloud.dynamicform.entity.FieldType;


/***
 * 
 * @author elsu@iflytek.com
 * @date 2016年4月6日 下午6:37:01
 */
class FieldTypeHolder {
    private String          fieldTypeConfigLocation;
    private List<FieldType> fieldTypes = new ArrayList<FieldType>();

    /**
     * @param fieldTypeConfigLocation
     */
    public FieldTypeHolder(String fieldTypeConfigLocation) {
        this.fieldTypeConfigLocation = fieldTypeConfigLocation;
        init();
    }

    public void init() {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader
                    .read(FieldTypeHolder.class.getResourceAsStream(fieldTypeConfigLocation));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        List<Element> items = document.selectNodes("/fieldTypes/fieldType");
        Iterator<Element> rootIterator = items.iterator();
        String name = null;
        String code = null;
        String description = null;
        String fieldClassName = null;
        String configTemplate = null;
        String editTemplate = null;
        String viewTemplate = null;
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
            fieldType = new FieldType(name, code, description, fieldClassName, configTemplate,
                    editTemplate, viewTemplate);
            fieldTypes.add(fieldType);
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
