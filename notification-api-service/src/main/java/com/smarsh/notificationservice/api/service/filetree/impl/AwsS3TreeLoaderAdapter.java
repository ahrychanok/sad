package com.smarsh.notificationservice.api.service.filetree.impl;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.smarsh.core.io.api.Location;
import com.smarsh.core.io.location.AwsS3Location;
import com.smarsh.core.io.location.Credentials;
import com.smarsh.notificationservice.api.service.filetree.FileTreeLoaderAdapter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dzmitry_Sulauka
 */
public class AwsS3TreeLoaderAdapter implements FileTreeLoaderAdapter {

    private final Location location;

    public AwsS3TreeLoaderAdapter(Location location) {
        this.location = location;
    }

    @Override
    public List<String> listAllObjects() {
        AwsS3Location awsS3Location = (AwsS3Location) location;
        AmazonS3Client s3Client = getAwsClient(awsS3Location);
        return s3Client.listObjects(awsS3Location.getBucket())
            .getObjectSummaries()
            .stream()
            .filter(i -> i.getKey()
                .endsWith(".xml"))
            .map(S3ObjectSummary::getKey)
            .collect(Collectors.toList());
    }

    protected AmazonS3Client getAwsClient(AwsS3Location location) {
        Credentials credentials = location.getCredentials();
        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(credentials.getValue(), credentials.getSecret()));
        s3Client.setEndpoint(location.getUri());
        return s3Client;
    }
}
