<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<p><spring:message code="mail.myMessages" /></p>

<security:authorize access="isAuthenticated()">


<display:table
	pagesize="4" name="messages" id="row"
	requestURI="message/actor/list.do" >
	
	<!-- Date -->
	<display:column	property ="moment"
					titleKey="mail.message.moment"/>
					
	<display:column	property ="sender" 	
					titleKey="mail.message.sender"/>

	<display:column	property ="receiver" 	
					titleKey="mail.message.receiver"/>
	
	<display:column property ="subject"	
					titleKey="mail.message.subject"/>
	
	<display:column	property ="tags"
					titleKey="mail.message.tags"/>
					
	<display:column	property ="body"
					titleKey="mail.message.body"/>
				
	<display:column>
		
			<spring:url var="deleteMessage" value="/message/actor/delete.do?rowId={rowId}">
				<spring:param name="rowId" value="${row.id}"/>
			</spring:url>
		
				<a href="${deleteMessage}" onclick="return confirm('<spring:message code="mail.delete" />')">
				 <spring:message code="message.delete"/>
				 </a>

		</display:column>	
	

															
</display:table>

<!-- Enlaces parte inferior -->
<spring:url var="newMessage" value="/message/actor/create.do"/>

<p><a href="${newMessage}"><spring:message code="mail.message.new" /></a></p>


</security:authorize>