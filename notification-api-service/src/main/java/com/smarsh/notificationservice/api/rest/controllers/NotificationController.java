package com.smarsh.notificationservice.api.rest.controllers;

import com.smarsh.core.rest.service.controller.AbstractController;
import com.smarsh.core.rest.service.dto.SuccessResponseDto;
import com.smarsh.notificationservice.api.service.QueueSenderService;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dzmitry_Sulauka
 */
@RestController
public class NotificationController extends AbstractController {

    private final QueueSenderService senderService;

    @Autowired
    public NotificationController(QueueSenderService senderService) {
        this.senderService = senderService;
    }

    @ApiOperation(value = "Post notification metadata")
    @PostMapping(path = "/notifications/email", consumes = JSON_MEDIA_TYPE)
    public SuccessResponseDto<Void> createEmail(@RequestBody EmailMetadata metadata) {
        senderService.sentToTheQueue(metadata);
        return success();
    }
}
