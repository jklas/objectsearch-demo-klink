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
<a href="login.htm" style="text-decoration: none;"><h1 style="color:white;">Welcome to klink!</h1></a>

<h3>Sign up</h3>

<script type="text/javascript">
function validatePassword() {
	var password = document.getElementById("pass").value;
	var passwordConfirm = document.getElementById("pass_confirm").value;

	if(password == passwordConfirm) {
		return true;
	} else {
		alert('Passwords doesn\'t match');
		return false;
	}
}

</script>

<c:if test="${not empty success }" >
	<c:if test="${success == 'true'}" >
		<div>
			<c:out value="${message}"></c:out>
		</div>		
	</c:if>
	<c:if test="${success == 'false'}" >
		<div>
			<c:out value="${message}"></c:out>
		</div>
	</c:if>
</c:if>


<form style="margin-left: 50px;" method="post" action="signup.htm" onsubmit="return validatePassword()">
<input value="try" name="subaction" type="hidden">
<table>
	<tbody>
		<tr>
			<td><label>First Name&nbsp;</label></td>
			<td><input name="firstName" size="20" type="text"></td>
		</tr>
		<tr>
			<td><label>Last Name&nbsp;</label></td>
			<td><input name="lastName" size="20" type="text"></td>
		</tr>
		<tr>
			<td><label>E-mail&nbsp;</label></td>
			<td><input name="email" size="20" type="text"></td>
		</tr>
		<tr>
			<td><label>Company Name&nbsp;</label></td>
			<td><input name="companyName" size="20" type="text"></td>
		</tr>
		<tr>
			<td><label>Country Name&nbsp;</label></td>
			<td><input name="countryName" size="20" type="text"></td>
		</tr>
		<tr>
			<td><label>Enter Password&nbsp;</label></td>
			<td><input id="pass" name="password" size="20" type="password"></td>
		</tr>
		<tr>
			<td><label>Confirm Password&nbsp;</label></td>
			<td><input id="pass_confirm" size="20" type="password"></td>
		</tr>
		
		<tr style="margin-top: 50px;">
			<td colspan="2"><input value="Sign up"
				style="width: 55%; margin-left: 10%; margin-top: 10px; margin-bottom: 10px"
				type="submit"></td>
		</tr>
	</tbody>
</table>
</form>

<link rel="stylesheet" type="text/css" href="styles/base.css">
</html>