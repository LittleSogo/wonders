<%--
  Created by IntelliJ IDEA.
  User: zj
  Date: 2020/3/19
  Time: 14:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理界面</title>
    <link rel="stylesheet" href="../bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="../bootstrap/css/bootstrap-table.css">
    <link href="https://cdn.bootcss.com/metisMenu/3.0.2/metisMenu.min.css" rel="stylesheet">
    <script src="../js/jquery-2.1.1.min.js"></script>
    <script src="../bootstrap/js/bootstrap.js"></script>
    <script src="../bootstrap/js/bootstrap-table.js"></script>
    <script src="../bootstrap/js/bootstrap-table-zh-CN.js"></script>
    <script src="https://cdn.bootcss.com/metisMenu/3.0.2/metisMenu.min.js"></script>
</head>
<body>
<nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
    <div class="navbar-header">
        <a class="navbar-brand" href="/pages/back/index.jsp"><i class="fa fa-graduation-cap fa-fw" aria-hidden="true" ></i>学生信息管理系统</a>
    </div>
    <ul class="nav navbar-top-links navbar-right">
        <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"> <i
                class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i> </a>
            <ul class="dropdown-menu dropdown-user">
                <li><a href="#"><i class="fa fa-user fa-fw"></i> 用户信息</a></li>
                <li><a href="#"><i class="fa fa-gear fa-fw"></i> 设置中心</a></li>
                <li class="divider"></li>
                <li><a href="login.jsp"><i class="fa fa-sign-out fa-fw"></i> 登出系统</a></li>
            </ul>
        </li>
    </ul>
    <div class="navbar-default sidebar" role="navigation">
        <div class="sidebar-nav navbar-collapse">
            <ul class="nav" id="side-menu">
                <li><a href="/pages/back/index.jsp"><i class="fa fa-dashboard fa-fw"></i>系统首页</a></li>
                <li><a href="#"><i class="fa fa-bar-chart-o fa-fw"></i> 班级管理<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li><a href="/pages/back/classes/classes_insert.jsp">增加班级</a></li>
                        <li><a href="/pages/back/classes/classes_list.action">班级列表</a></li>
                    </ul>
                </li>
                <li><a href="#"><i class="fa fa-edit fa-fw"></i> 学生管理<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li><a href="panels-wells.html">增加学生</a></li>
                        <li><a href="buttons.html">学生列表</a></li>
                    </ul>
                </li>
                <li>
                    <a href="#"><i class="fa fa-question-circle-o fa-fw" aria-hidden="true"></i>关于系统</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

</body>
</html>
