<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="row">
	<div class="span8">
		<div class="clearfix bottom-margin">
			<div class="profile-picture-ctnr">
				<c:choose>
					<c:when test="${not empty profile.picture}">
						<img class="media-object profile-picture" src="<c:url value="/user/picture/${profile.picture}" />">
					</c:when>
					<c:otherwise>
						<img class="media-object profile-picture" src="http://b.vimeocdn.com/ps/445/980/4459809_300.jpg">
					</c:otherwise>
				</c:choose>
			</div>
			<c:choose>
				<c:when test="${empty profile.name}">
					<h2 class="profile-heading"><a href="<c:url value="/profile/${profile.username}" />">${profile.username}</a></h2>
				</c:when>
				<c:otherwise>
					<h2 class="profile-heading"><a href="<c:url value="/profile/${profile.username}" />">${profile.name}&nbsp;${profile.surname}</a></h2>
				</c:otherwise>
			</c:choose>
		</div>
		
		<div class="well profile-well span4 no-margin-left">
			<h4 class="title">Personal Information</h4>
			<table class="table table-stripped table-bordered">
				<tr>
					<th>Name</th>
					<td>${profile.name}</td>
				</tr>
				
				<tr>
					<th>Surname</th>
					<td>${profile.surname}</td>
				</tr>
				
				<tr>
					<th>Email</th>
					<td>${profile.email}</td>
				</tr>
				
				<tr>
					<th>Reputation</th>
					<td>${profile.rating}</td>
				</tr>
			</table>
		</div>
	</div>
	
	<div class="span4">
		<c:if test="${not empty myCommunities}">
			<ul class="nav nav-list">
				<li class="nav-header">Communities Involved</li>
				<c:forEach items="${myCommunities}" var="community">
					<li>
						<a href="<c:url value="/community/${community.id}" />">${community.name}</a>
					</li>
				</c:forEach>
			</ul>
		</c:if>
	</div>
</div>