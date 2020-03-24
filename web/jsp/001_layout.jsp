<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
  <head>
  		<!-- 使用easyUI只需要引入的文件 -->
		<link rel="stylesheet" type="text/css"href="../js/jquery-easyui-1.4/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css"href="../js/jquery-easyui-1.4/themes/icon.css">
		<script type="text/javascript"src="../js/jquery-easyui-1.4/jquery.min.js"></script>
		<script type="text/javascript"src="../js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
		<script type="text/javascript"src="../js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>

		<script type="text/javascript">
		
			$(function() {
				
				$('a[title]').click(function(){
					var src = $(this).attr('title');
					var title = $(this).html();
					
					/*判断是否已经存在有相同title的tabs,如果没有就新增一个，否则就选中 */
					if($('#tt').tabs('exists',title)){
						$('#tt').tabs('select',title)
					}else{
						$('#tt').tabs('add', {
							title : title,
							content : '<iframe frameborder=0 style=width:100%;height:100% src='+src+'></iframe>',
							closable : true
						});
					}
					
					
					
					
				});
				$("#tt").tree({
		  			//url:'easyuiDemo/001_tree.json',
		  			url:'<%=basePath%>getAllData.do',
		  			checkbox:true,
		  			animate:true/*,
		  			onContextMenu: function(e,row){
		  				e.preventDefault();
						$(this).tree('select', row.target);
		  				$('#mm').menu('show', {
								left: e.pageX,
								top: e.pageY
							});
		  			}*/
		  			
		  		});
				
				
				
				

			});
			
			 $('#cc').calendar({   
			      width:600,   
			      height:600,   
			      current:new Date()   
			  }); 
			
		</script>


	</head>
  
  <body>
  <!-- fit 自适应父页面宽度，在子页面中设置 -->
		<div class="easyui-layout" id="cc" fit=true style="width: 600px; height: 400px;">
			<div region="north" title="easyui-layout" split="true" style="height: 100px;">
				
			</div>
			<div region="west" split="true" title="用户管理" style="width: 160px;">
				<div id="aa" fit=true class="easyui-accordion"style="width: 300px; height: 200px;">
					<!--  <div title="用户列表">
						<a title="003_form.jsp" >用户列表</a><br/><br/>
						<a title="001_dialog.jsp" >用户权限</a>
					</div>-->
					<ul id="tt"></ul>
					
					<div title="权限列表">
					</div>
					<div title="资源列表">
					</div>
				</div>

			</div>
			<div region="center"  title="主界面"style="padding: 1px; background:write;" >
				<div id="tt"  fit=true class="easyui-tabs"style="width: 500px; height: 250px;">
					
				</div> 
			</div>
		</div>
		

		<script type="text/javascript">
			
			/*$(function(){
				$.messager.alert('提示信息','The warning message'); 
			});*/
			
			
			
		</script>
		
		
		
		
		
		
		
		
		
		
		
		
	</body>
</html>
