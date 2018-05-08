package com.example.vo;

import lombok.Data;

import java.util.List;

/**
 * @author xuan
 * @create 2018-04-04 21:39
 **/
@Data
public class ResultVo {
    private Integer code;
    private String msg;
    private Object data;    //此处不一定是List集合
}
