package org.example.deployment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@ConfigurationProperties(prefix = "app.custom")
@Getter
@Setter
public class HelloController {
    private String environment;
    @GetMapping
    @ResponseBody
    public Map<String, String> hello() {
        Map<String, String> myMap = new HashMap<>();
        String myEnv = environment;
        if (myEnv == null) {
            myEnv = "default";
        }

        myMap.put("myEnv", myEnv);
        return myMap;
    }
}
