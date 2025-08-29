package com.zhuritec.micro;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "charge-netty-server", url = "http://localhost:8085")
public interface WebsocketClient {
    @PostMapping("/pushAll")
    public void pushAll(@RequestBody String message);

    //    @PostMapping("/pushById")
//    public void pushById(String message, String id);
    @PostMapping("/pushById/{userid}")
    public void pushById(@RequestBody String message, @PathVariable("userid") String userid);
}
