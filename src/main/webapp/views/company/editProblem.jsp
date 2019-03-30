<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('COMPANY')"> 

	<form:form modelAttribute="problem" action="problem/company/edit.do">
    <!--Hidden Attributes -->
	<form:hidden path ="id"/>
	<form:hidden path ="version"/>


	<acme:textbox code="problem.title" path="title" />
	<br/>
	<acme:textarea code="problem.statement" path="statement" /> 
	<br/>
	<acme:textbox code="problem.hint" path="hint" />
	<br/>
	<acme:boolean code="problem.isDraftMode" trueCode="problem.true" falseCode="problem.false" path="isDraftMode"/>	
	
	<br/>
	
 	
 	<jstl:choose>
 		<jstl:when test="${problem.id != 0}">
 			<acme:submit name="edit" code="company.save" />  
 			<acme:submit name="delete" code="company.delete" />
 		</jstl:when> <jstl:otherwise>
 			<acme:submit name="save" code="company.save" />  	
 		</jstl:otherwise>
 	</jstl:choose>
 	
 	
 	
	</form:form>
	
	<acme:cancel url="/problem/company/list.do" code="company.cancel" />  
	
</security:authorize>