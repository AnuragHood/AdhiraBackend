<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body style ="background-color:#9cafec">
	<div style ="text-align:center; top:40%; position: relative">
	<p><b>${success}</b></p><br />
	<a href ="http://localhost:4200"><b>Login</b></a>
	<p><b>${failure}</b></p><br />
	<a href ="http://localhost:4200"><b>Register</b></a><br />
	<c:if test="${newPass}==required"></c:if>
	<form action="/action_page.php">
  New Password:<br>
  <input type="text" name="firstname" value="Mickey">
  <br>
  Confirm Password:<br>
  <input type="text" name="lastname" value="Mouse">
  <br><br>
  <input type="submit" value="Submit">
</form>
	</div>
</body>
</html>