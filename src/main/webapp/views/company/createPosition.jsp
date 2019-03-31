<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><spring:message code="position.create" /></p>



<form:form action="position/company/edit.do" modelAttribute="formObjectPositionProblemCheckbox" >

<fieldset>
  <legend> <spring:message code="position.data" /> </legend>
  	
  	<form:hidden path ="id"/>

	<acme:textbox code="position.title" path="title"/>	
	<br />
	
	<acme:textarea code="position.description" path="description"/>	
	<br />
		
	<acme:datebox code="position.deadline" path="deadline"/>	
	<br />  
	
	<acme:boolean code="position.isDraftMode" trueCode="position.true" falseCode="position.false" path="isDraftMode"/>	
	<br />
	
	<acme:textbox code="position.requiredProfile" path="requiredProfile"/>	
	<br />
	
	<acme:textarea code="position.requiredSkills" path="requiredSkills"/>	
	<br />
	
	<acme:textarea code="position.requiredTecnologies" path="requiredTecnologies"/>	
	<br />
	
	<acme:input code="position.offeredSalary" path="offeredSalary"/>	
	<br />
	
</fieldset>

<fieldset>
  <legend> <spring:message code="problem.data" /> </legend>
  
	<acme:checkbox  path="problems" map="${map}" />
	

	
</fieldset>
	<br />
	
	<acme:submit code="position.createButton" name="save" />
	<br />
	<jstl:if test="${positionId != 0 }">
 		<acme:submit name="delete" code="position.delete" />
 	</jstl:if> 
	<br />
</form:form> 
	
	<acme:cancel url="/position/company/list.do" code="position.cancel" /> 
	
	