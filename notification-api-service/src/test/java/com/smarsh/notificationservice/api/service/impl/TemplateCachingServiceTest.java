package com.smarsh.notificationservice.api.service.impl;

import com.smarsh.notificationservice.api.MockTemplateDataUtil;
import com.smarsh.notificationservice.api.exception.TemplateMissingException;
import com.smarsh.notificationservice.api.model.Key;
import com.smarsh.notificationservice.api.model.TemplateXmlModel;
import com.smarsh.notificationservice.api.service.filetree.TemplatesMapBuilderService;
import com.smarsh.notificationservice.client.model.TemplateType;
import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import java.util.Collection;
import java.util.HashMap;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateCachingServiceTest {

    public static final String DEFAULT_ID = "default";
    TemplateCachingServiceImpl service;

    @Mock
    TemplatesMapBuilderService templatesMapBuilderService;
    HashMap<String, TemplateXmlModel> hashMap;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        hashMap = new HashMap<>();
        service = new TemplateCachingServiceImpl(templatesMapBuilderService);
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetFromCache() throws Exception {

        TemplateXmlModel xmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel.setId("1");
        xmlModel.setIsDefault(false);

        TemplateXmlModel xmlModel1 = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel1.setId("2");
        xmlModel1.setClientId(null);

        TemplateXmlModel xmlModel2 = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel2.setId("3");
        xmlModel2.setClientId(null);
        xmlModel2.setIsDefault(false);

        hashMap.put("1", xmlModel);
        hashMap.put("2", xmlModel1);
        hashMap.put("3", xmlModel2);
        when(templatesMapBuilderService.getAllTemplatesMap()).thenReturn(hashMap);
        service.fillCache();

        Key key = new Key();
        key.setGuid("1");
        key.setType(TemplateType.EXPORT_NOTIFICATION);
        TemplateXmlModel fromCache = service.getFromCache(key);
        Assert.assertNotNull(fromCache);
        ReflectionAssert.assertReflectionEquals(xmlModel, fromCache);

        Key keyWithoutGuid = new Key();
        keyWithoutGuid.setType(TemplateType.EXPORT_NOTIFICATION);
        keyWithoutGuid.setGuid("2");
        TemplateXmlModel fromCache1 = service.getFromCache(keyWithoutGuid);
        Assert.assertNotNull(fromCache1);
        ReflectionAssert.assertReflectionEquals(xmlModel1, fromCache1);

        Key getDefault = new Key();
        getDefault.setClientId(777);
        getDefault.setType(TemplateType.EXPORT_NOTIFICATION);
        getDefault.setGuid(DEFAULT_ID);
        TemplateXmlModel fromCache2 = service.getFromCache(getDefault);
        Assert.assertNotNull(fromCache2);
        ReflectionAssert.assertReflectionEquals(xmlModel1, fromCache2);

    }

    @Test(expectedExceptions = TemplateMissingException.class)
    public void testGetFromCacheNegative() throws Exception {
        Key getDefault = new Key();
        getDefault.setClientId(777);
        getDefault.setType(TemplateType.EXPORT_NOTIFICATION);
        TemplateXmlModel fromCache = service.getFromCache(getDefault);
        Assert.assertNull(fromCache);
        getDefault.setGuid("default");
        service.getFromCache(getDefault);
    }

    @Test
    public void testGetAllByKey() throws Exception {

        TemplateXmlModel xmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel.setId("1");
        xmlModel.setIsDefault(false);
        xmlModel.setClientId(null);

        TemplateXmlModel xmlModel1 = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel1.setId("2");
        xmlModel1.setClientId(null);

        TemplateXmlModel xmlModel2 = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel2.setId("3");
        xmlModel2.setIsDefault(false);
        hashMap.put("1", xmlModel);
        hashMap.put("2", xmlModel1);
        hashMap.put("3", xmlModel2);

        when(templatesMapBuilderService.getAllTemplatesMap()).thenReturn(hashMap);
        service.fillCache();

        Collection<TemplateXmlModel> allByKey = service.getAllByKey(new Key(xmlModel.getType()));
        Assert.assertNotNull(allByKey);
        Assert.assertFalse(allByKey.isEmpty());
        Assert.assertEquals(allByKey.size(), 2);

        Collection<TemplateXmlModel> allByKey1 = service.getAllByKey(new Key(xmlModel.getType(), 1, false));
        Assert.assertNotNull(allByKey1);
        Assert.assertFalse(allByKey1.isEmpty());
        Assert.assertEquals(allByKey1.size(), 1);

    }

    @Test
    public void testRemoveFromCache() throws Exception {
        TemplateXmlModel xmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel.setId("1");
        xmlModel.setIsDefault(false);
        xmlModel.setClientId(null);

        TemplateXmlModel xmlModel1 = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel1.setId("2");
        xmlModel1.setClientId(null);

        TemplateXmlModel xmlModel2 = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel2.setId("3");
        xmlModel2.setIsDefault(false);

        TemplateXmlModel xmlModel3 = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel3.setId("4");
        xmlModel3.setIsDefault(false);

        hashMap.put("1", xmlModel);
        hashMap.put("2", xmlModel1);
        hashMap.put("3", xmlModel2);
        hashMap.put("4", xmlModel3);
        when(templatesMapBuilderService.getAllTemplatesMap()).thenReturn(hashMap);
        service.fillCache();

        Key key = new Key(TemplateType.EXPORT_NOTIFICATION, xmlModel.getId());
        service.removeFromCache(key);
        TemplateXmlModel fromCache = service.getFromCache(key);
        Assert.assertNull(fromCache);
        Collection<TemplateXmlModel> allByKey = service.getAllByKey(new Key(TemplateType.EXPORT_NOTIFICATION,1));
        Assert.assertNotNull(allByKey);
        Assert.assertEquals(allByKey.size(), 2);
    }

    @Test
    public void testUpdateCache() throws Exception {

        TemplateXmlModel xmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel.setId("1");
        xmlModel.setIsDefault(false);
        xmlModel.setClientId(null);
        hashMap.put("1", xmlModel);
        when(templatesMapBuilderService.getAllTemplatesMap()).thenReturn(hashMap);
        service.fillCache();
        Key key = new Key(xmlModel.getType(), xmlModel.getClientId(), xmlModel.getIsDefault());
        TemplateXmlModel model = MockTemplateDataUtil.mockTemplateXmlModel();
        model.setId("1");
        model.setIsDefault(false);
        model.setClientId(null);
        model.setType(TemplateType.EXTERNAL_JOURNALING);
        service.updateCache(key, model);

        TemplateXmlModel cache = service.getFromCache(new Key(TemplateType.EXTERNAL_JOURNALING, model.getId()));
        Assert.assertNotNull(cache);
        Assert.assertEquals(cache.getId(), model.getId());
        Assert.assertEquals(cache.getType(), model.getType());

        TemplateXmlModel templateXmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        templateXmlModel.setId("1");
        templateXmlModel.setIsDefault(false);
        templateXmlModel.setClientId(null);
        templateXmlModel.setType(TemplateType.EXPORT_NOTIFICATION);
        service.updateCache(new Key(TemplateType.EXTERNAL_JOURNALING, templateXmlModel.getClientId(), templateXmlModel
            .getIsDefault()), templateXmlModel);

    }

    @Test
    public void testPutInCache() throws Exception {
        TemplateXmlModel xmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel.setId("1");
        xmlModel.setIsDefault(false);
        xmlModel.setClientId(null);
        hashMap.put("1", xmlModel);
        when(templatesMapBuilderService.getAllTemplatesMap()).thenReturn(hashMap);
        service.fillCache();

        TemplateXmlModel model = MockTemplateDataUtil.mockTemplateXmlModel();
        model.setId("2");
        model.setIsDefault(false);
        model.setClientId(null);
        Key key = new Key(TemplateType.EXPORT_NOTIFICATION, null, "2", false);
        service.putInCache(key, model);

        TemplateXmlModel fromCache = service.getFromCache(key);
        Assert.assertNotNull(fromCache);
        ReflectionAssert.assertReflectionEquals(model, fromCache);

        Key key1 = new Key(TemplateType.EXTERNAL_JOURNALING, null, "23", false);
        TemplateXmlModel model1 = MockTemplateDataUtil.mockTemplateXmlModel();
        model1.setId("23");
        model1.setType(TemplateType.EXTERNAL_JOURNALING);
        model1.setIsDefault(false);
        model1.setClientId(null);
        service.putInCache(key1, model1);

        TemplateXmlModel fromCache1 = service.getFromCache(key1);
        Assert.assertNotNull(fromCache1);
        ReflectionAssert.assertReflectionEquals(model1, fromCache1);

    }

    @Test
    public void testFillCache() throws Exception {
        TemplateXmlModel xmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel.setId("1");
        xmlModel.setIsDefault(false);
        xmlModel.setClientId(null);

        TemplateXmlModel xmlModel1 = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel1.setId("2");
        xmlModel1.setClientId(null);

        TemplateXmlModel xmlModel2 = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel2.setId("3");
        xmlModel2.setIsDefault(false);

        TemplateXmlModel xmlModel3 = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel3.setId("4");
        xmlModel3.setIsDefault(false);

        hashMap.put("1", xmlModel);
        hashMap.put("2", xmlModel1);
        hashMap.put("3", xmlModel2);
        hashMap.put("4", xmlModel3);
        when(templatesMapBuilderService.getAllTemplatesMap()).thenReturn(hashMap);
        service.fillCache();

    }

    @Test
    public void testReloadCache() throws Exception {
        TemplateXmlModel xmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel.setId("1");
        xmlModel.setIsDefault(false);
        xmlModel.setClientId(null);

        TemplateXmlModel xmlModel1 = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel1.setId("2");
        xmlModel1.setClientId(null);

        TemplateXmlModel xmlModel2 = MockTemplateDataUtil.mockTemplateXmlModel();
        xmlModel2.setId("3");
        xmlModel2.setIsDefault(false);
        hashMap.put("1", xmlModel);
        hashMap.put("2", xmlModel1);
        hashMap.put("3", xmlModel2);

        when(templatesMapBuilderService.getAllTemplatesMap()).thenReturn(hashMap);
        service.reloadCache();
        Collection<TemplateXmlModel> allByKey = service.getAllByKey(new Key(TemplateType.EXPORT_NOTIFICATION, true));
        Assert.assertNotNull(allByKey);
        Assert.assertFalse(allByKey.isEmpty());
        Assert.assertEquals(allByKey.size(), 1);
    }

}
