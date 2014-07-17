<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Song View</title>
<!-- <link type="text/css" href="resources/css/bootstrap.css" rel="stylesheet" /> -->
<link type="text/css" href="css/bootstrap-responsive.min.css"
	rel="stylesheet">
<link
	href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css"
	rel="stylesheet" />

</head>
<body>

 	<div class="panel panel-default">
		<display:table class="table table-hover row-clickable"
			name="songs">
			<display:column property="id"/>
			<display:column property="title" />
			<display:column property="artist" />
			<display:column property="album" />
			<display:column property="duration" />
			<display:column property="uuid" />
			<display:column property="playlistId" />
			<display:column property="netScore" />			
			<display:column property="songUrl" href="{songUrl}"/>
			<display:column property="songUri" />
			<display:column property="artUrl" title="art" href="{artUrl}"/>
		</display:table>
	</div>		
</body>
</html>