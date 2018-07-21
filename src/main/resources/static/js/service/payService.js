app.service('payService', function ($http) {
    //统一下单
    this.createNative = function () {
        return $http.get('../pay/createNative');
    }
    //查询订单（后台需要轮询调用微信订单查询API，查看订单支付状态）
    this.queryPayStatus = function (out_trade_no) {
        return $http.get('../pay/queryOrderStatus?out_trade_no=' + out_trade_no);
    }
});