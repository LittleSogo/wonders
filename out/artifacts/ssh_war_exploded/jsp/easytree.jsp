<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>tree demo</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css"href="<%=basePath%>js/jquery-easyui-1.4/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css"href="<%=basePath%>js/jquery-easyui-1.4/themes/icon.css">
	<script type="text/javascript"src="<%=basePath%>js/jquery-easyui-1.4/jquery.min.js"></script>
	<script type="text/javascript"src="<%=basePath%>js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
	<script type="text/javascript"src="<%=basePath%>js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
	

  </head>
  
  <body>
  	<div id="mydiv"  class="easyui-dialog" style="top:280px;
  	width:300px;height:180px;padding:10px;background:#fafafa;"  
  	 closed=true draggable =true>
  		<form action="">
  			<table>
  				<tr>
  					<td>名称</td>
  					<td><input id="name" type="text" name="name"/></td>
  				</tr>
  				<tr>
  					<td>地址</td>
  					<td><input id="url" type="text" name="url"/></td>
  				</tr>
  				<tr align="center">
  					<td colspan="2">
  						<div id="dlg-buttons">
							<a id="saveBtn" class="easyui-linkbutton">保存</a>
							<a id="cancelBtn"  class="easyui-linkbutton">关闭</a>
						</div>
  					</td>
  				</tr>
  			</table>
  		</form>
  	</div>
  	<ul id="tt"></ul>
  	
  	<div id="mm" class="easyui-menu" style="width:120px;height:300px;">
		<div onclick="create()">新建</div>
		<div onclick="deleteVal()">
			<span>删除</span>	
		</div>
		<div data-options="iconCls:'icon-save'">保存</div>
		<div data-options="iconCls:'icon-print',disabled:true">打印</div>
		<div class="menu-sep"></div>
		<div>退出</div>
	</div>
  	
  	<!-- <input type="button" onclick="getChild()" value="获取子节点"/>
  	<input type="button" onclick="getChecked()" value="获取选中的节点"/> -->
  </body>
  
  <script type="text/javascript">
  		$(function(){
			/*$('#mydiv').dialog({
				toolbar:[{
					text:'Add',
					iconCls:'icon-add',
					handler:function(){
						alert('add')
					}
				},'-',{
					text:'Save',
					iconCls:'icon-save',
					handler:function(){
						alert('save')
					}
				}],
				
			});*/
			$(document).bind('contextmenu',function(e){
				e.preventDefault();
				$('#mm').menu('show', {
					left: e.pageX,
					top: e.pageY
				});
			});
		});
		//保存
		$("#saveBtn").click(function(){
		//步骤：1前台更新
			//选取选中的节点
			var parent = $("#tt").tree('getSelected');
			$("#tt").tree('append',
				{
					parent:parent.target,
					data:[{
						text:$("#name").val(),
						attributes:{
							url:$("#url").val()
						}
						
					}]
				}
			);
			//此处需要刷新父节点确保 parentId不为空
			
			//步骤2 后台更新
			 $.ajax({	
	             type: "POST",
	             url: "<%=basePath%>save.do",
	             data: {
	             	parentid:parent.id,
	             	name:$("#name").val(),
	             	url:$("#url").val()
	             	 },
	             dataType: "json",
	             success: function(json){
					 console.log(json+"====>>json<<===");
					 /*var result = eval("(" + json + ")");
					 $.each(result, function(i,dom) {
                        
                        alert(dom.msg+"==><=="+i);
                    });*/
	             	//此处需要整理
	                 $.messager.show({
	                     title:'提示信息',
	                     msg:'保存成功！！'
	                 });
					 
	             },
	             error:function(){
	             	$.messager.show({
	                     title:'提示信息',
	                     msg:'保存失败...'
	                 });
	             }
         	});
         	//关闭当前输入页面
			$("#mydiv").dialog('close');
		});
		
		//删除数据
		function deleteVal(){
			var node = $('#tt').tree('getSelected');
			//console.log(node);
			//alert(node.id);
			$('#tt').tree('remove', node.target);
			$.ajax({	
	             type: "POST",
	             url: "<%=basePath%>delete.do",
	             data: {
	             	id:parent.id,	    
	             	 },
	             dataType: "json",
	             success: function(json){
	             	//此处需要整理
	                 $.messager.show({
	                     title:'提示信息',
	                     msg:'保存成功！！'
	                 });
					 
	             },
	             error:function(){
	             	$.messager.show({
	                     title:'提示信息',
	                     msg:'保存失败...'
	                 });
	             }
         	});
			
		}
		
		//取消
		$("#cancelBtn").click(function(){
			$("#mydiv").dialog('close');
		});
  
  		$("#tt").tree({
  			//url:'easyuiDemo/001_tree.json',
  			url:'<%=basePath%>getAllData',
  			checkbox:true,
  			animate:true,
  			onContextMenu: function(e,row){
  				e.preventDefault();
				$(this).tree('select', row.target);
  				$('#mm').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
  			}
  			
  		});
  		
  		function getChild(){
	  		var root = $("#tt").tree('getRoot');
	  		//alert(root.target);
	  		$("#tt").tree('getChildren',root.target);
  			//console.log($("#tt").tree('getChildren',root.target));
  		}
  		
  		function getChecked(){
  			console.log($("#tt").tree('getChecked'));
  			var checkList = $("#tt").tree('getChecked');
  			
  			alert($("#tt").tree('getChecked')[0].text);
  		}
  		//新增
  		function create(){
  			$("#mydiv").dialog('setTitle','新增页面');
  			$("#mydiv").dialog('open');
  		}
  </script>
  
</html>
