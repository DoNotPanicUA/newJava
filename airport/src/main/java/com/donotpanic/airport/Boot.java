package com.donotpanic.airport;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Boot {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Boot.class).headless(false).run(args);
        context.setParent(new ClassPathXmlApplicationContext("ApplicationContext.xml"));
        App.runApplication(context);
    }

}