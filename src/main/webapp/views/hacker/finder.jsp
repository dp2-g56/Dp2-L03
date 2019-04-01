<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('HACKER')">

	<form:form action="finder/hacker/edit.do" modelAttribute="finder">
	
		<form:hidden path="id"/>
		<form:hidden path="version"/>

		<acme:textbox code="finder.keyWord" path="keyWord"/>
		
		<acme:datebox code="finder.deadLine" path="deadLine"/>
		
		<acme:textbox code="finder.minSalary" path="minSalary"/>

		<acme:datebox code="finder.maxDeadLine" path="maxDeadLine"/>

		<br/><br/> 
		
		<acme:submit name="save" code="hacker.save"/>
		<acme:cancel url="/finder/hacker/list.do" code="hacker.cancel"/>
	
	</form:form>

</security:authorize>
