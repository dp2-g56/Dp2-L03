
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<script type="text/javascript">

  function phonenumberval() {
	  
  var phoneNumber;
  phoneNumber = document.getElementById("phoneNumber").value;

		
  var res = false;
 
  if (/(\+[0-9]{1,3})(\([0-9]{1,3}\))([0-9]{4,})$/.test(phoneNumber)) {
    res = true;
  }
  if (/(\+[0-9]{3})([0-9]{4,})$/.test(phoneNumber)) {
	    res = true;
  }
  if(phoneNumber == ""){
	  alert("<spring:message code="anonymous.alertSave" />");
  }
  if(res == false && phoneNumber != "") {
	  
    confirm("<spring:message code="anonymous.confirmationPhone" />");
  }
 
}
   </script>

	<!-- Company -->
	<security:authorize access = "hasRole('COMPANY')">
		<form:form modelAttribute="company" action="authenticated/edit.do">


		<form:hidden path="id"/>
		<form:hidden path="version"/>
		<form:hidden path="problems"/>
		<form:hidden path="positions"/>
		
		<!-- Actor Attributes -->
		<fieldset>
    	<legend> <spring:message code="anonymous.personalData" /> </legend>
		<acme:textbox path="name" code="anonymous.name" />
		<br />
	
		<acme:textbox path="middleName" code="anonymous.middleName" />
		<br />
	
		<acme:textbox path="surname" code="anonymous.surname" />
		<br />
	
		<acme:textbox path="photo" code="anonymous.photo" />
		<br />
	
		<acme:textbox path="email" code="anonymous.email" />
		<br />
	
		<acme:textbox path="phoneNumber" code="anonymous.phoneNumber" />
		<br />
	
		<acme:textbox path="address" code="anonymous.address" />
		<br />	
		
		<acme:textbox path="companyName" code="company.companyName" />
		<br />
		</fieldset>
			
		<!-- BOTONES -->	
		<input type="submit" name="save" value="<spring:message code="anonymous.save" />" 
		onclick="phonenumberval();validateEmail();"/> 
	
		<acme:cancel url="/" code="anonymous.cancel" /> 
	
		</form:form>
	</security:authorize>
	
