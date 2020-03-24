<%--
  Created by IntelliJ IDEA.
  User: zj
  Date: 2020/3/19
  Time: 15:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getContextPath();
%>
<html>
<head>
    <title>easyui管理界面</title>

    <style>
        .logo {
            width: 180px;
            height: 50px;
            line-height: 50px;
            text-align: center;
            font-size: 20px;
            font-weight: bold;
            float: left;
            color: #fff;
        }

        .logout {
            float: right;
            padding: 30px 15px 0 0;
            color: #fff;
        }

        .logout a {
            color: #fff;
            text-decoration: none;
        }

        .logout a:HOVER {
            text-decoration: underline;
        }

    </style>

    <link rel="stylesheet" href="../js/jquery-easyui-1.4/themes/default/easyui.css">
    <link rel="stylesheet" href="../js/jquery-easyui-1.4/themes/icon.css">

    <script type="text/javascript" src="../js/jquery-2.1.1.min.js"></script>

    <script type="text/javascript" src="../js/jquery-easyui-1.4/jquery.easyui.min.js"></script>

    <script type="text/javascript" src="../js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>

</head>
<body class="easyui-layout">
<!-- 头部开始-->
<div data-options="region:'north',title:'头部',split:true,noheader:true" style="height: 60px; background: #666">
    <div class="logo">后台管理</div>
    <div class="logout">
        您好，administrator|<a href="">退出</a>
    </div>
</div>
<!-- End头部 -->

<!-- 底部开始 -->
<div data-options="region:'south',title:'底部',split:true,noheader:true" style="height: 35px; line-height: 30px; text-align: center;">黄宝康版权所有!TEL:18679758769</div>
<!-- End底部 -->

<!-- 导航开始 -->
<div data-options="region:'west',title:'导航',split:true,iconCls:'icon-large-shapes'" style="width: 180px; padding: 10px">
    <ul id="nav" class="easyui-tree"></ul>
</div>
<!-- End导航 -->

<!-- 内容区域开始 -->
<div data-options="region:'center'" style="overflow: hidden;">
    <div id="tabs">
        <div title="首页" iconCls="icon-help" style="padding: 0 10px; display: block;">
            <p>欢迎来到赣州智慧党务管理平台</p>
        </div>
    </div>
</div>
<!-- End内容区域 -->

<!-- 右键菜单 -->
<div id="mm" class="easyui-menu" style="width:150px;display: none">
    <div id="mm-tabupdate">刷新</div>
    <div class="menu-sep"></div>
    <div id="mm-tabclose">关闭</div>
    <div id="mm-tabcloseall">全部关闭</div>
    <div id="mm-tabcloseother">除此之外全部关闭</div>
    <div class="menu-sep"></div>
    <div id="mm-tabcloseleft">当前页左侧全部关闭</div>
    <div id="mm-tabcloseright">当前页右侧全部关闭</div>
</div>

</body>
<script type="text/javascript">
    $(function() {

        // 首页tab显示
        $('#tabs').tabs({
            fit : true,
            border : false,
        });

        // 导航树加载
        $('#nav').tree({
            url : 'nav.json',
            lines : true,
            onClick : function(node) {
                if (node.url) {
                    if ($('#tabs').tabs('exists', node.text)) {
                        $('#tabs').tabs('select', node.text);
                    } else {
                        $('#tabs').tabs('add', {
                            title : node.text,
                            iconCls : node.iconCls,
                            content : '<iframe scrolling="auto" frameborder="0"  src="'+node.url+'" style="width:100%;height:100%;"></iframe>',
                            closable : true,
                        });
                    }
                }
            }
        });

        // 绑定右键菜单
        $('#tabs').tabs({
            'onContextMenu' : function(e, title, index) {
                // 阻止默认
                e.preventDefault();
                // 显示菜单mm
                if (index > 0) {
                    $('#mm').menu('show', {
                        left : e.pageX,
                        top : e.pageY
                    }).data("tabIndex", index);// 把当前索引放进去
                    $('#tabs').tabs('select', index);// 选中当前tab
                }
            }
        });

        // 关闭当前
        $('#mm-tabclose').click(function() {
            $('#tabs').tabs('close', $('#mm').data("tabIndex"));// 关闭当前tab页
        });

        // 全部关闭
        $('#mm-tabcloseall').click(function() {
            // $('#tabs').tabs('tabs') 返回所有panel，本想用这个方法，需要查找相应节点
            $('.tabs-inner span').each(function(i, n) {
                var t = $(n).text();
                if (t !== '首页') {
                    $('#tabs').tabs('close', t);
                }
            });
        });

        // 关闭除当前之外的TAB
        $('#mm-tabcloseother').click(function() {
            $('#mm-tabcloseright').click();
            $('#mm-tabcloseleft').click();
        });

        // 关闭当前右侧的TAB
        $('#mm-tabcloseright').click(function() {
            var nextall = $('.tabs-selected').nextAll();
            if (nextall.length == 0) {
                return false;
            }
            nextall.each(function(i, n) {
                var t = $('a:eq(0) span', $(n)).text();
                if (t !== '首页') {
                    $('#tabs').tabs('close', t);
                }
            });
        });
        // 关闭当前左侧的TAB
        $('#mm-tabcloseleft').click(function() {
            var prevall = $('.tabs-selected').prevAll();
            if (prevall.length == 0) {
                return false;
            }
            prevall.each(function(i, n) {
                var t = $('a:eq(0) span', $(n)).text();
                if (t !== '首页') {
                    $('#tabs').tabs('close', t);
                }
            });
        });
    })



</script>
</html>
