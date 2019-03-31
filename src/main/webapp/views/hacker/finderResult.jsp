<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('HACKER')">

	<form:form action="finder/hacker/clean.do">
		<acme:submit name="save" code="finder.cleanFilter"/>
		
		<spring:url var="finderUrl" value="/finder/hacker/edit.do" />
		<a href="${finderUrl}">
			<button type="button" ><spring:message code="finder.edit" /></button>	
		</a>
	</form:form>
	
	</br>

	<display:table pagesize="5" name="positions" id="row" class="displaytag" 
					requestURI="/finder/hacker/list.do">
	
		<display:column property="ticker" titleKey="position.ticker" /> 
			
		<display:column property="title" titleKey="position.title" /> 
		
		<display:column property="description" titleKey="position.description" /> 
		
		<display:column property="deadline" titleKey="position.deadline" /> 
		
		<display:column property="requiredProfile" titleKey="position.requiredProfile" /> 
		
		<display:column property="requiredSkills" titleKey="position.requiredSkills" /> 
		
		<display:column property="requiredTecnologies" titleKey="position.requiredTecnologies" /> 
		
		<display:column property="offeredSalary" titleKey="position.offeredSalary" /> 
	
	</display:table>

</security:authorize>
