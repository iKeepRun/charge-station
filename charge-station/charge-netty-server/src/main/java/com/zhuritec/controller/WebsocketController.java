package com.zhuritec.controller;

import com.zhuritec.service.WebsocketPushService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebsocketController {
    @Resource
    private WebsocketPushService websocketPushService;
    @PostMapping("/pushAll")
    public void pushAll(@RequestBody  String message){
        websocketPushService.pushAll(message);
    }

    @PostMapping("/pushById/{userid}")
    public void pushById(@RequestBody String message, @PathVariable("userid") String userid){
        websocketPushService.pushById(message, userid);
    }
}
