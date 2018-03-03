package com.smarsh.notificationservice.api.service.impl;

import com.smarsh.core.io.api.CoreIOException;
import com.smarsh.core.io.api.Location;
import com.smarsh.core.io.api.Resource;
import com.smarsh.core.io.api.ResourceLocation;
import com.smarsh.core.io.api.Storage;
import com.smarsh.core.io.location.FileSystemLocation;
import com.smarsh.notificationservice.api.exception.ResourceAccessException;
import com.smarsh.notificationservice.api.exception.TemplateTransformationException;
import com.smarsh.notificationservice.api.exception.TemplateUploadingException;
import com.smarsh.notificationservice.api.model.TemplateXmlModel;
import com.smarsh.notificationservice.api.service.FetchResourceService;
import com.smarsh.notificationservice.api.service.TemplateStoringService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateStoringServiceImpl implements TemplateStoringService {

    private final FetchResourceService fetchResourceService;
    private final JAXBContext jaxbContext;
    private final Storage storage;
    private final Location location;

    public TemplateStoringServiceImpl(FetchResourceService fetchResourceService, Storage storage, Location location) {
        this.fetchResourceService = fetchResourceService;
        this.storage = storage;
        this.location = location;
        try {
            this.jaxbContext = JAXBContext.newInstance(TemplateXmlModel.class);
        } catch (JAXBException e) {
            throw new TemplateTransformationException("Unnable to create jaxb context", e);
        }
    }

    @Override
    public TemplateXmlModel unmarshalXml(String path) {

        try (InputStream is = fetchResourceService.fetchResource(path)) {
            XMLInputFactory newFactory = XMLInputFactory.newFactory();
            XMLEventReader reader = newFactory.createXMLEventReader(is);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<TemplateXmlModel> element = unmarshaller.unmarshal(reader, TemplateXmlModel.class);

            if (element.getValue() == null) {
                throw new TemplateTransformationException(String.format("Error while parsing template file.File path=[%s]",
                    path));
            }

            return element.getValue();

        } catch (JAXBException | CoreIOException | IOException | XMLStreamException e) {
            throw new TemplateTransformationException(String.format("Error while trying to unmarshal template file path=[%s]",
                path), e);
        }
    }

    @Override
    public void marshalXml(TemplateXmlModel model, String path) {

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "utf-8");
            marshaller.marshal(model, os);

            Resource resource =
                storage.forItem(path, location)
                    .get();
            if (location instanceof FileSystemLocation) {
                Path filePath = Paths.get(((FileSystemLocation) location).getPath(), path);
                if (filePath.getParent() != null) {
                    Files.createDirectories(filePath.getParent());
                }
                Files.copy(new ByteArrayInputStream(os.toByteArray()), filePath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                resource.save(new ByteArrayInputStream(os.toByteArray()));
            }
        } catch (JAXBException e) {
            throw new TemplateTransformationException("Error while marshalling template", e);
        } catch (IOException e) {
            throw new TemplateUploadingException("Error while trying to close stream", e);
        } catch (CoreIOException e) {
            throw new TemplateUploadingException("Error while trying to save template on storage", e);
        }

    }

    @Override
    public void removeFile(String path) {
        try {
            Resource resource =
                storage.forItem(path, location)
                    .get();
            resource.delete();
        } catch (CoreIOException e) {
            throw new ResourceAccessException("Error while trying to delete template from storage.templatePath=" + path, e);
        }

    }

    @Override
    public void moveFile(String base, String to) {
        try {
            Resource resource =
                storage.forItem(base, location)
                    .get();
            ResourceLocation toLocation = location.asResourceLocation(to, Optional.empty(), Optional.empty());
            if (location instanceof FileSystemLocation) {
                Path filePath = Paths.get(((FileSystemLocation) location).getPath(), to);
                if (filePath.getParent() != null) {
                    Files.createDirectories(filePath.getParent());
                }
            }
            resource.move(toLocation);
        } catch (CoreIOException e) {
            throw new ResourceAccessException(String.format(
                "Error while trying to move template.TemplateBasePath=[%s] locationToMove=[%s]", base, to), e);
        } catch (IOException e) {
            throw new ResourceAccessException(e);
        }
    }
}
