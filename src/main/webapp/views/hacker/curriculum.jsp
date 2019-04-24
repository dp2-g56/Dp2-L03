<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

	<spring:message code="curriculum.title" var="title"/>
	<h3><jstl:out value="${title}: ${curriculum.title}"/></h3>
	
	<spring:message code="personalData"	var="perData"/>
	<h4><jstl:out value="${perData}"/></h4>
	
	<display:table name="personalData" id="row">
			
		<display:column property="fullName" titleKey="personalData.fullName" /> 
		<display:column property="statement" titleKey="personalData.statement" /> 
		<display:column property="phoneNumber" titleKey="personalData.phoneNumber" /> 
		<display:column property="gitHubProfile" titleKey="personalData.gitHubProfile" /> 
		<display:column property="linkedinProfile" titleKey="personalData.linkedinProfile" /> 
	
	</display:table>
	
	<spring:message code="positionData"	var="posData"/>
	<h4><jstl:out value="${posData}"/></h4>
	
	<display:table name="positionData" id="row" pagesize="5" class="displaytag" 
					requestURI="${requestURI}">
			
		<display:column property="title" titleKey="positionData.title" /> 
		<display:column property="description" titleKey="positionData.description" /> 
		<display:column property="startDate" titleKey="positionData.startDate" /> 
		<display:column property="endDate" titleKey="positionData.endDate" />
		<security:authorize access="hasRole('HACKER')">
			<display:column titleKey="positionData.action">
				<spring:url var="editPositionData" value="/positionData/hacker/edit.do">
					<spring:param name="positionDataId" value="${row.id}"/>
				</spring:url>
				<spring:url var="deletePositionData" value="/positionData/hacker/delete.do">
					<spring:param name="positionDataId" value="${row.id}"/>
				</spring:url>
				<a href="${editPositionData}">
					<spring:message code="positionData.edit" var="editPositionDataMessage" />
					<jstl:out value="${editPositionDataMessage}"/>
				</a> / 
				<a href="${deletePositionData}" onclick="return confirm('<spring:message code="positionData.delete.confirmation" />')">
					<spring:message code="positionData.delete" var="deletePositionDataMessage" />
					<jstl:out value="${deletePositionDataMessage}"/>
				</a>
			</display:column>
		</security:authorize>
	
	</display:table>
	
	<security:authorize access="hasRole('HACKER')">
		<spring:url var="newPositionData" value="/positionData/hacker/new.do">
			<spring:param name="curriculumId" value="${curriculum.id}"/>
		</spring:url>
		<p>
			<a href="${newPositionData}">
				<spring:message code="positionData.new" var="newPositionDataMessage" />
				<jstl:out value="${newPositionDataMessage}"/>
			</a>
		</p>
	</security:authorize>
	
	<spring:message code="educationData" var="eduData"/>
	<h4><jstl:out value="${eduData}"/></h4>
	
	<display:table name="educationData" id="row" pagesize="5" class="displaytag" 
					requestURI="${requestURI}">
			
		<display:column property="degree" titleKey="educationData.degree" /> 
		<display:column property="institution" titleKey="educationData.institution" /> 
		<display:column property="mark" titleKey="educationData.mark" /> 
		<display:column property="startDate" titleKey="educationData.startDate" /> 
		<display:column property="endDate" titleKey="educationData.endDate" />
		<security:authorize access="hasRole('HACKER')">
			<display:column titleKey="educationData.action">
				<spring:url var="editEducationData" value="/educationData/hacker/edit.do">
					<spring:param name="educationDataId" value="${row.id}"/>
				</spring:url>
				<spring:url var="deleteEducationData" value="/educationData/hacker/delete.do">
					<spring:param name="educationDataId" value="${row.id}"/>
				</spring:url>
				<a href="${editEducationData}">
					<spring:message code="educationData.edit" var="editEducationDataMessage" />
					<jstl:out value="${editEducationDataMessage}"/>
				</a> / 
				<a href="${deleteEducationData}" onclick="return confirm('<spring:message code="educationData.delete.confirmation" />')">
					<spring:message code="educationData.delete" var="deleteEducationDataMessage" />
					<jstl:out value="${deleteEducationDataMessage}"/>
				</a>
			</display:column>
		</security:authorize>
	
	</display:table>
	
	<security:authorize access="hasRole('HACKER')">
		<spring:url var="newEducationData" value="/educationData/hacker/new.do">
			<spring:param name="curriculumId" value="${curriculum.id}"/>
		</spring:url>
		<p>
			<a href="${newEducationData}">
				<spring:message code="educationData.new" var="newEducationDataMessage" />
				<jstl:out value="${newEducationDataMessage}"/>
			</a>
		</p>
	</security:authorize>
	
	<spring:message code="miscellaneousData" var="misData"/>
	<h4><jstl:out value="${misData}"/></h4>
	
	<display:table name="miscellaneousData" id="row" pagesize="5" class="displaytag" 
					requestURI="${requestURI}">
			
		<display:column property="freeText" titleKey="miscellaneousData.freeText" /> 
		<display:column titleKey="miscellaneousData.action">
			<spring:url var="attachments" value="/miscellaneousData/hacker/listAttachments.do">
				<spring:param name="miscellaneousDataId" value="${row.id}"/>
			</spring:url>
			<security:authorize access="hasRole('HACKER')">
				<spring:url var="editMiscellaneousData" value="/miscellaneousData/hacker/edit.do">
					<spring:param name="miscellaneousDataId" value="${row.id}"/>
				</spring:url>
				<spring:url var="deleteMiscellaneousData" value="/miscellaneousData/hacker/delete.do">
					<spring:param name="miscellaneousDataId" value="${row.id}"/>
				</spring:url>
			</security:authorize>
			<a href="${attachments}">
				<spring:message code="attachments.show" var="show" />
				<jstl:out value="${show} (${row.attachments.size()})"/>
			</a><security:authorize access="hasRole('HACKER')"> / 
				<a href="${editMiscellaneousData}">
					<spring:message code="miscellaneousData.edit" var="editMiscellaneousDataMessage" />
					<jstl:out value="${editMiscellaneousDataMessage}"/>
				</a> / 
				<a href="${deleteMiscellaneousData}" onclick="return confirm('<spring:message code="miscellaneousData.delete.confirmation" />')">
					<spring:message code="miscellaneousData.delete" var="deleteMiscellaneousDataMessage" />
					<jstl:out value="${deleteMiscellaneousDataMessage}"/>
				</a>
			</security:authorize>
		</display:column>
		
	</display:table>
	
	<security:authorize access="hasRole('HACKER')">
		<spring:url var="newMiscellaneousData" value="/miscellaneousData/hacker/new.do">
			<spring:param name="curriculumId" value="${curriculum.id}"/>
		</spring:url>
		<p>
			<a href="${newMiscellaneousData}">
				<spring:message code="miscellaneousData.new" var="newMiscellaneousDataMessage" />
				<jstl:out value="${newMiscellaneousDataMessage}"/>
			</a>
		</p>
	</security:authorize>
	
	<security:authorize access="hasRole('HACKER')">
		<acme:cancel url="/curriculum/hacker/list.do" code="hacker.back" /> 
	</security:authorize>
	
  	<security:authorize access="hasAnyRole('ROLE_ANONYMOUS')">
  		<a href="anonymous/position/list.do"><spring:message code="position.back" /></a>
  	</security:authorize>