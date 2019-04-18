<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

	
	<display:table name="companies" id="row">
	
		<display:column property="name" titleKey="companies.name" /> 
		<display:column property="VATNumber" titleKey="companies.VATNumber" /> 
		<display:column property="photo" titleKey="companies.photo" /> 
		<display:column property="email" titleKey="companies.email" /> 
		<display:column property="phone" titleKey="companies.phone" /> 
		<display:column property="address" titleKey="companies.address" /> 
		
		<display:column titleKey="companies.positions">
    

    	
    		
       		<spring:url var="positionsUrl" value="/anonymous/company/positions.do?idCompany={idCompany}">
            	<spring:param name="idCompany" value="${row.id}"/>
        	</spring:url>
        	
        	<a href="${positionsUrl}">
              <spring:message var ="viewPositions" code="position.viewPositions" />
             <jstl:out value="${viewPositions}" />   
        	</a>
        	
        </display:column>
	
	</display:table>
	

