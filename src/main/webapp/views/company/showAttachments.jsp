<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<display:table pagesize="5" name="${attachments}" id="attachment"
	requestURI="${requestURI}">
	<display:column titleKey="problem.attachment">
		<a href="${attachment.trim()}" target="_blank"><spring:message code="problem.attachment" /> ${attachments.indexOf(attachment)+1}</a>
	</display:column>
	<jstl:if test="${canEdit}">
	<display:column titleKey="company.delete">
		<button type="button" onclick="javascript: relativeRedir('problem/company/deleteAttachment.do?problemId='+${problemId}+'&attachmentNumber='+${attachments.indexOf(attachment)})" >
			<spring:message code="company.delete" />
		</button>	
	</display:column>
	</jstl:if>
	
</display:table>

<jstl:if test="${canEdit}">
<spring:url var="createUrl" value="/problem/company/addAttachment.do?problemId=${problemId}"/>       	
    <a href="${createUrl}">
    	<spring:message var ="create" code="problem.addAttachment" />
    	<jstl:out value="${create}" /> 
    </a>
    	
<br/>
<br/>
</jstl:if>

 <security:authorize access="hasRole('COMPANY')">
	<input type="button"
		name="cancel"
		value="<spring:message code="problem.back"/>" onclick="javascript:relativeRedir('problem/company/list.do');" />

	</security:authorize>	
	
	<security:authorize access="hasAnyRole('ROLE_ANONYMOUS')">		
	<input type="button"
		name="cancel"
		value="<spring:message code="problem.back"/>" onclick="javascript:relativeRedir('anonymous/position/list.do');" />
	</security:authorize>																


