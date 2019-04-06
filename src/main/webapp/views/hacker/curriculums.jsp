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
			<spring:url var="currEditUrl" value="/curriculum/hacker/edit.do">
				<spring:param name="curriculumId" value="${row.id}"/>
			</spring:url>
			<spring:url var="currDeleteUrl" value="/curriculum/hacker/delete.do">
				<spring:param name="curriculumId" value="${row.id}"/>
			</spring:url>
			
			<spring:message code="curriculum.show" var="show" />
			<spring:message code="curriculum.edit"	var="edit"/>
			<spring:message code="curriculum.delete" var="delete" />
			
			<a href="${currUrl}"><jstl:out value="${show}"/></a> / <a href="${currEditUrl}"><jstl:out value="${edit}"/></a> / <a href="${currDeleteUrl}" onclick="return confirm('<spring:message code="curriculum.delete.confirmation" />')"><jstl:out value="${delete}"/></a>
		</display:column>
	
	</display:table>
	
	<spring:url var="newCurrUrl" value="/curriculum/hacker/new.do"/>
	<p><a href="${newCurrUrl}">
		<spring:message code="curriculum.new" var="newCurriculum" />
		<jstl:out value="${newCurriculum}"/>
	</a></p>
	
</security:authorize>
