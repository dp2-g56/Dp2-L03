<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('HACKER')">

	<jstl:if test="${res}">
		<script type="text/javascript">
			alert("<spring:message code="application.create.error"/>");
		</script>
	</jstl:if>

	<form name="filter" id="filter" action="application/hacker/filter.do"
		method="post">
		<label for="filter"><spring:message code="application.filter" /></label>
		<br /> <select name="fselect">
			<option value="ALL">-</option>
			<option value="PENDING"><spring:message
					code="application.status.pending" /></option>
			<option value="SUBMITTED"><spring:message
					code="application.status.submitted" /></option>
			<option value="ACCEPTED"><spring:message
					code="application.status.accepted" /></option>
			<option value="REJECTED"><spring:message
					code="application.status.rejected" /></option>
		</select> <input type="submit" name="refresh" id="refresh"
			value="<spring:message code ="application.filter.button"/>" />

	</form>

	<br />

	<display:table pagesize="5" name="applications" id="row"
		class="displaytag" requestURI="${requestURI}">

		<jstl:choose>
			<jstl:when test="${row.status.toString()=='ACCEPTED'}">
				<jstl:set var="color" value="green" />
			</jstl:when>

			<jstl:when test="${row.status.toString()=='REJECTED'}">
				<jstl:set var="color" value="red" />
			</jstl:when>

			<jstl:when test="${row.status.toString()=='PENDING'}">
				<jstl:set var="color" value="grey" />
			</jstl:when>

			<jstl:when test="${row.status.toString()=='SUBMITTED'}">
				<jstl:set var="color" value="orange" />
			</jstl:when>

			<jstl:otherwise>
				<jstl:set var="color" value="black" />
			</jstl:otherwise>
		</jstl:choose>

		<display:column titleKey="application.problemTitle"
			style="color:${color}">
			<jstl:out value="${row.problem.title}" />
		</display:column>

		<display:column titleKey="application.creationMoment"
			style="color:${color}">
			<jstl:out value="${row.creationMoment}" />
		</display:column>

		<display:column property="link" titleKey="request.link"
			style="color:${color}" />

		<display:column property="explication" titleKey="request.explication"
			style="color:${color}" />

		<display:column property="submitMoment"
			titleKey="request.submitMoment" style="color:${color}" />

		<display:column property="status" titleKey="request.status"
			style="color:${color}" />

		<display:column titleKey="application.position" style="color:${color}">
			<jstl:out value="${row.position.title}" />
		</display:column>

		<display:column titleKey="application.curriculum"
			style="color:${color}">
			<jstl:out value="${row.curriculum.title}" />
		</display:column>

		<display:column>
			<jstl:if test="${row.status.toString()=='PENDING'}">
				<spring:url var="editApplication"
					value="/application/hacker/edit.do">
					<spring:param name="applicationId" value="${row.id}" />
				</spring:url>
				<a href="${editApplication}"> <spring:message
						code="application.edit" /></a>
			</jstl:if>
		</display:column>

	</display:table>

	<acme:cancel code="application.create"
		url="/application/hacker/create.do" />

</security:authorize>