<%@ page language="java" pageEncoding="utf8" contentType="text/html;charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://groupon.s/groupondateutils" prefix="grouponfn" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<fmt:setLocale value="en_US" scope="session" />

<c:set var="currentTimeMillis" value="<%=System.currentTimeMillis()%>" />
<c:set var="cacheVersion" value="1" />

<c:set var="jsFolder" value="/res/js" />
<c:set var="imgFolder" value="/res/img" />
<c:set var="cssFolder" value="/res/css" />