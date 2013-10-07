<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<head>
   	<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title><tiles:getAsString name="title"/></title>
    
    <%-- CSS Files --%>
	<link href="<c:url value="${cssFolder}/bootstrap.min.css"/>" rel="stylesheet" type="text/css">
	<link href="<c:url value="${cssFolder}/bootstrap-responsive.min.css"/>" rel="stylesheet" type="text/css">
	<link href="<c:url value="${cssFolder}/customize-bootstrap.css"/>" rel="stylesheet" type="text/css">
    
	<%-- CSS Files Defined in tiles.xml --%>
	<tiles:importAttribute name="cssFiles" ignore="true" />
	<tiles:importAttribute name="externalCssFiles" ignore="true" />
	
	<%-- for CSS Files in this project --%>
	<c:forEach items="${cssFiles}" var="cssFile">
		<link rel="stylesheet" href="<c:url value="${cssFolder}${cssFile}?v=${cacheVersion}" />" />
	</c:forEach>
	
	<%-- for CSS files out of this project (Hosted anywhere) --%>
	<c:forEach items="${externalCssFiles}" var="cssFile">
		<link rel="stylesheet" href="<c:url value="${cssFile}" />" />
	</c:forEach>
	
	<%-- favicon --%>
	<link rel="shortcut icon" href="${imgFolder}/favicon.png">

	<%-- JavaScript Files --%>
	<script type="text/javascript" src="<c:url value="${jsFolder}/jquery-1.10.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="${jsFolder}/bootstrap.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="${jsFolder}/main.js"/>"></script>
	
	<%-- Javascript Files defined in tiles.xml --%>
	<tiles:importAttribute name="jsFiles" ignore="true" />
	<tiles:importAttribute name="externalJsFiles" ignore="true" />
	
	<%-- for Javascript Files in this project --%>
	<c:forEach items="${jsFiles}" var="jsFile">
		<script type="text/javascript" src="<c:url value="${jsFolder}${jsFile}?v=${cacheVersion}" />"></script>
	</c:forEach>
	
	<%-- for Javascript Files out of this project (Hosted anywhere) --%>
	<c:forEach items="${externalJsFiles}" var="jsFile">
		<script type="text/javascript" src="<c:url value="${jsFile}" />"></script>
	</c:forEach>
</head>