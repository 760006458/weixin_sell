package com.example.controller.qrcode;

import com.example.enums.PayStatusEnum;
import com.example.pay.qrcode.WeixinPayService;
import com.example.util.IdWorker;
import com.example.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

/**
 * @author xuan
 * @create 2018-05-08 20:21
 **/
@Controller
@RequestMapping("/pay")
public class PayController {

    @Value("${qrcode.queryCount}")
    private int queryCount;

    @Autowired
    private WeixinPayService weixinPayService;

    @RequestMapping("/toPay")
    public String toPayPage() {
        return "pay/pay";
    }

    /**
     * 调用微信支付API，进行预支付，返回数据中有支付链接，有前端负责生成二维码
     *
     * @return
     */
    @RequestMapping("/createNative")
    @ResponseBody
    public Map createNative() {
        IdWorker idworker = new IdWorker();
        //注：订单号和订单金额需要从Redis取，因为这是下订单时存进去的
        return weixinPayService.createNative(idworker.nextId() + "", "1");
    }

    @RequestMapping("/queryOrderStatus")
    @ResponseBody
    public ResultVo queryOrderStatus(@RequestParam String out_trade_no) {
        int count = 0;
        while (count++ < queryCount) {
            //调用查询接口
            Map<String, String> map = weixinPayService.queryPayStatus(out_trade_no);
            if (map == null) {
                return new ResultVo(PayStatusEnum.FAIL.getCode(), PayStatusEnum.FAIL.getMsg());
            }
            if (map.get("trade_state").equals("SUCCESS")) {
                //注：支付成功后，还需要修改数据库订单支付状态
                return new ResultVo(PayStatusEnum.SUCCESS.getCode(), PayStatusEnum.SUCCESS.getMsg());
            }
            try {
                Thread.sleep(3000);//间隔三秒轮询一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new ResultVo(PayStatusEnum.TIMEOUT.getCode(), PayStatusEnum.TIMEOUT.getMsg());
    }
}