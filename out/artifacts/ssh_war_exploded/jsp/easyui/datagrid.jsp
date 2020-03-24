<%--
  Created by IntelliJ IDEA.
  User: zj
  Date: 2020/3/22
  Time: 11:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>easyui datagrid</title>

    <!-- 使用easyUI只需要引入的文件 -->
    <link rel="stylesheet" type="text/css"href="../../js/jquery-easyui-1.4/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"href="../../js/jquery-easyui-1.4/themes/icon.css">
    <script type="text/javascript"src="../../js/jquery-easyui-1.4/jquery.min.js"></script>
    <script type="text/javascript"src="../../js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
    <script type="text/javascript"src="../../js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>

</head>
<body>
<div class="easyui-panel">
    <form id="myform">
        <table>
            <tr>
                <td>用户名</td>
                <td><input type="text" class="easyui-textbox" name="name" value="用户名"></td>
                <td>密码</td>
                <td><input type="text" class="easyui-textbox" value="3333"></td>
                <td>验证码</td>
                <td><input type="text" class="easyui-textbox"></td>
                <td><input type="button" class="easyui-linkbutton" value="查询"> </td>
            </tr>

        </table>

    </form>


    <table id="dg"></table>
</div>
<div id="tb">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="showModel();"></a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-help',plain:true"></a>
</div>

<div id="p" class="easyui-panel" title="My Panel"
     style="width:500px;height:150px;padding:10px;background:#fafafa;"
     data-options="iconCls:'icon-save',closable:true,
        collapsible:true,minimizable:true,maximizable:true">
    <p>panel content.</p>
    <p>panel content.</p>
</div>

</body>
<script type="text/javascript">

    $(function () {
        $("#p").panel('close','forceClose=true');
        $('#dg').datagrid({
            title:'数据表格demo',
            url:'/ssh/getAllData',
            toolbar:'#tb',
            columns:[[
                {field:'code',title:'Code',width:100},
                {field:'text',title:'内容',width:280},
                {field:'text',title:'内容',width:300},
                {field:'text',title:'内容',width:300},
                {field:'url',title:'链接',width:240,align:'left',sortable:true},
                {field:'operate',title:'可视化分析',width:100,align:'left',
                    formatter:function(value, row, index){
                        var str = 	'<input type="button" class="easyui-linkbutton" style="color:blue" data-options="iconCls:\'icon-search\'" value="修改" onclick="forModify(\' '+row.text+' \')">'+
                            '<input type="button" class="easyui-linkbutton" style="color:red" value="删除" onclick="forModify(\' '+row.text+' \')">';
                        return str;
                    }}
            ]],
            pagination:true,
            striped:true,
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [5, 10, 25, 50, 100],
            queryParams:{
                str: $("#myform").serialize()
            }

        });



    })

    function forModify(val) {
        alert(val);
    }

    function showModel() {


    }

</script>
</html>
