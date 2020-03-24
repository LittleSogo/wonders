<%--
  Created by IntelliJ IDEA.
  User: zj
  Date: 2020/3/14
  Time: 14:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>bootstrap表格</title>
    <link rel="stylesheet" href="../bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="../bootstrap/css/bootstrap-table.css">
    <script src="../js/jquery-2.1.1.min.js"></script>
    <script src="../bootstrap/js/bootstrap.js"></script>
    <script src="../bootstrap/js/bootstrap-table.js"></script>
    <script src="../bootstrap/js/bootstrap-table-zh-CN.js"></script>

</head>
<body>
<body>
<div class="panel-body" style="padding-bottom:0px;">
    <div class="panel panel-default">
        <div class="panel-heading">查询条件</div>
        <div class="panel-body">
            <form id="formSearch" class="form-horizontal">
                <div class="form-group" style="margin-top:15px">
                    <label class="control-label col-sm-1" for="txt_search_departmentname">部门名称</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" id="txt_search_departmentname">
                    </div>
                    <label class="control-label col-sm-1" for="txt_search_statu">状态</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" id="txt_search_statu">
                    </div>
                    <div class="col-sm-4" style="text-align:left;">
                        <button type="button" style="margin-left:50px" id="btn_query" class="btn btn-primary">查询</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div id="toolbar" class="btn-group">
        <button id="btn_add" type="button" class="btn btn-default" data-toggle="modal" data-target="#addModal">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
        </button>
        <button id="btn_edit" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>修改
        </button>
        <button id="btn_delete" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
        </button>
        <button id="btn_upload" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-cloud-upload" aria-hidden="true"></span>删除
        </button>
    </div>
    <div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">添加记录</h4>
                </div>
                <div class="modal-body">
                    <form role="form" action="javascript:void(0)">
                        <div class="form-group">
                            <input type="text" class="form-control" id="name" placeholder="请输入名称">
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" id="age" placeholder="请输入年龄">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="addRecord">提交</button>
                </div>
            </div>
        </div>
    </div>
    <table id="tb_departments"></table>
</div>
</body>
<script type="text/javascript">

    var basePath ="<%=request.getContextPath()%>";

    $(function () {

        //1.初始化Table
        var oTable = new TableInit();
        oTable.Init();

        //2.初始化Button的点击事件
        var oButtonInit = new ButtonInit();
        oButtonInit.Init();

    });

    var TableInit = function () {
        var oTableInit = new Object();
        //初始化Table
        oTableInit.Init = function () {
            $('#tb_departments').bootstrapTable({
                url: basePath+'/test',         //请求后台的URL（*）
                method: 'get',                      //请求方式（*）
                toolbar: '#toolbar',                //工具按钮用哪个容器
                striped: true,                      //是否显示行间隔色
                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pagination: true,                   //是否显示分页（*）
                sortable: false,                     //是否启用排序
                sortOrder: "asc",                   //排序方式
                queryParams: oTableInit.queryParams,//传递参数（*）
                sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
                pageNumber:1,                       //初始化加载第一页，默认第一页
                pageSize: 10,                       //每页的记录行数（*）
                pageList: [5, 10, 25, 50, 100],        //可供选择的每页的行数（*）
                search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
                strictSearch: true,
                showColumns: true,                  //是否显示所有的列
                showRefresh: true,                  //是否显示刷新按钮
                minimumCountColumns: 2,             //最少允许的列数
                clickToSelect: true,                //是否启用点击选中行
                height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
                uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
                showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
                cardView: false,                    //是否显示详细视图
                detailView: false,                   //是否显示父子表
                singleSelect: true, //开启单选,想要获取被选中的行数据必须要有该参数
                columns: [{
                    checkbox: true
                }, {
                    field: 't_name',
                    title: '部门名称'
                }, {
                    field: 't_sex',
                    title: '性别'
                }, {
                    field: 'Level',
                    title: '部门级别'
                }, {
                    field: 'Desc',
                    title: '操作',
                    formatter: actionFormatter,
                    /*formatter:function () {

                        return '<button type="button" class="btn btn-primary" onclick="modify()">修改</button>' +
                            '<button type="button" class="btn btn-danger">删除</button>';
                    }*/

                }, ]/*,
                success : function(data) {
                    console.log(data.message);
                }*/
            });
        };

        //得到查询的参数
        oTableInit.queryParams = function (params) {
            var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                limit: params.limit,   //页面大小
                offset: params.offset,  //页码
                order: params.order,
                ordername: params.sort,
                departmentname: $("#txt_search_departmentname").val(),
                statu: $("#txt_search_statu").val()
            };
            return temp;
        };
        return oTableInit;
    };


    var ButtonInit = function () {
        var oInit = new Object();
        var postdata = {};

        oInit.Init = function () {
            //初始化页面上面的按钮事件
        };

        return oInit;
    };

    function modify() {

        var a= $('#tb_departments').bootstrapTable('getSelections');
        alert(a[0].id);
    }
    //操作栏的格式化,value代表当前单元格中的值，row代表当前行数据，index表示当前行的下标
    function actionFormatter(value, row, index) {
        var id = index;
        var result = "";
        result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"EditViewById('" + id + "', view='view')\" title='查看'><span class='glyphicon glyphicon-search'></span></a>";
        result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"EditViewById('" + JSON.stringify(row) + "','" + id + "')\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
        result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"EditViewById('" + row.project_name + "', '" + row.case_num + "','" + id + "')\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
        result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"edit()\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
        result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"DeleteByIds('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";
        return result;

    }

    // 怎么能把row作为参数传递到该函数，然后使用row.project_name的方法取出原有的值，再做修改
    function EditViewById(project_name,case_num, index){
        alert(project_name)
        alert(case_num)
        alert(index);
        //填充数据到模态框
        $("#submit_test_time").val(project_name);
        $("#case_num").val(case_num)
        //弹出模态框
        $('#myModal').modal('show')
    }

    function edit(){
        // 获取当前行
        var row=$("#mytab").bootstrapTable('getSelections');
        console.log(row);
        alert(row[0].project_name);
    }


    // 搜索查询按钮触发事件
    $(function() {
        $("#btn_query").click(function () {
            $('#tb_departments').bootstrapTable(('refresh')); // 很重要的一步，刷新url！
            $('#search-keyword').val('');
        })
    })

    // 选择时间日期
   /* $(function () {
        $('#datetimepicker2').datetimepicker({
            format: 'YYYY-MM-DD hh:mm',
            locale: moment.locale('zh-cn')
        });

    });*/

</script>
</html>
