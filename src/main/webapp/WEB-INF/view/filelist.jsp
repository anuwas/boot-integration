<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TestSuitFiles</title>
<style type="text/css">
.fileName{
float: left;
width: 400px;
margin-right: 30px;
}
</style>
<script type="text/javascript">
function replayFile(fileName){
	alert(fileName);
	
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	     //document.getElementById("demo").innerHTML = this.responseText;
		     alert("Played");
	    }
	  };
	  xhttp.open("GET", "files/"+fileName, true);
	  xhttp.send();
}
</script>
</head>
<body>
	<div>
		<div>
		<ul>
		<c:forEach items="${fileNames}" var="element"> 
  			<li><div class="fileName">${element}</div><div><a href="javascript:void(0)" onclick=" return replayFile('${element}')">Replay</a></div></li>
  		</c:forEach>
		</ul>		
		</div>
	</div>
</body>
</html>
