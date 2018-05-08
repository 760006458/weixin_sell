package com.example.controller;

import com.example.websocket.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xuan
 * @create 2018-04-17 16:18
 **/
@Controller
@RequestMapping("/webSocket")
public class WebSocketController {

    @Autowired
    private MyWebSocket myWebSocket;

    @RequestMapping("/test")
    public String test() {
        return "websocket/WebSocketHtml";
    }

    @RequestMapping("/sendMessage")
    @ResponseBody
    public String sendMessage() {
        myWebSocket.sendMessage("这是后台给前台发送的消息");
        return "ok";
    }

}
