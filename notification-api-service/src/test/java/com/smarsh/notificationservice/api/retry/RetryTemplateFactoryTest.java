package com.smarsh.notificationservice.api.retry;

import com.smarsh.notificationservice.api.exception.TemplateMissingException;
import org.springframework.retry.support.RetryTemplate;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Dzmitry_Sulauka
 */
public class RetryTemplateFactoryTest {

    @Test
    public void templateFactoryTest() {

        RetryTemplate retryTemplate =
            RetryTemplateFactory.createRetryTemplate(2, 500, Arrays.asList(IllegalArgumentException.class));
        Assert.assertNotNull(retryTemplate);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        try {
            retryTemplate.execute(rc -> {
                atomicInteger.incrementAndGet();
                throw new IllegalArgumentException();
            });
        } catch (Exception e) {
        }
        Assert.assertTrue(atomicInteger.get() == 1);

        AtomicInteger atomicInteger1 = new AtomicInteger(0);
        try {
            retryTemplate.execute(rc -> {
                atomicInteger1.incrementAndGet();
                throw new TemplateMissingException();
            });
        } catch (Exception e) {
        }
        Assert.assertTrue(atomicInteger1.get() == 2);
    }

    @Test
    public void templateFactoryNoRetryTest() {

    }

}
