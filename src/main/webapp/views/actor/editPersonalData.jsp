
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
		<form:form modelAttribute="formObjectEditCompany" action="authenticated/edit.do">


		<form:hidden path="id"/>

		<!-- Actor Attributes -->
		<fieldset>
    	<legend> <spring:message code="anonymous.personalData" /> </legend>
		<acme:input path="name" code="anonymous.name" />
		<br />
	
		<acme:input path="surname" code="anonymous.surname" />
		<br />
	
		<acme:input path="photo" code="anonymous.photo" />
		<br />
	
		<acme:input path="email" code="anonymous.email" />
		<br />
		
		<acme:textbox path="VATNumber" code="admin.VATNumber" />
		<br />
	
		<acme:input path="phone" code="anonymous.phoneNumber" />
		<br />
	
		<acme:input path="address" code="anonymous.address" />
		<br />	
		
		<acme:input  path="companyName" code="company.companyName" />
		<br />
		</fieldset>
			
			<fieldset>
	  <legend> <spring:message code="creditCard.data" /> </legend>
	  
	  	<br />
	  
		<acme:input code="creditCard.holderName" path="holderName"/>	
		<br />
		
		<acme:selectString code="creditCard.brandName" path="brandName" items="${cardType}" itemsName="${cardType}"/>	
		<br />
		
		<acme:input code="creditCard.number" path="number"/>	
		<br />
		
		<acme:input code="creditCard.expirationMonth" path="expirationMonth"/>	
		<br />
		
		<acme:input code="creditCard.expirationYear" path="expirationYear"/>	
		<br />
		
		<acme:input code="creditCard.cvvCode" path="cvvCode"/>	
		<br />
		
	</fieldset>
				
		<!-- BOTONES -->	
		<input type="submit" name="save" value="<spring:message code="anonymous.save" />" 
		onclick="phonenumberval();validateEmail();"/> 
	
		<acme:cancel url="/" code="anonymous.cancel" /> 
	
		</form:form>
	</security:authorize>
	
	<security:authorize access = "hasRole('ADMIN')">
		<form:form modelAttribute="formObjectEditAdmin" action="authenticated/admin/edit.do">


		<form:hidden path="id"/>

		<!-- Actor Attributes -->
		<fieldset>
    	<legend> <spring:message code="anonymous.personalData" /> </legend>
		<acme:input path="name" code="anonymous.name" />
		<br />
	
		<acme:input path="surname" code="anonymous.surname" />
		<br />
	
		<acme:input path="photo" code="anonymous.photo" />
		<br />
	
		<acme:input path="email" code="anonymous.email" />
		<br />
		
		<acme:textbox path="VATNumber" code="admin.VATNumber" />
		<br />
	
		<acme:input path="phone" code="anonymous.phoneNumber" />
		<br />
	
		<acme:input path="address" code="anonymous.address" />
		<br />	
		
		<br />
		</fieldset>
			
			<fieldset>
	  <legend> <spring:message code="creditCard.data" /> </legend>
	  
	  	<br />
	  
		<acme:input code="creditCard.holderName" path="holderName"/>	
		<br />
		
		<acme:selectString code="creditCard.brandName" path="brandName" items="${cardType}" itemsName="${cardType}"/>	
		<br />
		
		<acme:input code="creditCard.number" path="number"/>	
		<br />
		
		<acme:input code="creditCard.expirationMonth" path="expirationMonth"/>	
		<br />
		
		<acme:input code="creditCard.expirationYear" path="expirationYear"/>	
		<br />
		
		<acme:input code="creditCard.cvvCode" path="cvvCode"/>	
		<br />
		
	</fieldset>
				
		<!-- BOTONES -->	
		<input type="submit" name="save" value="<spring:message code="anonymous.save" />" 
		onclick="phonenumberval();"/> 
	
		<acme:cancel url="/" code="anonymous.cancel" /> 
	
		</form:form>
	</security:authorize>
	
	<security:authorize access = "hasRole('HACKER')">
		<form:form modelAttribute="formObjectEditHacker" action="authenticated/hacker/edit.do">


		<form:hidden path="id"/>

		<!-- Actor Attributes -->
		<fieldset>
    	<legend> <spring:message code="anonymous.personalData" /> </legend>
		<acme:input path="name" code="anonymous.name" />
		<br />
	
		<acme:input path="surname" code="anonymous.surname" />
		<br />
	
		<acme:input path="photo" code="anonymous.photo" />
		<br />
	
		<acme:input path="email" code="anonymous.email" />
		<br />
		
		<acme:textbox path="VATNumber" code="admin.VATNumber" />
		<br />
	
		<acme:input path="phone" code="anonymous.phoneNumber" />
		<br />
	
		<acme:input path="address" code="anonymous.address" />
		<br />	
		
		<br />
		</fieldset>
			
			<fieldset>
	  <legend> <spring:message code="creditCard.data" /> </legend>
	  
	  	<br />
	  
		<acme:input code="creditCard.holderName" path="holderName"/>	
		<br />
		
		<acme:selectString code="creditCard.brandName" path="brandName" items="${cardType}" itemsName="${cardType}"/>	
		<br />
		
		<acme:input code="creditCard.number" path="number"/>	
		<br />
		
		<acme:input code="creditCard.expirationMonth" path="expirationMonth"/>	
		<br />
		
		<acme:input code="creditCard.expirationYear" path="expirationYear"/>	
		<br />
		
		<acme:input code="creditCard.cvvCode" path="cvvCode"/>	
		<br />
		
	</fieldset>
				
		<!-- BOTONES -->	
		<input type="submit" name="save" value="<spring:message code="anonymous.save" />" 
		onclick="phonenumberval();"/> 
	
		<acme:cancel url="/" code="anonymous.cancel" /> 
	
		</form:form>
	</security:authorize>
	
