package com.example.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket非controller，非service
 *
 * @author xuan
 * @create 2018-04-17 14:18
 **/
@Component
@ServerEndpoint("/webSocket")
@Slf4j
public class MyWebSocket {
    private Session session;
//    private static Set<MyWebSocket> webSocketSet = new HashSet<>();
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        log.info("【webSocket消息】有新的连接，当前总连接数：{}", webSocketSet.size());
    }

    @OnClose
    public void onClose() {
        try {
            webSocketSet.remove(this);
            log.info("【webSocket消息】有连接断开，当前总连接数：{}", webSocketSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message){
        log.info("【webSocket消息】收到客户端消息：{}", message);
    }

    /**
     * 发消息不那么重要，允许失败，所以此处catch处理，避免回滚调用方的service业务
     * @param message
     */
    public void sendMessage(String message){
        log.info("【webSocket消息】广播消息：{}", message);
        for (MyWebSocket webSocket : webSocketSet) {    //群发
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 谷歌浏览器刷新WebSocket连接会报错IOException: 你的主机中的软件中止了一个已建立的连接。
    // 但其他浏览器没问题，而且这个IO异常此处捕获不了，奇怪
//    @ExceptionHandler({IOException.class})
//    public void webSocketExceptionHandler(){
//        log.error("【WebSocket异常】你的主机中的软件中止了一个已建立的连接。");
//    }
}
