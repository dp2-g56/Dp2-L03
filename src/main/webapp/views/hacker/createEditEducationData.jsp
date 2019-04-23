<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('HACKER')">

	<form:form action="educationData/hacker/save.do" modelAttribute="educationData" >
	
		<form:input path="id" hidden="true"/>
		<form:input path="version" hidden="true"/>
		<input type="hidden" value="${curriculumId}" name="curriculumId"/>
		
		<acme:input code="educationData.degree" path="degree"/>
		<acme:input code="educationData.institution" path="institution"/>
		
		<form:label path="mark">
			<spring:message code="educationData.mark" />
		</form:label>	
		<form:select path="mark">
			<form:option value="APLUSPLUS" label="A++" />
			<form:option value="APLUS" label="A+" />
			<form:option value="A" label="A" />
			<form:option value="B" label="B" />
			<form:option value="C" label="C" />
			<form:option value="F" label="F" />
			<form:option value="FMINUS" label="F-" />		
		</form:select>
		<form:errors path="mark" cssClass="error" />
		
		<br/>
		
		<acme:datebox code="educationData.startDate" path="startDate"/>
		
		<br/>
		
		<acme:datebox code="educationData.endDate" path="endDate"/>
		
		<br/><br/>
		
		<jstl:if test="${educationData.id>0}">
			<input type="submit" name="save" value="<spring:message code="curriculum.updateButton" />"/> 
		</jstl:if>
		<jstl:if test="${educationData.id==0}">
			<input type="submit" name="save" value="<spring:message code="curriculum.createButton" />"/> 
		</jstl:if>
		<acme:cancel url="/curriculum/hacker/show.do?curriculumId=${curriculumId}" code="curriculum.cancelButton" /> 
		
	</form:form> 

</security:authorize>
