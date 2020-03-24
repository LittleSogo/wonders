<%--
  Created by IntelliJ IDEA.
  User: zj
  Date: 2020/3/20
  Time: 11:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="common.jsp"%>
<html>
<head>
    <title>easyUI_datagrid</title>
    <%--    动态包含--%>
    <%--    <jsp:include page="common.jsp"/>--%>
</head>
<body>

<!--<h2>Basic DataGrid</h2>
<p>The DataGrid is created from markup, no JavaScript code needed.</p>
<div style="margin:20px 0;"></div>

<!--<table class="easyui-datagrid" title="Basic DataGrid" style="width:100%;height:100%"
       data-options="singleSelect:true,
                       collapsible:true,
                       url:'<%=request.getContextPath()%>/getAllData',
                       method:'get'">
    <thead>
    <tr>
        <th data-options="field:'itemid',width:80">Item ID</th>
        <th data-options="field:'text',width:100">text</th>
        <th data-options="field:'listprice',width:80,align:'right'">List Price</th>
        <th data-options="field:'unitcost',width:80,align:'right'">Unit Cost</th>
        <th data-options="field:'url',width:250">url</th>
        <th data-options="field:'status',width:60,align:'center'">Status</th>
    </tr>
    </thead>
</table>-->

<table id="dg"></table>


<div id="tb">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">编辑</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-help',plain:true">帮助</a>
</div>

</body>
<script type="text/javascript">
    //$(function () {
    $("#dg").datagrid({
        url:'<%=request.getContextPath()%>/getAllData',
        columns:[[
            {field:'code',title:'代码',width:100},
            {field:'text',title:'名称',width:222,formatter:function (value,row,index) {
                }},
            {field:'text',title:'价格',width:333,align:'right'}
        ]],
        collapsible:true,
        //toolbar:"#tb"
        toolbar: [{
            iconCls: 'icon-edit',
            handler: function(){alert('编辑按钮')}
        },'-',{
            iconCls: 'icon-help',
            handler: function(){alert('帮助按钮')}
        }],
        pagination:true
    });
    //})


</script>

</html>
