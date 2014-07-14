<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Created Playlist</title>
</head>
<body>
	<h1>Created Playlists</h1>
	<p>The following rooms have been successfully created</p>
	<ol>
		<c:forEach items="${playlists}" var ="playlist">
		
		<c:out value="${playlist.name}"/><p>
 		</c:forEach>
	</ol>
</body>
</html>