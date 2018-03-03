package com.smarsh.notificationservice.api.service.filetree.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.smarsh.core.io.location.AwsS3Location;
import com.smarsh.core.io.location.Credentials;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * @author Dzmitry_Sulauka
 */
public class AwsS3TreeLoaderAdapterTest {

    @BeforeClass
    public void init() {

    }

    @Test
    public void testListAllObjects() throws Exception {
        AwsS3Location location = new AwsS3Location();
        location.setUri("uri");
        location.setBucket("bucket");
        Credentials credentials = new Credentials();
        credentials.setValue("value");
        credentials.setSecret("secret");
        location.setCredentials(credentials);
        AwsS3TreeLoaderAdapter spy = Mockito.spy(new AwsS3TreeLoaderAdapter(location));
        AmazonS3Client mock = Mockito.mock(AmazonS3Client.class);
        doReturn(mock).when(spy)
            .getAwsClient(eq(location));
        ObjectListing objectListing = Mockito.mock(ObjectListing.class);
        when(mock.listObjects(eq(location.getBucket()))).thenReturn(objectListing);
        S3ObjectSummary s3ObjectSummary = Mockito.mock(S3ObjectSummary.class);
        when(s3ObjectSummary.getKey()).thenReturn("file.xml");
        List<S3ObjectSummary> value = Arrays.asList(s3ObjectSummary);
        when(objectListing.getObjectSummaries()).thenReturn(value);
        List<String> strings = spy.listAllObjects();
        Assert.assertNotNull(strings);
        Assert.assertTrue(strings.size() > 0);
    }

    @Test
    public void testAwsClient() {
        AwsS3Location location = new AwsS3Location();
        location.setUri("uri");
        location.setBucket("bucket");
        Credentials credentials = new Credentials();
        credentials.setValue("value");
        credentials.setSecret("secret");
        location.setCredentials(credentials);
        AwsS3TreeLoaderAdapter spy = Mockito.spy(new AwsS3TreeLoaderAdapter(location));
        AmazonS3Client awsClient = spy.getAwsClient(location);
        Assert.assertNotNull(awsClient);
    }
}
