<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>klink! - Results for '<c:out value="${searchResults['search.query']}"/> '</title>
</head>
	<body>
		<jsp:include page="topBar.jsp"></jsp:include>
			
		<c:choose>
			<c:when	test="${not empty searchResults['results']}">
			<div class="pageText">
				I think that you might be looking for these people:
				<ul>
					<c:forEach var="rowData" items="${searchResults['results']}">
					<li class="searchResults">
					
					<c:choose>
						<c:when test="${!rowData.isContact}">
							<a href="addContact.htm?id=<c:out value="${rowData.result.id}" />">
								<img alt="Add Contact" src="images/add-friend.png" title="Add Contact" style="border:none;"/>
							</a>
						</c:when>
						<c:otherwise>
							<img alt="Contact" src="images/friend.png" title="This is already your contact"/>							
						</c:otherwise>
					</c:choose>
					
					<c:out value="${rowData.result.firstName}" />&nbsp;<c:out value="${rowData.result.lastName}" />
										
					(<fmt:formatNumber type="number" maxFractionDigits="4" groupingUsed="false" value="${rowData.score}" />)
					</li>	
					</c:forEach>
				</ul>
			</div>
			</c:when>
			<c:otherwise>
				<div class="pageText">
					Couldn't find any results for your query, please check your keywords
				</div>
			</c:otherwise>
		</c:choose>
	</body>
	
	<link rel="stylesheet" type="text/css" href="styles/base.css" />
</html>