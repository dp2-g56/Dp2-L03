<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('HACKER')">

	<spring:message code="curriculum.title" var="title"/>
	<h3><jstl:out value="${title}: ${curriculum.title}"/></h3>
	
	<spring:message code="personalData"	var="perData"/>
	<h4><jstl:out value="${perData}:"/></h4>
	
	<display:table name="personalData" id="row">
			
		<display:column property="fullName" titleKey="personalData.fullName" /> 
		<display:column property="statement" titleKey="personalData.statement" /> 
		<display:column property="phoneNumber" titleKey="personalData.phoneNumber" /> 
		<display:column property="gitHubProfile" titleKey="personalData.gitHubProfile" /> 
		<display:column property="linkedinProfile" titleKey="personalData.linkedinProfile" /> 
	
	</display:table>
	
	<spring:message code="positionData"	var="posData"/>
	<h4><jstl:out value="${posData}:"/></h4>
	
	<display:table name="positionData" id="row">
			
		<display:column property="title" titleKey="positionData.title" /> 
		<display:column property="description" titleKey="positionData.description" /> 
		<display:column property="startDate" titleKey="positionData.startDate" /> 
		<display:column property="endDate" titleKey="positionData.endDate" /> 
	
	</display:table>

</security:authorize>
