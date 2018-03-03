package com.smarsh.notificationservice.api.converter;

import com.smarsh.notificationservice.client.model.TemplateType;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateTypeConverter implements Converter<String, TemplateType> {
    @Override
    public TemplateType convert(String source) {
        return TemplateType.forValue(source);
    }

}
