<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('HACKER')">

	<form:form action="curriculum/hacker/save.do" modelAttribute="formObject" >
	
		<fieldset>
		  <legend> <spring:message code="curriculum.data" /> </legend>
		  	<form:input path="id" hidden="true"/>
		  	
		  	<br />
		
			<acme:input code="curriculum.title" path="title"/>	
			
		</fieldset>
		
		<fieldset>
		  <legend> <spring:message code="personalData" /> </legend>
		  
		  	<br />
		  
			<acme:input code="personalData.fullName" path="fullName"/>
			<br />
			
			<acme:input code="personalData.statement" path="statement"/>
			<br />
			
			<acme:input code="personalData.phoneNumber" path="phoneNumber"/>	
			<br />
			
			<acme:input code="personalData.linkedinProfile" path="linkedInProfile"/>	
			<br />
			
			<acme:input code="personalData.gitHubProfile" path="gitHubProfile"/>	
			
		</fieldset>
	
		<br />
		
		<jstl:if test="${formObject.id>0}">
			<acme:submit code="curriculum.updateButton" name="save" />
		</jstl:if>
		<jstl:if test="${formObject.id==0}">
			<acme:submit code="curriculum.createButton" name="save" />
		</jstl:if>
		<acme:cancel url="/curriculum/hacker/list.do" code="curriculum.cancelButton" /> 
		
	</form:form> 

</security:authorize>
