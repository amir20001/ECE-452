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
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js"></script>
  <script type="text/javascript" src="/js/jquery.jplayer.min.js"></script>
  <script type="text/javascript">
    $(document).ready(function(){
      $("#jquery_jplayer_1").jPlayer({
        ready: function() {
          $(this).jPlayer("setMedia", {
            mp3: "http://www.jplayer.org/audio/mp3/Miaow-snip-Stirring-of-a-fool.mp3"
          }).jPlayer("play");
          var click = document.ontouchstart === undefined ? 'click' : 'touchstart';
          var kickoff = function () {
            $("#jquery_jplayer_1").jPlayer("play");
            document.documentElement.removeEventListener(click, kickoff, true);
          };
          document.documentElement.addEventListener(click, kickoff, true);
        },
        loop: true,
        swfPath: "/js"
      });
    });
  </script>
</head>
<body>
	<div id="jquery_jplayer_1"></div>
 	<div class="panel panel-default">
		<display:table class="table table-hover row-clickable"
			name="songs" id="song">
			<display:column property="id"/>
			<display:column property="title" />
			<display:column property="artist" />
			<display:column property="album" />
			<display:column property="duration" />
			<display:column property="uuid" />
			<display:column property="playlistId" />
			<display:column property="netScore" />			
			<display:column property="songUrl" href="{song.songUrl}"/>
			<display:column property="songUri"/>
			<display:column property="artUrl" title="art" href="{song.artUrl}"/>
		</display:table>
	</div>		
</body>
</html>