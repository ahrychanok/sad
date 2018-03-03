package com.smarsh.blueprint;

import org.springframework.stereotype.Component;

/**
 * Hello world!
 */
@Component
public class BlueprintServiceImpl implements BlueprintService {

    @Override
    public String getHello(String world) {
        return "Hello" + world + ". My verision is 2";
    }

}
