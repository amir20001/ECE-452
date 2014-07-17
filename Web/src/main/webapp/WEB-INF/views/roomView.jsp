<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>	
<%@ page import="com.ece452.dao.RoomDao" %>
<%@ page import="com.ece452.domain.Room" %>	
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Rooms View</title>
<!-- <link type="text/css" href="resources/css/bootstrap.css" rel="stylesheet" /> -->
<link type="text/css" href="css/bootstrap-responsive.min.css"
	rel="stylesheet">
<link
	href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css"
	rel="stylesheet" />
			<%!
			List<Room> rooms = (List<Room>)request.getAttribute("rooms");
			for(int i=0; i< rooms.size(); i++)
			{
 				int roomId= rooms.get(i).id();
 				int random=76;
			}
			%>

</head>
<body>

 	<div class="panel panel-default">
		<display:table class="table table-hover row-clickable"
			name="rooms">

			<display:column property="id" title="roomID"
			 url="/room/view/<%=roomId%>" />
			<display:column property="name" />
			<display:column property="ownerUserId" />
			<display:column property="listenerCount" />
			<display:column property="currentSongId" />
			<display:column property="playlistId" />
			<display:column property="songPosition" />
			<display:column property="songIsPlaying" />
			<display:column property="songIsPlaying" />			
			<display:column property="songPlayStartTime" />
		</display:table>
	</div>		
</body>
</html>