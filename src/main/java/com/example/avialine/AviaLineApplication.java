package com.example.avialine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.*;

@Slf4j
@SpringBootApplication
public class AviaLineApplication{

    @Value("${server.port}")
    private String value;

    public static void main(String[] args) {
        SpringApplication.run(AviaLineApplication.class, args);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void openSwagger(){
        try {

            Runtime.getRuntime().exec("open -a Safari http://localhost:" + value + "/swagger-ui/index.html#/auth-controller/register");

        }catch (Exception e){
            log.debug(e.getMessage());
        }
    }

}
