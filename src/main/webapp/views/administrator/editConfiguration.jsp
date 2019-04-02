<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>		
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ADMIN')">

<form:form action="configuration/administrator/save.do" modelAttribute="configuration">
		<!-- Hidden Attributes -->
		<form:hidden path="id"/>
		<form:hidden path="version" />
		
		<acme:input code="configuration.finderResult" path="finderResult"/>
		<br/>
		<acme:input code="configuration.minFinderResults" path="minFinderResults"/>
		<br/>
		<acme:input code="configuration.maxFinderResults" path="maxFinderResults"/>
		<br/>
		<acme:input code="configuration.timeFinder" path="timeFinder"/>
		<br/>
		<acme:input code="configuration.minTimeFinder" path="minTimeFinder"/>
		<br/>
		<acme:input code="configuration.maxTimeFinder" path="maxTimeFinder"/>
		<br/>
		<acme:input code="configuration.spainTelephoneCode" path="spainTelephoneCode"/>
		<br/>
		<acme:input code="configuration.welcomeMessageEnglish" path="welcomeMessageEnglish"/>
		<br/>
		<acme:input code="configuration.welcomeMessageSpanish" path="welcomeMessageSpanish"/>
		<br/>
		<acme:input code="configuration.systemName" path="systemName"/>
		<br/>
		<acme:input code="configuration.imageURL" path="imageURL"/>
		<br/>
		<acme:input code="configuration.VAT" path="VAT"/>
		<br/>
		<acme:input code="configuration.fare" path="fare"/>
		<br/>
		<acme:input code="configuration.cardType" path="cardType"/>
		<br/>
		<acme:submit name="save" code="configuration.save.button"/>
		<br/>
		<acme:cancel url="/configuration/administrator/list.do" code="configuration.cancel.button"/>
		
</form:form>

</security:authorize>