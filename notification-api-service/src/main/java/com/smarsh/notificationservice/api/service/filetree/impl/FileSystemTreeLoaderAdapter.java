package com.smarsh.notificationservice.api.service.filetree.impl;

import com.smarsh.core.io.api.Location;
import com.smarsh.core.io.location.FileSystemLocation;
import com.smarsh.notificationservice.api.exception.PathResolvingException;
import com.smarsh.notificationservice.api.service.filetree.FileTreeLoaderAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dzmitry_Sulauka
 */
public class FileSystemTreeLoaderAdapter implements FileTreeLoaderAdapter {

    private final Location location;

    public FileSystemTreeLoaderAdapter(Location location) {
        this.location = location;
    }

    @Override
    public List<String> listAllObjects() {

        FileSystemLocation fileSystemLocation = (FileSystemLocation) location;
        try {
            return  Files.walk(Paths.get(fileSystemLocation.getPath()))
                .filter(p -> p.toString()
                    .endsWith(".xml"))
                .map(p -> Paths.get(fileSystemLocation.getPath())
                    .relativize(p)
                    .toString())
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PathResolvingException("Error while trying to load file tree", e);
        }
    }
}
