<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
 <meta charset="UTF-8">
 <title>Title</title>
</head>
<body>
<!-- 상대경로 사용, [현재 URL이 속한 계층 경로 + /save] -->
<!-- 현재 지금 jsp는 Controller를 타고 /servlet-mvc/members/new-form 으로 forwarding됨 -->
<!-- /save가 아닌 save만 사용하면, 가장 마지막 위치인 /new-form이 /save로 변함 -->

<form action="save" method="post">
 username: <input type="text" name="username" />
 age: <input type="text" name="age" />
 <button type="submit">전송</button>
</form>
</body>
</html>