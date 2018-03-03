package com.smarsh.notificationservice.client.service.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateUtils {

    private TemplateUtils() {
    }

    public static String resolveTemplateId(String templateId) {
        return StringUtils.isEmpty(templateId) ? "default" : templateId;
    }

}
