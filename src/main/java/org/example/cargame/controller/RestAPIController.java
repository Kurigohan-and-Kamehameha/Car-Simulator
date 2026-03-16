package org.example.cargame.controller;

import org.example.cargame.enums.Direction;
import org.example.cargame.enums.EngineType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/engine")
public class RestAPIController {

    @PostMapping("/engine/{type}")
    public String switchEngine(@PathVariable EngineType type) {
        return type.name();
    }

    @PostMapping("/color/{type}")
    public String switchColor(@PathVariable String color) {
        return color;
    }

    @PostMapping("/drive/{type}")
    public String moveDirection(@PathVariable Direction direction) {
        return direction.name();
    }
}
