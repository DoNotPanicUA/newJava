package com.dnp;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class App {

    @RequestMapping("/hi")
    String home(){
        try {ChildClass c = (new ChildClass("###"));
            c.setStr("@@@");
            return "Hi :). Class type:" +c.toString() + "; " +
                    c.clone().toString();
        }catch (Exception e){
            return "Error: " + e.getMessage();
        }

    }

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }
}