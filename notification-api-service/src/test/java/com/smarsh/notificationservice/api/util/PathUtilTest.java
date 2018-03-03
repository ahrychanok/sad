package com.smarsh.notificationservice.api.util;

import com.smarsh.notificationservice.api.MockTemplateDataUtil;
import com.smarsh.notificationservice.api.model.TemplateXmlModel;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Dzmitry_Sulauka
 */
public class PathUtilTest {

    @Test
    public void testResolveClientPath() throws Exception {
        TemplateXmlModel templateXmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        String resolvedPath = PathUtil.resolvePath(templateXmlModel);
        Assert.assertNotNull(resolvedPath);
        Assert.assertEquals(resolvedPath, "exportNotification/1/templateId.xml");
    }

    @Test
    public void testResolveDefaultPath() throws Exception {
        TemplateXmlModel templateXmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        templateXmlModel.setClientId(null);
        String resolvedPath = PathUtil.resolvePath(templateXmlModel);
        Assert.assertNotNull(resolvedPath);
        Assert.assertEquals(resolvedPath, "exportNotification/default/templateId.xml");
    }

    @Test
    public void testResolveNormalPath() throws Exception {
        TemplateXmlModel templateXmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        templateXmlModel.setClientId(null);
        templateXmlModel.setIsDefault(false);
        String resolvedPath = PathUtil.resolvePath(templateXmlModel);
        Assert.assertNotNull(resolvedPath);
        Assert.assertEquals(resolvedPath, "exportNotification/templateId.xml");
    }
}
