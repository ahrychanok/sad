package com.smarsh.notificationservice.api.adapter;

import com.smarsh.notificationservice.api.service.builder.MessageBuilderAdapter;
import com.smarsh.notificationservice.api.service.builder.adapter.AutoDetectingAdapter;
import com.smarsh.notificationservice.api.service.builder.adapter.FileMessageBuilderAdapter;
import com.smarsh.notificationservice.api.service.builder.adapter.TemplateMessageBuilderAdapter;
import com.smarsh.notificationservice.api.service.builder.adapter.TextMessageBuilderAdapter;
import com.smarsh.notificationservice.client.model.AdapterType;
import com.smarsh.notificationservice.client.model.EmailLocation;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class AutoDetectingAdapterTest {

    @InjectMocks
    AutoDetectingAdapter autoDetectingAdapter;

    FileMessageBuilderAdapter fileMessageBuilderAdapter = Mockito.mock(FileMessageBuilderAdapter.class);
    TextMessageBuilderAdapter textMessageBuilderAdapter = Mockito.mock(TextMessageBuilderAdapter.class);
    TemplateMessageBuilderAdapter templateMessageBuilderAdapter = Mockito.mock(TemplateMessageBuilderAdapter.class);

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void setUp() throws Exception {

        autoDetectingAdapter.register(AdapterType.FILE, fileMessageBuilderAdapter);
        autoDetectingAdapter.register(AdapterType.TEXT, textMessageBuilderAdapter);
        autoDetectingAdapter.register(AdapterType.TEMPLATE, templateMessageBuilderAdapter);

        MessageBuilderAdapter mock = Mockito.mock(MessageBuilderAdapter.class);
        when(mock.buildMessage(any(EmailMetadata.class))).thenReturn(any(MimeMessage.class));
    }

    @Test
    public void testTemplateAdapter() throws Exception {
        EmailMetadata metadata = new EmailMetadata();
        metadata.setTemplateId("templateId");
        autoDetectingAdapter.buildMessage(metadata);
        verify(templateMessageBuilderAdapter).buildMessage(any(EmailMetadata.class));
    }

    @Test
    public void testFileAdapter() throws Exception {
        EmailMetadata metadata = new EmailMetadata();
        metadata.setLocation(new EmailLocation());
        autoDetectingAdapter.buildMessage(metadata);
        verify(fileMessageBuilderAdapter).buildMessage(any(EmailMetadata.class));
    }

    @Test
    public void testTextAdapter() throws Exception {
        EmailMetadata metadata = new EmailMetadata();
        autoDetectingAdapter.buildMessage(metadata);
        verify(textMessageBuilderAdapter).buildMessage(any(EmailMetadata.class));
    }
}
