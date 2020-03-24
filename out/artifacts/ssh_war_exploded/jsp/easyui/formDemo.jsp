<%--
  Created by IntelliJ IDEA.
  User: zj
  Date: 2020/3/21
  Time: 20:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>easyui form 表单</title>
    <script type="text/javascript" src="../../js/jquery-easyui-1.4/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="../../js/jquery-easyui-1.4/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="../../js/jquery-easyui-1.4/themes/icon.css">
    <script type="text/javascript" src="../../js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>

    <script type="text/javascript">
        $(function () {
            //自定义的验证方法
            $.extend($.fn.validatebox.defaults.rules, {
                minLength:{
                    validator:function (value,params) {
                        return value.length >= param[0];
                    },
                    message:'必填项 请补充完整！'
                }
            });

            $("#age").numberbox({
                min:0,
                max:180,
                required:true,
                missingMessage:'年龄为正数！'

            });

            $.messager.show({
                title:'提示信息',
                msg:'这是一个easyUI提示框'
            });


        });


        function formSubmit() {
            alert("bb");
        }




    </script>

</head>
<body>
<div class="easyui-panel" style="width: 400px;width: 400px" title="用户新增"
     closable="true" collapsible="true" minimizable="true" maximizable=true
     loadingMessage="数据加载中"
>
    <form id="myform" method="post" action="" >
        <table>
            <tr>
                <td>姓名</td>
                <td><input type="text" class="easyui-validatebox"  validType="minLength[3]" required=true  name="username"></td>
            </tr>
            <tr>
                <td>出生年月</td>
                <td><input type="text" class="easyui-datetimebox"></td>
            </tr>
            <tr>
                <td>性别</td>
                <td>男<input type="radio" name="sex" value="男">女<input type="radio" name="sex" value="女"></td>
            </tr>
            <tr>
                <td>年龄</td>
                <td><input type="text" id="age" name="age"></td>
            </tr>
            <tr>
                <td>城市</td>
                <td width="80px">
                    <select class="easyui-combobox" >
                        <option>北京</option>
                        <option>上海</option>
                        <option>广州</option>
                        <option>长沙</option>
                    </select>
                </td>
            </tr >
            <tr>
                <td colspan="2"><input type="button" class="easyui-linkbutton" value="提  交" onclick="formSubmit();"></td>
            </tr>
        </table>

    </form>
</div>



</body>
</html>
