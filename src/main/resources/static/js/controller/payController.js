app.controller('payController', function ($scope, payService) {
    //统一下单（生成二维码）
    $scope.createNative = function () {
        payService.createNative().success(
            function (response) {
                $scope.out_trade_no = response.out_trade_no;    //订单号
                $scope.money = (response.total_fee / 100).toFixed(2);   //金额(元)
                //生成二维码图片
                var qr = new QRious({
                    element: document.getElementById('qrious'),
                    size: 250,
                    level: "H",
                    value: response.code_url    //微信支付路径，后台调用统一下单API接口后返回的
                    // weixin://wxpay/bizpayurl?pr=f3Fgj1Q
                    // weixin://wxpay/bizpayurl?pr=pcmH2Fm
                });

                queryPayStatus(response.out_trade_no);//查询订单状态
            }
        );
    }

    //注：方法不能加this，奇怪
    queryPayStatus = function (out_trade_no) {
        payService.queryPayStatus(out_trade_no).success(
            function (response) {
                if (response.code == 1) {
                    location.href = "../html/paysuccess.html";
                } else if (response.code == 3) {
                    location.href = "../html/payfail.html";
                } else if (response.code == 2) {    //支付超时，重新生成二维码
                    $scope.createNative();
                }
            }
        );
    }
});