package com.smarsh.blueprint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smarsh.blueprint.BlueprintService;
import com.smarsh.core.rest.service.controller.AbstractController;
import com.smarsh.core.rest.service.dto.SuccessResponseDto;

@RestController
public class BlueprintServiceController extends AbstractController {

    private BlueprintService blueprintService;

    @Autowired
    public BlueprintServiceController(BlueprintService blueprintService) {
        super();
        this.blueprintService = blueprintService;
    }

    @GetMapping(path = "/greetings", produces = JSON_MEDIA_TYPE)
    SuccessResponseDto<String> getHello(@RequestParam String world) {
        return wrapWithResponse(blueprintService.getHello(world));
    }

}
