 <%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

	
	<display:table
	pagesize="5" name="problems" id="row"
	requestURI="${requestURI}" >

	<display:column  titleKey="problem.edit" >
		<jstl:if test="${row.isDraftMode}">
			<spring:url var="editUrl" value="/problem/company/edit.do?problemId={problemId}">
            	<spring:param name="problemId" value="${row.id}"/>
        	</spring:url>
        	
        	<a href="${editUrl}">
              <spring:message var ="edit" code="problem.edit" />
              <jstl:out value="${edit}" />  
        	</a>
        </jstl:if>
	</display:column>
	
	<display:column  property="title" titleKey="problem.title" />
	
	<display:column  property="statement" titleKey="problem.statement" />
	
	<display:column  property="hint" titleKey="problem.hint" />
    
    
    <display:column titleKey="problem.attachments">
    	
    		<jstl:set var="attachmentsSize" value="${row.attachments.size()}" />
    		
       		<spring:url var="problemUrl" value="/problem/company/listAttachments.do?problemId={problemId}">
            	<spring:param name="problemId" value="${row.id}"/>
        	</spring:url>
        	
        	<a href="${problemUrl}">
              <spring:message var ="viewAttachments" code="problem.viewAttachments" />
              <jstl:out value="${viewAttachments}(${attachmentsSize})" />  
        	</a>
        	
    	
    </display:column>
    
    <display:column titleKey="problem.mode" sortable= "true">
    	<jstl:choose>
			<jstl:when test="${row.isDraftMode}" >
				<spring:message code="problem.draftMode" />
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="problem.finalMode" />
			</jstl:otherwise>
		</jstl:choose>
	</display:column>
	
												
</display:table>
<br />
	<spring:url var="createUrl" value="/problem/company/create.do"/>       	
    <a href="${createUrl}">
    	<spring:message var ="create" code="problem.create" />
    	<jstl:out value="${create}" />  
    </a>