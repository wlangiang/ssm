<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>财务报表</title>
    <%@include file="../include/css.jsp"%>
    <link rel="stylesheet" href="/static/plugins/datatables1.10.13/media/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="/static/plugins/datatables1.10.13/extensions/FixedHeader/css/fixedHeader.bootstrap.min.css">
    <link rel="stylesheet" href="/static/plugins/datepicker/datepicker3.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../include/header.jsp"%>
    <jsp:include page="../include/sider.jsp">
        <jsp:param name="menu" value="finance_day"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">
            <div class="box">
                <div class="box-body">
                    <form class="form-inline">
                        <input type="text" class="form-control" id="date">
                    </form>
                </div>
            </div>

            <!-- Default box -->
            <div class="box box-primary box-solid">
                <div class="box-header with-border">
                    <h3 class="box-title">财务日报</h3>
                    <%--<div class="box-tools pull-right">
                        <a href="javascript:;" id="exportCsv" class="btn btn-default"><i class="fa fa-file-o"></i> 导出Excel</a>
                    </div>--%>
                    <div class="box-tools pull-right">
                        <a href="javascript:;" id="exportCsv" class="btn btn-default"><i class="fa fa-file-o"></i> 导出Excel</a>

                        <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                            <i class="fa fa-minus"></i></button>
                        <button type="button" class="btn btn-box-tool" data-widget="remove" data-toggle="tooltip" title="Remove">
                            <i class="fa fa-times"></i></button>
                    </div>
                </div>
                <div class="box-body">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>流水号</th>
                                <th>创建时间</th>
                                <th>业务类型</th>
                                <th>业务模块</th>
                                <th>金额</th>
                                <th>业务流水</th>
                                <th>状态</th>
                                <th>创建人</th>
                                <th>备注</th>
                                <th>#</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <!-- /.box-body -->
                <div id="date1">
                </div>
                <div id="main" style="width: 600px; height: 400px" class="box-foot">

                </div>
            </div>
            <!-- /.box -->

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

</div>

<%@include file="../include/js.jsp"%>
<script src="/static/plugins/datatables1.10.13/media/js/jquery.dataTables.min.js"></script>
<script src="/static/plugins/datatables1.10.13/media/js/dataTables.bootstrap.min.js"></script>
<script src="/static/plugins/datatables1.10.13/extensions/FixedHeader/js/dataTables.fixedHeader.min.js"></script>
<script src="/static/plugins/layer/layer.js"></script>
<script src="/static/plugins/moment.js"></script>
<script src="/static/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="/static/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script src="/static/plugins/echarts.min.js"></script>
<script>
    $(function(){
        $("#date").val(moment().format("YYYY-MM-DD"));
        /*html=""+moment().format("YYYY-MM-DD")+"：收入"+"<span class='alert-success'>"+1000+"</span>"+
            "元，支出"+"<span class='alert-error'>"+2000.00+"</span>元";
        $("#date1").append(html);*/

        $("#date").datepicker({
            format:'yyyy-mm-dd',
            language:"zh-CN",
            autoClose:true,
            endDate:moment().format("YYYY-MM-DD")
        }).on("changeDate",function(e){
            var today = e.format(0,'yyyy-mm-dd');
            table.ajax.reload();
           /* html="<h3>"+today+"：收入"+"<span class='alert-success'>"+1000+"</span>"+
                "元，支出"+"<span class='alert-error'>"+2000.00+"</span>"+元+"</h3>";
            $("#date1").append(html);*/

        })


       var table = $(".table").DataTable({
          /* "lengthMenu": [10,50,100],*/
           "lengthChange":false,
           "pageLength": 10,
           "serverSide": true,
           "ajax":{
               "url":"/finance/upload",
               "type":"get",
               "data":function(obj){
                   obj.day = $("#date").val();
               }

           },
           "searching":false,//不使用自带的搜索
           "order":[[0,'desc']],//默认排序方式
           "ordering": false,
           "columns":[
               {"data":"id","name":"id"},
               {"data":"serialNumber"},
               {"data":"createDate"},
               {"data":"type"},
               {"data":"module"},
               {"data":"money"},
               {"data":"moduleSerialNumber"},
               {"data":"state"},
               {"data":"createUser"},
               {"data":"remark"},
               {"data":function(row){
                    if(row.state=='未到账'){
                        return "<a href='javascript:;' rel='"+row.id+"' class='btn btn-xs btn-default checkBtn'>确认</a>";
                        ;
                    }{
                       return "";
                    }

               }}

           ],
           "columnDefs":[
               {targets:[0],visible: false},
           ],//第0列不显示
           "language":{ //定义中文
               "search": "搜索:",
               "zeroRecords":    "没有匹配的数据",
               "lengthMenu":     "显示 _MENU_ 条数据",
               "info":           "显示从 _START_ 到 _END_ 条数据 共 _TOTAL_ 条数据",
               "infoFiltered":   "(从 _MAX_ 条数据中过滤得来)",
               "loadingRecords": "加载中...",
               "processing":     "处理中...",
               "paginate": {
                   "first":      "首页",
                   "last":       "末页",
                   "next":       "下一页",
                   "previous":   "上一页"
               }
           }
       });
       new $.fn.dataTable.FixedHeader(table);

       $(document).delegate(".checkBtn","click",function(){
          var id = $(this).attr("rel");
          layer.confirm("确认已收款?",function(index){
              layer.close(index);
              $.post("/finance/confirm/"+id).done(function(data){
                if(data.status=='success'){
                    layer.msg("确认成功");
                    table.ajax.reload(false,null);
                }else{
                    layer.msg(data.message);
                }

              }).error(function(){
                layer.msg("服务器忙，请稍后");
              });

          });
       });
        $("#exportCsv").click(function(){
           var day = $("#date").val();
           window.location.href="/finance/day/"+day+"/data.xls"

        });

        var mychart = echarts.init($("#main")[0]);

        var option = {
            title:{
                text:"当日收入饼状图",
                left: 'center',
                top:20
            },
            tooltip:{},
            legend:{
                orient: 'vertical',
                x: 'left',
                data:['设备租赁','劳务派遣','收入']
            },
            series:[{
                name:'收入',
                type:'pie',
                data:[
                    {value:20,name:'设备租赁'},
                    {value:80,name:'劳务派遣'},
                    {value:160,name:'收入'}
                ]
            }]
        };
        mychart.setOption(option);


    });

</script>

</body>
</html>
