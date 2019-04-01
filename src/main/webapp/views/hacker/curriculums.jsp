<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('HACKER')">
	
	<display:table pagesize="5" name="curriculums" id="row" class="displaytag" 
					requestURI="/curriculum/hacker/list.do">
			
		<display:column property="title" titleKey="curriculum.title" /> 
		
		<display:column titleKey="curriculum.action">
			<spring:url var="currUrl" value="/curriculum/hacker/show.do">
				<spring:param name="curriculumId" value="${row.id}"/>
			</spring:url>
			<a href="${currUrl}">
				<spring:message code="curriculum.show" var="show" />
				<jstl:out value="${show}"/>
			</a>
		</display:column> 
	
	</display:table>

</security:authorize>
