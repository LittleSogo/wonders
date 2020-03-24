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
    <title>bootstrap文件上传</title>
    <link rel="stylesheet" href="../bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="../bootstrap/css/fileinput.min.css">
    <link rel="stylesheet" href="../bootstrap/css/bootstrap-table.css">
    <script src="../js/jquery-2.1.1.min.js"></script>
    <script src="../bootstrap/js/bootstrap.js"></script>
    <script src="../bootstrap/js/bootstrap-table.js"></script>
	<script src="../bootstrap/js/fileinput.min.js"/>
    <script src="../bootstrap/js/bootstrap-table-zh-CN.js"></script>

</head>
<body>
	<form>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog modal-lg" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="myModalLabel">请选择Excel文件</h4>
					</div>
					<div class="modal-body">
						<a href="~/Data/ExcelTemplate/Order.xlsx" class="form-control" style="border:none;">下载导入模板</a>
						<input type="file" name="txt_file" id="txt_file" multiple class="file-loading" />
					</div></div>
			</div>
		</div>
	</form>
<body>
    
</body>
<script type="text/javascript">

var basePath ="<%=request.getContextPath()%>";

$(function () {
	//0.初始化fileinput
	var oFileInput = new FileInput();
	oFileInput.Init("txt_file", "/api/OrderApi/ImportOrder");

});

//初始化fileinput
var FileInput = function () {
	var oFile = new Object();

	//初始化fileinput控件（第一次初始化）
	oFile.Init = function(ctrlName, uploadUrl) {
		var control = $('#' + ctrlName);

		//初始化上传控件的样式
		control.fileinput({
			language: 'zh', //设置语言
			uploadUrl: uploadUrl, //上传的地址
			allowedFileExtensions: ['jpg', 'gif', 'png'],//接收的文件后缀
			showUpload: true, //是否显示上传按钮
			showCaption: false,//是否显示标题
			browseClass: "btn btn-primary", //按钮样式
			//dropZoneEnabled: false,//是否显示拖拽区域
			//minImageWidth: 50, //图片的最小宽度
			//minImageHeight: 50,//图片的最小高度
			//maxImageWidth: 1000,//图片的最大宽度
			//maxImageHeight: 1000,//图片的最大高度
			//maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
			//minFileCount: 0,
			maxFileCount: 10, //表示允许同时上传的最大文件个数
			enctype: 'multipart/form-data',
			validateInitialCount:true,
			previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
			msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
		});

		//导入文件上传完成之后的事件
		$("#txt_file").on("fileuploaded", function (event, data, previewId, index) {
			$("#myModal").modal("hide");
			var data = data.response.lstOrderImport;
			if (data == undefined) {
				toastr.error('文件格式类型不正确');
				return;
			}
			//1.初始化表格
			var oTable = new TableInit();
			oTable.Init(data);
			$("#div_startimport").show();
		});
	}
	return oFile;
};

	//点击效果
	$("#btn_upload").onclick=function () {
		alert("bb");
	}

</script>
</html>
