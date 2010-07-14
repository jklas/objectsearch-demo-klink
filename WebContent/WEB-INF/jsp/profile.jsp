<%@ page session="false"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">		
		<title>Welcome to Klink!</title>
	</head>
	
	<body>
		<div>
		<jsp:include page="topBar.jsp"/>
		</div>
		
		<c:choose>
			<c:when test="person == null">
				<div class="message">
					Person not found
				</div>
			</c:when>
			<c:otherwise>
				<div class="personDescription">
					This is the info you've entered so far:
					<ul>
						<li>First Name: <c:out value="${person.firstName}" /></li>
						<li>Last Name: <c:out value="${person.lastName}" /></li>
						<li>Email: <c:out value="${person.email}" /></li>
						<li>Company Name: <c:out value="${person.company.name}" /></li>
						<li>Company Country: <c:out value="${person.company.country}" /></li>
					</ul>			
				</div>
			</c:otherwise>
		</c:choose>
		
	</body>

	<link rel="stylesheet" type="text/css" href="styles/base.css" />
</html>