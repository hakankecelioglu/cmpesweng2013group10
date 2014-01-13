<%@ include file="/WEB-INF/views/common/includes.jsp"%>
<div class="row">
	<div class="span2" id="home-left-frame">
		<h4 class="small-heading">Filter</h4>
		<ul class="nav nav-tabs nav-stacked">
			<li><a href="#">Home Feed</a></li>
			<li><a href="#">Community Feed</a></li>
			<li><a href="#">Followed Tasks</a></li>
			<li><a href="<c:url value="/allCommunities" />">All
					Communities</a></li>
			<li><a href="#">New Tasks</a></li>
		</ul>

		<div class="h-user-communities" style="display: none;">
			<h4 class="small-heading">Communities</h4>
			<ul class="nav nav-tabs nav-stacked"></ul>
		</div>
	</div>

	<div class="span9">
		<span class="label label-important label-home-header">All
			Communities</span>

		<c:forEach var="community" items="${allcommunities}">
			<div class="well">
				<div class="media">
					<a class="pull-left" href="#"> <c:choose>
							<c:when test="${not empty community.picture}">
								<img class="media-object community-icon"
									src="<c:url value="/community/picture/${community.picture}" />">
							</c:when>
							<c:otherwise>
								<img class="media-object community-icon"
									src="http://b.vimeocdn.com/ps/445/980/4459809_300.jpg">
							</c:otherwise>
						</c:choose>
					</a>

					<div class="media-body">
						<h2 class="media-heading">${community.name}</h2>
						<p class="lead">${community.description}</p>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div>
