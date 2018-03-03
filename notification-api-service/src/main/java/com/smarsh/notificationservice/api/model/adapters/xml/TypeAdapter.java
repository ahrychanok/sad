package com.smarsh.notificationservice.api.model.adapters.xml;

import com.smarsh.notificationservice.client.model.TemplateType;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Dzmitry_Sulauka
 */
public class TypeAdapter extends XmlAdapter<String, TemplateType> {

    @Override
    public TemplateType unmarshal(String v) throws Exception {
        return TemplateType.forValue(v);
    }

    @Override
    public String marshal(TemplateType v) throws Exception {
        return v.toValue();
    }
}
