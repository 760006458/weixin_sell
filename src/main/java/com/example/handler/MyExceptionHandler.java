package com.example.handler;

import com.example.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

/**
 * @author xuan
 * @create 2018-04-15 16:46
 **/
@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler({NullPointerException.class, ArithmeticException.class})
    public String exceptionHandler() {
        log.error("【抛空指针异常了】");
        return "error/error";
    }

    @ExceptionHandler({SellException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void exceptionHandler2(){
        log.error("【抛SellException异常了】使用@ResponseStatus(400)状态码，浏览器状态码由此变化");
    }
}
