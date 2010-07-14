<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
	
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to klink!</title>
</head>
<body>

<div style="color: white; padding: 40px;" class="pageText">
<h1>Welcome to klink!</h1>

<c:choose>
	<c:when test="${success == 'true'}" >
		<div style="width:40%">
			<c:out value="${message}"></c:out>
		</div>		
	</c:when>
	<c:when test="${success == 'false'}" >
		<div style="width:40%">
			<c:out value="${message}"></c:out>
		</div>
	</c:when>
	<c:otherwise>
		<h3>Please login here:</h3>
	</c:otherwise>	
</c:choose>

<br></br>

<form style="margin-left: 50px;" method="get" action="login.htm">
<table>
	<tbody>
		<tr>
			<td><input value="try" name="subaction" type="hidden"> <label>E-mail&nbsp;</label></td>
			<td><input name="mail" size="20" type="text"></td>
		</tr>
		<tr>
			<td><label>Password&nbsp;</label></td>
			<td><input name="password" size="20" type="password"></td>
		</tr>
		<tr style="margin-top: 50px;">
			<td colspan="2"><input value="Login"
				style="width: 55%; margin-left: 10%; margin-top: 10px; margin-bottom: 10px"
				type="submit"></td>
		</tr>
	</tbody>
</table>
</form>
<div style="margin-top: 10px;">First time here? <strong><a href="signup.htm" class="pageText" style="padding: 10px;">Sign Up</a></strong></div>
</div>

<link rel="stylesheet" type="text/css" href="styles/base.css">
</html>