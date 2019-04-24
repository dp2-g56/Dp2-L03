<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

	<spring:message code="curriculum.title" var="title"/>
	<h3><jstl:out value="${title}: ${curriculum.title}"/></h3>
	
	<spring:message code="personalData"	var="perData"/>
	<h4><jstl:out value="${perData}"/></h4>
	
	<display:table name="personalData" id="row">
			
		<display:column property="fullName" titleKey="personalData.fullName" /> 
		<display:column property="statement" titleKey="personalData.statement" /> 
		<display:column property="phoneNumber" titleKey="personalData.phoneNumber" /> 
		<display:column property="gitHubProfile" titleKey="personalData.gitHubProfile" /> 
		<display:column property="linkedinProfile" titleKey="personalData.linkedinProfile" /> 
	
	</display:table>
	
	<spring:message code="positionData"	var="posData"/>
	<h4><jstl:out value="${posData}"/></h4>
	
	<display:table name="positionData" id="row" pagesize="5" class="displaytag" 
					requestURI="${requestURI}">
			
		<display:column property="title" titleKey="positionData.title" /> 
		<display:column property="description" titleKey="positionData.description" /> 
		<display:column property="startDate" titleKey="positionData.startDate" /> 
		<display:column property="endDate" titleKey="positionData.endDate" />
	
	</display:table>

	
	<spring:message code="educationData" var="eduData"/>
	<h4><jstl:out value="${eduData}"/></h4>
	
	<display:table name="educationData" id="row" pagesize="5" class="displaytag" 
					requestURI="${requestURI}">
			
		<display:column property="degree" titleKey="educationData.degree" /> 
		<display:column property="institution" titleKey="educationData.institution" /> 
		<display:column property="mark" titleKey="educationData.mark" /> 
		<display:column property="startDate" titleKey="educationData.startDate" /> 
		<display:column property="endDate" titleKey="educationData.endDate" />
	
	</display:table>
	
	<spring:message code="miscellaneousData" var="misData"/>
	<h4><jstl:out value="${misData}"/></h4>
	
	<display:table name="miscellaneousData" id="row" pagesize="5" class="displaytag" 
					requestURI="${requestURI}">
			
		<display:column property="freeText" titleKey="miscellaneousData.freeText" />
		
		<display:column property="attachments" titleKey="miscellaneousData.attachment" /> 
		
	</display:table>
	
	<br>
	<br>
  	<a href="anonymous/position/list.do"><spring:message code="position.backToPublicData" /></a>
