package com.coedev;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // another comment added
public class PingPongController {

    private static int COUNTER = 0;

    record PingPong(String result){}

    @GetMapping("/ping")
    public PingPong getPingPong(){
        //return new PingPong("Pong: " + ++COUNTER);
        return new PingPong("Pong: %s".formatted(++COUNTER));
    }
}
