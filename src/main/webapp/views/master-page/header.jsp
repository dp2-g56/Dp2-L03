<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${imageURL}" height= 150px width= 500px alt="Acme Hacker-Rank Co., Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="administrator/administrator/create.do"><spring:message code="master.page.administrator.createAdmin" /></a></li>		
					<li><a href="statistics/administrator/show.do"><spring:message code="master.page.administrator.statistics" /></a></li>
					<li><a href="configuration/administrator/list.do"><spring:message code="master.page.administrator.configuration" /></a></li>
					<li><a href="broadcast/administrator/send.do"><spring:message code="master.page.administrator.broadcast" /></a></li>
					<li><a href="administrator/suspicious/list.do"><spring:message code="master.page.administrator.banUnban" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('COMPANY')">
			<li><a class="fNiv"><spring:message	code="master.page.company" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="position/company/list.do"><spring:message code="master.page.company.PositionList" /></a></li>
					<li><a href="problem/company/list.do"><spring:message code="master.page.company.listProblems" /></a></li>	
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('HACKER')">
			<li><a class="fNiv"><spring:message	code="master.page.hacker" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="finder/hacker/list.do"><spring:message code="master.page.hacker.finder" /></a></li>
					<li><a href="curriculum/hacker/list.do"><spring:message code="master.page.hacker.curriculums" /></a></li>
					<li><a href="application/hacker/list.do"><spring:message code="master.page.hacker.application" /></a></li>			
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv"><spring:message	code="master.page.register" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="anonymous/hacker/create.do"><spring:message code="master.page.createHacker" /></a></li>	
					<li><a href="anonymous/company/create.do"><spring:message code="master.page.createCompany" /></a></li>			
				</ul>
			</li>
			
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>				
					<li><a href="authenticated/showProfile.do"><spring:message code="master.page.myProfile" /> </a></li>
					<li><a href="authenticated/edit.do"><spring:message code="master.page.editPersonalData" /> </a></li>
					<li><a href="message/actor/list.do"><spring:message code="master.page.mailSystem" /> </a></li>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
			
			
		</security:authorize>
		
		<security:authorize access="permitAll">
			
			<li><a href="anonymous/position/list.do"><spring:message code="master.page.publicPositions" /></a></li>
			<li><a href="anonymous/company/list.do"><spring:message code="master.page.publicCompanies" /></a></li>
			
			<li><a class="fNiv"><spring:message	code="master.page.termsAndConditions" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="anonymous/termsAndConditionsEN.do"><spring:message code="master.page.termsAndConditionsEN" /></a></li>
					<li><a href="anonymous/termsAndConditionsES.do"><spring:message code="master.page.termsAndConditionsES" /></a></li>					
					
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasAnyRole('HACKER', 'COMPANY')">
		<li><a href="authenticated/deleteUser.do" onClick="return confirm('<spring:message code="delete.user.confirmation" />')"><spring:message code="master.page.deleteUser" /> </a></li>
		</security:authorize>
		
		
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

