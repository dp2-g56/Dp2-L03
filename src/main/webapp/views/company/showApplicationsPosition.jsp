<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<p><spring:message code="brotherhood.position.application.list" /></p>


	<display:table pagesize="5" name="allApplications" id="row" requestURI="${requestURI}" >


	
	<display:column property="creationMoment" titleKey="application.creationMoment" />
	
	<display:column property="link" titleKey="application.link" />	
	
	<display:column property="explication" titleKey="application.explication" />
	
	<display:column property="submitMoment" titleKey="application.submitMoment" />
	
	<display:column property="status" titleKey="application.status" />
	
	<display:column titleKey="application.problem"> 
		<jstl:out value="${row.problem.title}"/>
	</display:column>	
	
	<display:column titleKey="application.curriculum">
		<spring:message var="seeCurriculum" code="application.viewCurriculum" />
						
			<spring:url var="curriculumUrl" value="/position/company/curriculum/list.do?applicationId={applicationId}">
            	<spring:param name="applicationId" value="${row.id}"/>
        	</spring:url>
        	
        	<a href="${curriculumUrl}">
              <jstl:out value="${seeCurriculum}" />  
        	</a>
   	
	</display:column>
	
	<display:column titleKey="application.hacker">
		<jstl:out value="${row.hacker.userAccount.username}" />
	</display:column>
	
	
	
	<security:authorize access="hasRole('COMPANY')">
	
	
	<display:column>
	
		<jstl:if test="${row.status == 'SUBMITTED' && sameActorLogged}">
			<a href="position/company/application/accept.do?applicationId=${row.id}" onclick="return confirm('<spring:message code="company.confirmChangeStatus" />')">
				<spring:message code="application.accept" />
			</a>
			<br />
			<a href="position/company/application/reject.do?applicationId=${row.id}"  onclick="return confirm('<spring:message code="company.confirmChangeStatus" />')">
				<spring:message code="application.reject" />
			</a>
		</jstl:if>
	
	</display:column>
	
	</security:authorize>
	
	
	</display:table>
	
	
<br />

<security:authorize access="hasRole('COMPANY')">
<jstl:if test="${sameActorLogged}">
<a href="position/company/list.do"><spring:message code="position.back" /></a>
</jstl:if>
</security:authorize>




	
