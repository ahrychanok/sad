package com.smarsh.notificationservice.api.rest.controllers;

import com.smarsh.core.rest.service.controller.AbstractController;
import com.smarsh.core.rest.service.dto.SuccessResponseDto;
import com.smarsh.notificationservice.client.model.Template;
import com.smarsh.notificationservice.client.model.TemplateIdResponse;
import com.smarsh.notificationservice.client.model.TemplateType;
import com.smarsh.notificationservice.client.service.TemplateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

/**
 * @author Dzmitry_Sulauka Template management controller.
 */
@RestController
@RequestMapping(path = "/notifications/templates")
public class TemplateController extends AbstractController {

    private final TemplateService templateService;

    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @ApiOperation(value = "Upload template file")
    @RequestMapping(method = RequestMethod.POST,
                    path = "/upload",
                    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                    produces = JSON_MEDIA_TYPE)
    public SuccessResponseDto<TemplateIdResponse> saveFile(
        @RequestPart(name = "file") MultipartFile file,
        @RequestPart(name = "template") @Valid Template template) {

        return wrapWithResponse(templateService.uploadTemplate(template, file));
    }

    @ApiOperation(value = "Get template by id.")
    @GetMapping(path = "/types/{type}/{id}", produces = { JSON_MEDIA_TYPE})
    public void getTemplate(
        @PathVariable @ApiParam(value = "Template id for file getting", required = true) String id,
        @RequestParam(required = false) @ApiParam(value = "Template clientId", required = false) Integer clientId,
        @PathVariable @ApiParam(value = "Template type", required = true) TemplateType type,
        HttpServletResponse response) throws IOException {
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        try (InputStream is = templateService.getTemplate(id, clientId, type)) {

            OutputStream os = response.getOutputStream();
            if (is != null) {
                IOUtils.copy(is, os);
                response.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"template.html\""));
                os.close();
            }
        }
    }

    @ApiOperation(value = "Get templates metadata.")
    @GetMapping(path = "/types/{type}", produces = JSON_MEDIA_TYPE)
    public SuccessResponseDto<Collection<Template>> getTemplateMetadata(
        @RequestParam(required = false) @ApiParam(value = "Client id") Integer clientId,
        @PathVariable @ApiParam(value = "Template type", required = true) TemplateType type) {

        return wrapWithResponse(templateService.getTemplateMetadata(clientId, type));
    }

    @ApiOperation(value = "Update template by id.")
    @PutMapping(path = "/types/{templateType}/{id}", produces = JSON_MEDIA_TYPE)
    public SuccessResponseDto<Void> updateTemplate(
        @PathVariable @ApiParam(value = "Template type", required = true) TemplateType templateType,
        @PathVariable @ApiParam(value = "Template identifier", required = true) String id,
        @RequestPart(name = "file", required = false) MultipartFile file,
        @RequestPart(name = "template") Template template) {

        templateService.updateTemplate(templateType, id, template, file);
        return success();
    }

    @ApiOperation(value = "Delete template by id.")
    @DeleteMapping(path = "/types/{type}/{id}", produces = JSON_MEDIA_TYPE)
    public SuccessResponseDto<Void> deleteTemplate(
        @PathVariable @ApiParam(value = "Template id", required = true) String id,
        @RequestParam(required = false) @ApiParam(value = "Template clientId", required = false) Integer clientId,
        @PathVariable @ApiParam(value = "Template type", required = true) TemplateType type) {

        templateService.deleteTemplate(id, clientId, type);
        return success();
    }

    @ApiOperation(value = "Reload cache, just for testing.")
    @PostMapping(path = "/cache/reload", produces = JSON_MEDIA_TYPE)
    public SuccessResponseDto<Void> reloadCache() {

        templateService.reloadCache();
        return success();
    }

}
