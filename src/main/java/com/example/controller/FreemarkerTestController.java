package com.example.controller;

import com.example.dto.OrderDTO;
import com.example.enums.SellExceptionEnum;
import com.example.exception.SellException;
import com.example.service.OrderMasterService;
import com.example.util.Page54Util;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author xuan
 * @create 2018-04-12 20:38
 **/
@Controller
@Slf4j
public class FreemarkerTestController {
    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private Configuration configuration;

//    @Value("${project.orderPageSize}")
//    private String orderPageSize;

    /**
     * 本方法是动态调用freemarker的模板
     *
     * @param pageNum
     * @param size
     * @return
     */
    @RequestMapping("/order/list")
    public ModelAndView test1(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "4") Integer size) {
        PageRequest pageRequest = new PageRequest(pageNum - 1, size);
        Page<OrderDTO> page = orderMasterService.findList(pageRequest);
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
//        freemarker的ftl模板中可以直接调用方法
//        map.put("totalPage", page.getTotalPages());
//        map.put("orders", page.getContent());
        map.put("currentPage", pageNum);
        int totalPage = page.getTotalPages();

//        分页值前五后四
        int[] beginAndEnd = Page54Util.getBeginAndEnd(totalPage, pageNum);
        map.put("beginPage", beginAndEnd[0]);
        map.put("endPage", beginAndEnd[1]);

        ModelAndView modelAndView = new ModelAndView("order/list", map);
        return modelAndView;
    }

    /**
     * freemarker生成静态页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/test2")
    @ResponseBody
    public String test2(HttpServletRequest request) {
        try {
            Template template = configuration.getTemplate("order/test2.ftl");
            HashMap<Object, Object> map = new HashMap<>();
            map.put("aa", 11);
            map.put("bb", 22);
            String realPath = request.getSession().getServletContext().getRealPath("/");
            File file = new File(realPath + "/aaa.html");
            Writer writer = new FileWriter(file);
            template.process(map, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int a = 10 / 0;
        return "123";
    }

    @RequestMapping("/test3")
    @ResponseBody
    public String exceptionTest(){
        throw new SellException(SellExceptionEnum.ORDER_NOT_EXIST);
//        return "ok";
    }

}
