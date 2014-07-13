<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Create Room </title>
    
        <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap responsive -->
    <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
    <!-- Font awesome - iconic font with IE7 support --> 
    <link href="css/font-awesome.css" rel="stylesheet">
    <link href="css/font-awesome-ie7.css" rel="stylesheet">
    <!-- Bootbusiness theme -->
    <link href="css/boot-business.css" rel="stylesheet">
</head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User View</title>
<!-- <link type="text/css" href="resources/css/bootstrap.css" rel="stylesheet" /> -->
<link type="text/css" href="css/bootstrap-responsive.min.css"
	rel="stylesheet">
<link
	href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css"
	rel="stylesheet" />	

<h1>Create a new Playlist.</h1>
 		
<form:form method="post" action="/playlist/createlist.html"
        modelAttribute="uploadForm" enctype="multipart/form-data">
<br />
Playlist Name: <input type="text" name="name">
<br />
Playlist Genre: <input type="text" name="genre" />
<br />
<input type="submit" value="Submit" />

</form:form>

</body>
</html>