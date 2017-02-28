<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>设备租赁合同显示</title>
    <%@include file="../../include/css.jsp"%>
    <link rel="stylesheet" href="/static/plugins/uploader/webuploader.css">
    <link rel="stylesheet" href="/static/plugins/datepicker/datepicker3.css">
    <link rel="stylesheet" href="/static/plugins/select2/select2.min.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper" id="app">

    <%@include file="../../include/header.jsp"%>
    <jsp:include page="../../include/sider.jsp">
        <jsp:param name="menu" value="business_device_rent"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">
            <h3 style="text-align: center" class="visible-print-block">凯盛软件租赁合同清单</h3>
            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">合同详情</h3>

                    <div class="box-tools pull-right hidden-print">
                        <button id="print" class="btn btn-default btn-sm"><i class="fa fa-print">打印</i></button>
                    </div>
                </div>
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>公司名称</label>&nbsp&nbsp
                                ${deviceRent.companyName}
                            </div>
                            <div class="form-group">
                                <label>联系电话</label>&nbsp&nbsp
                                ${deviceRent.tel}
                            </div>
                            <div class="form-group">
                                <label>租赁日期</label>&nbsp&nbsp
                                ${deviceRent.rentDate}
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>法人代表</label>&nbsp&nbsp
                                ${deviceRent.linkMan}
                            </div>
                            <div class="form-group">
                                <label>地址</label>&nbsp&nbsp
                                ${deviceRent.address}
                            </div>
                            <div class="form-group">
                                <label>归还日期</label>&nbsp&nbsp
                                ${deviceRent.backDate}
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>身份证号</label>&nbsp&nbsp
                                ${deviceRent.cardNum}
                            </div>
                            <div class="form-group">
                                <label>传真</label>&nbsp&nbsp
                                ${deviceRent.fax}
                            </div>
                            <div class="form-group">
                                <label>总天数</label>&nbsp&nbsp
                                ${deviceRent.totalDay}
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->

            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">设备列表</h3>
                </div>
                <div class="box-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>设备名称</th>
                            <th>单位</th>
                            <th>租赁单价</th>
                            <th>数量</th>
                            <th>总价</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${rentDetailList}" var="rentDetail">
                            <tr>
                                <td>${rentDetail.deviceName}</td>
                                <td>${rentDetail.deviceUnit}</td>
                                <td>${rentDetail.devicePrice}</td>
                                <td>${rentDetail.num}</td>
                                <td>${rentDetail.totalPrice}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer" style="text-align: right">
                    总租赁费${deviceRent.totalPrice} 元 预付款 ${deviceRent.preCost} 元 尾款 ${deviceRent.lastCost} 元
                </div>
            </div>

            <div class="box hidden-print">
                <div class="box-header">
                    <h3 class="box-title">合同扫描件</h3>
                    <div class="box-tools pull-right">
                        <a href="/device/rent/download/zip?id=${deviceRent.id}" class="btn btn-sm btn-default">
                            <i class="fa fa-file-zip">打包下载</i>
                        </a>
                    </div>
                </div>
                <div class="box-body">
                    <ul>
                        <c:forEach items="${rentDocList}" var="rentDoc">
                            <li><a href="/device/rent/download?id=${rentDoc.id}">${rentDoc.sourceName}</a></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

</div>

<%@include file="../../include/js.jsp"%>
<script src="/static/plugins/uploader/webuploader.min.js"></script>
<script src="/static/plugins/moment.js"></script>
<script src="/static/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="/static/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script src="/static/plugins/select2/select2.full.min.js"></script>
<script src="/static/plugins/vue.js"></script>
<script src="/static/plugins/layer/layer.js"></script>
<script>
    $(function(){
       $("#print").click(function(){
           window.print();
       });
    });
</script>
</body>
</html>

