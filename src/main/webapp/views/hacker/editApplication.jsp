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

	<form:form modelAttribute="application"
		action="application/hacker/edit.do">
		<!--Hidden Attributes -->
		<form:hidden path="id" />
		<form:hidden path="version" />

		<jstl:choose>
			<jstl:when test="${application.id == 0}">
				<acme:selectNotPred code="application.curriculum" path="curriculum"
					items="${curriculums}" itemLabel="title" />
				<br />
				<acme:selectNotPred code="application.position" path="position"
					items="${positions}" itemLabel="title" />
				<br />
			</jstl:when>
			<jstl:otherwise>
				<acme:textarea code="application.explication" path="explication" />
				<acme:textbox code="application.link" path="link" />
			</jstl:otherwise>
		</jstl:choose>


		<jstl:choose>
			<jstl:when test="${application.id != 0}">
				<acme:submit name="edit" code="hacker.edit" />
			</jstl:when>
			<jstl:otherwise>
				<acme:submit name="save" code="hacker.save" />
			</jstl:otherwise>
		</jstl:choose>

	</form:form>

	<acme:cancel url="/application/hacker/list.do" code="hacker.cancel" />

</security:authorize>