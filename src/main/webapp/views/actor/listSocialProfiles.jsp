<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<br/>
<security:authorize access="isAuthenticated()">

	<table>
		<tr>
			<td><spring:message code="actor.fullName" /></td>
			<td><jstl:out
					value="${actor.name} ${actor.surname}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.VATNumber" /></td>
			<td><jstl:out value="${actor.VATNumber}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.photo" /></td>
			<td><jstl:out value="${actor.photo}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.email" /></td>
			<td><jstl:out value="${actor.email}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.phone" /></td>
			<td><jstl:out value="${actor.phone}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.address" /></td>
			<td><jstl:out value="${actor.address}" /></td>
		</tr>



	</table>

	<security:authorize access="hasAnyRole('ADMIN')">
		<acme:cancel url="/administrator/export.do?id=${actor.id}"
			code="export" />
	</security:authorize>

	<security:authorize access="hasAnyRole('HACKER')">
		<acme:cancel url="/hacker/export.do?id=${actor.id}"
			code="export" />
	</security:authorize>

	<security:authorize access="hasAnyRole('COMPANY')">
		<acme:cancel url="/company/export.do?id=${actor.id}"
			code="export" />
	</security:authorize>

	<h2>
		<spring:message code="socialProfile.mySocialProfiles" />
	</h2>

	<display:table pagesize="5" name="socialProfiles" id="socialProfile"
		requestURI="${requestURI}">

		<display:column property="nick" titleKey="socialProfile.nick" />

		<display:column property="name" titleKey="socialProfile.name" />
	

		<display:column property="profileLink"
			titleKey="socialProfile.profileLink" />

		<display:column>

			<a
				href="authenticated/socialProfile/edit.do?socialProfileId=${socialProfile.id}">
				<spring:message code="socialProfile.edit" />
			</a>

		</display:column>
		
	</display:table>

	<a href="authenticated/socialProfile/create.do"><spring:message
			code="socialProfile.create" /></a>


</security:authorize>