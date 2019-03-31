<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<p><spring:message code="brotherhood.position.problem.list" /></p>

<security:authorize access="hasRole('COMPANY')">
	
	<display:table pagesize="5" name="allProblems" id="row" requestURI="${requestURI}" >
	
	<display:column property="title" titleKey="problem.title" />
	
	<display:column property="statement" titleKey="problem.statement" />
	
	<display:column property="hint" titleKey="problem.hint" />
		
	<display:column titleKey="problem.attachments">
        <jstl:set var="attachmentsSize" value="${row.attachments.size()}" />
        <spring:url var="attachementsURL" value="/position/company/attachement/list.do?problemId={problemId}">      		
        	<spring:param name="problemId" value="${row.id}"/>	
        </spring:url>							
        <a href="${attachementsURL}">
              <spring:message var ="viewAttachements1" code="problem.viewAttachements" />
              <jstl:out value="${viewAttachements1}(${attachmentsSize})" />    
        </a>
    </display:column>
  						
</display:table>


		<br />
		<a href="position/company/list.do"><spring:message code="position.back" /></a>
	
</security:authorize>