<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('HACKER')">

	<form:form action="positionData/hacker/save.do" modelAttribute="positionData" >
	
		<form:input path="id" hidden="true"/>
		<form:input path="version" hidden="true"/>
		<input type="hidden" value="${curriculumId}" name="curriculumId"/>
		
		<acme:input code="positionData.title" path="title"/>
		<acme:input code="positionData.description" path="description"/>
		<acme:datebox code="positionData.startDate" path="startDate"/>
		
		<br/>
		
		<acme:datebox code="positionData.endDate" path="endDate"/>
		
		<br/><br/>
		
		<jstl:if test="${positionData.id>0}">
			<input type="submit" name="save" value="<spring:message code="curriculum.updateButton" />"/> 
		</jstl:if>
		<jstl:if test="${positionData.id==0}">
			<input type="submit" name="save" value="<spring:message code="curriculum.createButton" />"/> 
		</jstl:if>
		<acme:cancel url="/curriculum/hacker/show.do?curriculumId=${curriculumId}" code="curriculum.cancelButton" /> 
		
	</form:form> 

</security:authorize>
