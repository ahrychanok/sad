package com.smarsh.notificationservice.api.service.filetree.impl;

import com.smarsh.core.io.location.FileSystemLocation;
import com.smarsh.notificationservice.api.exception.PathResolvingException;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.Mockito.when;

/**
 * @author Dzmitry_Sulauka
 */
public class FileSystemTreeLoaderAdapterTest {

    @Test(expectedExceptions = PathResolvingException.class)
    public void testListAllObjectsNegative() throws Exception {
        FileSystemLocation fileSystemLocation = Mockito.mock(FileSystemLocation.class);
        FileSystemTreeLoaderAdapter fileSystemTreeLoaderAdapter = new FileSystemTreeLoaderAdapter(fileSystemLocation);
        when(fileSystemLocation.getPath()).thenThrow(IOException.class);
        fileSystemTreeLoaderAdapter.listAllObjects();
    }
}
