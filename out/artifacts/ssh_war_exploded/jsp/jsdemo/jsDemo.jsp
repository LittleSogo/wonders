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
    <title>jueryDemo</title>
    <script src="../../js/jquery-2.1.1.min.js"></script>

</head>
<body>


<img src="111.png" alt="图片">
<input type="button" id="btn_hide" value="隐藏">
<input type="button" id="btn_show" value="显示">


</body>
<script type="text/javascript">

$(function () {$("#btn_hide").click(function () {
    $("img").hide(500);
});

})

</script>

</html>
