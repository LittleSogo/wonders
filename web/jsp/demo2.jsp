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
<div>
    <div>
		<div class="col-*-12">
			<table id="table"></table>
			<div id="toolbar">
				<div class="btn btn-primary" data-toggle="modal" data-target="#addModal">添加记录</div>
				<button id="btn_add" type="button" class="btn btn-default" οnclick="addVideoShow();">
					<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
				</button>
				<button id="btn_delete" type="button" class="btn btn-default" οnclick="batchUploadShow();">
					<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>批量上传
				</button>
				<button id="btn_edit" type="button" class="btn btn-default" οnclick="editMemberInfoShow();">
					<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>选择修改
				</button>
				<button id="btn_delete" type="button" class="btn btn-default" οnclick="delMemberVideo();">
					<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>选择删除
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
		</div>
	</div>
</div>

</body>
<script type="text/javascript">
    $(function () {
        $("#table").bootstrapTable({ // 对应table标签的id
            url: "<%=request.getContextPath()%>/test", // 获取表格数据的url
            cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
            striped: true,  //表格显示条纹，默认为false
            pagination: true, // 在表格底部显示分页组件，默认false
            pageList: [5,10,20,50,100], // 设置页面可以显示的数据条数
            pageSize: 10, // 页面数据条数
            pageNumber: 1, // 首页页码
            search: true,//是否搜索
            sidePagination: 'server', // 设置为服务器端分页
			toolbar: "#toolbar",//指定工具栏
            queryParams: function (params) { // 请求服务器数据时发送的参数，可以在这里添加额外的查询参数，返回false则终止请求

                return {
                    pageSize: params.limit, // 每页要显示的数据条数
                    offset: params.offset, // 每页显示数据的开始行号
                    sort: params.sort, // 要排序的字段
                    sortOrder: params.order, // 排序规则
                    dataId: $("#dataId").val() // 额外添加的参数
                }
            },
            sortName: 'id', // 要排序的字段
            sortOrder: 'desc', // 排序规则
            columns: [
                {
                    checkbox: true, // 显示一个勾选框
                    align: 'center' // 居中显示
                }, {
                    field: 'tSex', // 返回json数据中的name
                    title: '编号', // 表格表头显示文字
                    align: 'center', // 左右居中
                    valign: 'middle' // 上下居中
                }, {
                    field: 'tName',
                    title: '名称',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'calcMode',
                    title: '计算方式',
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index){ // 单元格格式化函数
                        var text = '-';
                        if (value == 1) {
                            text = "方式一";
                        } else if (value == 2) {
                            text = "方式二";
                        } else if (value == 3) {
                            text = "方式三";
                        } else if (value == 4) {
                            text = "方式四";
                        }
                        return text;
                    }
                }, {
                    title: "操作",
                    align: 'center',
                    valign: 'middle',
                    width: 200, // 定义列的宽度，单位为像素px
                    formatter: function (value, row, index) {
                        return 	'<button class="btn btn-success" οnclick="modifyUserData(this)">修改</button>' +
                        '<button class="btn btn-danger"οnclick="deleteUserData(this)">删除</button>' +
                        '<button class="btn btn-warning"οnclick="resetPassword(this)">密码重置</button>';}
                }
            ],
            onLoadSuccess: function(){  //加载成功时执行
                console.info("加载成功");
            },
            onLoadError: function(){  //加载失败时执行
                console.info("加载数据失败");
            }

        })
    });



</script>
</html>
