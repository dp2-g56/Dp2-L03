 <%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<p><spring:message code="company.position.list" /></p>


	
	
	
	<display:table
	pagesize="5" name="positions" id="row"
	requestURI="${requestURI}" >

	
 	<display:column property="ticker" titleKey="position.ticker" />
 	
	<display:column property="title" titleKey="position.title" />
	
	<display:column property="description" titleKey="position.description" />
	
	<display:column property="deadline" titleKey="position.deadline" />
	
	<display:column property="requiredProfile" titleKey="position.requiredProfile" />
	
	
	<display:column titleKey="position.requiredSkills">
    	
    		<jstl:set var="skillsSize" value="${row.requiredSkills.size()}" />
    		
       		<spring:url var="skillsUrl" value="/position/company/skill/list.do?positionId={positionId}">
            	<spring:param name="positionId" value="${row.id}"/>
        	</spring:url>
        	
        	<a href="${skillsUrl}">
              <spring:message var ="viewSkills1" code="position.viewSkills" />
              		<jstl:out value="${viewSkills1}(${skillsSize})" /> 
        	</a>
        	
    </display:column>
	
	<display:column titleKey="position.requiredTecnologies">
    	
    		<jstl:set var="tecnologiesSize" value="${row.requiredTecnologies.size()}" />
    		
       		<spring:url var="skillsUrl" value="/position/company/technology/list.do?positionId={positionId}">
            	<spring:param name="positionId" value="${row.id}"/>
        	</spring:url>
        	
        	<a href="${skillsUrl}">
              <spring:message var ="viewTecnologies1" code="position.viewTecnologies" />
              		<jstl:out value="${viewTecnologies1}(${tecnologiesSize})" /> 
        	</a>
        	
    </display:column>
	
	<display:column property="offeredSalary" titleKey="position.offeredSalary" />
	
	<display:column titleKey="position.status">
        <jstl:choose>
        	<jstl:when test="${row.isCancelled}">
        		<spring:message code="position.cancelled" />
        	</jstl:when>
        	
        	<jstl:otherwise>
        			<jstl:if test="${row.isDraftMode}">
        				<spring:message code="position.draft" />
        			</jstl:if>
        			<jstl:if test="${!row.isDraftMode}">
        				<spring:message code="position.final" />
        			</jstl:if>
        	</jstl:otherwise>
        	
        </jstl:choose>
    </display:column>
    
    
    <display:column titleKey="position.applications">
    
             <jstl:if test="${!row.isDraftMode}">
    	
    		
    		
       		<spring:url var="applicationsUrl" value="/position/company/application/list.do?positionId={positionId}">
            	<spring:param name="positionId" value="${row.id}"/>
        	</spring:url>
        	
        	<a href="${applicationsUrl}">
              <spring:message var ="viewApplications1" code="position.viewApplications" />
             <jstl:out value="${viewApplications1}" />   
        	</a>
        	
        	</jstl:if>
        	
    </display:column>
    
    <display:column titleKey="position.problems">
   

    		<jstl:set var="problemsSize" value="${row.problems.size()}" />
    		
       		<spring:url var="problemsUrl" value="/position/company/problem/list.do?positionId={positionId}">
            	<spring:param name="positionId" value="${row.id}"/>
        	</spring:url>
        	
        	<a href="${problemsUrl}">
              <spring:message var ="viewProblems1" code="position.viewProblems" />
             <jstl:out value="${viewProblems1}(${problemsSize})" />   
        	</a>

        	
    </display:column>
    
      <security:authorize access="hasRole('COMPANY')">

	<display:column>
		<jstl:if test="${row.isDraftMode && !row.isCancelled}">
			<a href="position/company/edit.do?positionId=${row.id}">
				<spring:message code="position.edit" />
			</a>
		</jstl:if>
		
		<jstl:if test="${!row.isDraftMode && !row.isCancelled}">
			<a href="position/company/cancel.do?positionId=${row.id}" onclick="return confirm('<spring:message code="position.verificationCancel" />')">
				<spring:message code="position.cancel" />
			</a>
		</jstl:if>

	</display:column>
	</security:authorize>
									
</display:table>

<security:authorize access="hasRole('COMPANY')">
<br />		
	<a href="position/company/create.do"><spring:message code="position.create" /></a>
	</security:authorize>

	
