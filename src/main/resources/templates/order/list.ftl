<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.bootcss.com/bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
    <title>Freemarker + BootStrap测试</title>
</head>
<body>
    <div class="container">
        <div class="row clearfix">
            <div class="col-md-12 column">
                <table class="table">
                    <thead>
                    <tr>
                        <th>
                            订单编号
                        </th>
                        <th>
                            买家姓名
                        </th>
                        <th>
                            买家电话
                        </th>
                        <th>
                            订单金额
                        </th>
                        <th>
                            订单状态
                        </th>
                        <th>
                            支付状态
                        </th>
                        <th>
                            时间
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list page.getContent() as order>
                        <#if order_index%2 == 0>
                            <tr>
                        <#else>
                            <tr class="success">
                        </#if>
                            <td>
                                ${order.orderId}
                            </td>
                            <td>
                                ${order.buyerName}
                            </td>
                            <td>
                                ${order.buyerPhone}
                            </td>
                            <td>
                                ${order.orderAmount}
                            </td>
                            <td>
                                ${order.payStatusByCode}
                            </td>
                            <td>
                                ${order.orderStatusByCode}
                            </td>
                            <td>
                                ${order.createTime?string('yyyy-MM-dd HH:mm:ss')}
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <div class="col-md-12 column">
                <ul class="pagination pull-right">
                    <#if currentPage lte 1>  <#--小于等于-->
                        <li class="disabled"><a href="#">上一页</a></li>
                    <#else>
                        <li><a href="/sell/order/list?page=${currentPage-1}&size=4">上一页</a></li>
                    </#if>

                    <#--<#assign totalPage = page.getTotalPages()/>-->
                    <#--分页值前五后四，由于freemarker模板中不方便些Java代码，所以在后台进行计算beginPage和endPage-->
                    <#list beginPage..endPage as index>
                        <#if currentPage == index>
                            <li class="disabled">
                        <#else>
                            <li>
                        </#if>
                            <a href="/sell/order/list?page=${index}&size=4">${index}</a>
                        </li>
                    </#list>

                    <#if currentPage gte page.getTotalPages()>  <#--大于等于-->
                        <li class="disabled"><a href="#">下一页</a></li>
                    <#else>
                        <li><a href="/sell/order/list?page=${currentPage+1}&size=4">下一页</a></li>
                    </#if>
                </ul>
            </div>
        </div>
    </div>
</body>
</html>