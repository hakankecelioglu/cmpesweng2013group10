<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="row">
	<%------------------------------------ LEFT FRAME ----------------------------------------------%>
	<jsp:include page="common/leftMenu.jsp"></jsp:include>
	<%------------------------------------ LEFT FRAME ENDS ----------------------------------------------%>

	<div class="span9">
		<span class="label label-important label-home-header">Top Helpful Users</span>

		<c:forEach var="user" items="${users}">
			<div class="well" style="position: relative;">
				<div class="media">
					<a class="pull-left" href="<c:url value="/profile/${user.username}" />">
						<c:choose>
							<c:when test="${not empty user.picture}">
								<img class="media-object community-icon" src="<c:url value="/user/picture/${user.picture}" />">
							</c:when>
							<c:otherwise>
								<img class="media-object community-icon" src="http://b.vimeocdn.com/ps/445/980/4459809_300.jpg">
							</c:otherwise>
						</c:choose>
					</a>

					<div class="media-body">
						<h2 class="media-heading"><a href="<c:url value="/profile/${user.username}" />">${user.username}</a></h2>
						<p class="lead">${user.username}</p>
					</div>
				</div>
				
				<div class="date" style="position: absolute; bottom: 20px; right: 20px;">
					${user.rating}
				</div>
			</div>
		</c:forEach>
		
		<%-- pagination starts here --%>
		<div class="pagination">
			<ul>
				<c:choose>
					<c:when test="${hasPrev}">
						<li><a href="<c:url value="/user/helpful?page=${currentPage - 1}" />">Prev</a></li>
					</c:when>
					<c:otherwise>
						<li class="disabled"><a href="javascript:;">Prev</a></li>
					</c:otherwise>
				</c:choose>
				
				<c:forEach items="${pages}" var="page">
					<c:choose>
						<c:when test="${currentPage eq page}">
							<li class="active"><a href="javascript:;">${page + 1}</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="<c:url value="/user/helpful?page=${page}" />">${page + 1}</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				
				<c:choose>
					<c:when test="${hasNext}">
						<li><a href="<c:url value="/user/helpful?page=${currentPage + 1}" />">Next</a></li>
					</c:when>
					<c:otherwise>
						<li class="disabled"><a href="javascript:;">Next</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
		<%-- pagination ends here --%>
	</div>
</div>