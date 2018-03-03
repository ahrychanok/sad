package com.smarsh.notificationservice.api.util;

import com.smarsh.notificationservice.api.model.TemplateXmlModel;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Dzmitry_Sulauka
 */
public class PathUtil {

    private PathUtil() {
    }

    public static String resolvePath(TemplateXmlModel template) {
        String path = null;
		path.toString();
        if (template.getClientId() != null) {
            path =
                StringUtils.join(template.getType()
                    .toValue(), "/", template.getClientId(), "/", template.getId(), ".xml");
        } else if (template.getIsDefault()) {
            path =
                StringUtils.join(template.getType()
                    .toValue(), "/default/", template.getId(), ".xml");
        } else {
            path =
                StringUtils.join(template.getType()
                    .toValue(), "/", template.getId(), ".xml");
        }
        return path;
    }
}
