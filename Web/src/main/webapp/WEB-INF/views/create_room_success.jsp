<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Created Room</title>
</head>
<body>
	<h1>Created Room</h1>
	<p>The following rooms have been succesfully created</p>
	<ol>
		<c:forEach items="${rooms}" var=roomDao.getAllRooms()>
			<li>${rooms}</li>
		</c:forEach>
	</ol>
</body>
</html>