<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('HACKER')">

	<form action="miscellaneousData/hacker/saveAttachment.do" name="createAttachment" id="createAttachment" method="post">
	
		<input type="hidden" value="${miscellaneousDataId}" name="miscellaneousDataId" />
		
		<spring:message code="miscellaneousData.attachment" var="mess"/>
		<jstl:out value="${mess}"/>
		<input type="text" name="attachment" value="${attachment}" required />
	
		<input type="submit" name="save" value="<spring:message code="curriculum.createButton" />"/> 

		<acme:cancel url="/miscellaneousData/hacker/listAttachments.do?miscellaneousDataId=${miscellaneousDataId}" code="curriculum.cancelButton" /> 
		
	</form> 

</security:authorize>
