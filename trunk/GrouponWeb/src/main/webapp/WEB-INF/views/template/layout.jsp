<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<!DOCTYPE html>
<html lang="en">

<tiles:insertAttribute name="htmlHead" />

<body class="<c:out value="${bodyClass}"/>">
	<tiles:insertAttribute name="nav" />
	<div class="container">
	
		<c:if test="${not empty user}">
			<tiles:insertAttribute name="innerNav" />
		</c:if>

		<tiles:insertAttribute name="body" />
		
		<tiles:insertAttribute name="footer" />
	</div>
</body>
</html>