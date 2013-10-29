<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="well">
	<div class="media">
		<a class="pull-left" href="#">
			<c:choose>
				<c:when test="${not empty community.picture}">
					<img class="media-object community-icon" src="<c:url value="/community/picture/${community.picture}" />">
				</c:when>
				<c:otherwise>
					<img class="media-object community-icon" src="http://b.vimeocdn.com/ps/445/980/4459809_300.jpg">
				</c:otherwise>
			</c:choose>
		</a>
		
		<div class="media-body">
			<h1 class="media-heading">${community.name}</h1>
			<p class="lead">${community.description}</p>
		</div>
	</div>
</div>

<ul class="nav nav-tabs">
	<li class="active">
		<a href="#">Home</a>
	</li>
	<li><a href="#">Members</a></li>
	<li><a href="#">Closed Tasks</a></li>
	
	<li class="pull-right"><a href="<c:url value="/task/create?communityId=${community.id}" />"><i class="icon-plus"></i> Create Task</a></li>
	<li class="pull-right"><a href="#">Join Community</a></li>
</ul>

<div class="row">
	<div class="span8">
		<h1 class="pull-left">Tasks</h1>
		<div class="pull-right">
			Sort by: 
			<select>
				<option>Latest</option>
				<option>Urgency</option>
			</select>
		</div>
	</div>
	
	<div class="span4">
		<h3>Similar Communities</h3>
	</div>
</div>

<div class="row">
	<div class="span8">
		<c:choose>
			<c:when test="${not empty tasks}">
				<c:forEach var="task" items="${tasks}">
					<div class="well community-task-well">
						<h3>${task.title}</h3>
						<div>
							<p class="pull-left">by <b>${task.owner.name}&nbsp;${task.owner.surname}</b></p>
							<p class="pull-right">Location: <b>Van/Turkey</b></p>
							<div class="clearfix"></div>
						</div>
						<hr class="clearfix">
						<p>
							${fn:replace(task.description, newLineChar, "<br />")}
						</p>
						
						<div class="clearfix">
							<div class="pull-left">Need: 55 more cadir</div>
							<div class="pull-right">
								<c:choose>
									<c:when test="${task.followerCount == 0}">
										No follower
									</c:when>
									<c:when test="${task.followerCount == 1}">
										1 follower
									</c:when>
									<c:otherwise>
										<c:out value="${task.followerCount}" /> followers
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						
						<div class="progress progress-striped">
							<div class="bar bar-success" style="width: 40%"></div>
							<div class="bar bar-warning" style="width: 60%;"></div>
						</div>
						
						<div class="clearfix">
							<div class="pull-left">${grouponfn:dateDiff(task.deadline)} days left!</div>
							<div class="pull-right">
								<button class="btn btn-success" id="followTask" data-taskid="${task.id}">Follow</button>
								<button class="btn btn-success">Reply</button>
							</div>
						</div>
					</div>
				</c:forEach>
				
				<%-- TODO: Remove later --%>
				
				<%-- pagination starts here --%>
				<div class="pagination">
					<ul>
						<li class="disabled"><a href="#">Prev</a></li>
						<li class="active"><a href="#">1</a></li>
						<li><a href="#">2</a></li>
						<li><a href="#">3</a></li>
						<li><a href="#">4</a></li>
						<li><a href="#">5</a></li>
						<li><a href="#">Next</a></li>
					</ul>
				</div>
				<%-- pagination ends here --%>
			</c:when>
			<c:otherwise>
				<div class="well community-task-well">
					<p>There is no task in this community!</p>
					<p>
						<a href="<c:url value="/task/create?communityId=${community.id}" />" class="btn btn-success"><i class="icon-plus"></i> Create Task</a>
					</p>
				</div>
			</c:otherwise>
		</c:choose>
		
	</div><%-- End of tasks --%>
	
	<div class="span4">
		<c:forEach var="similarCommunity" items="${similarCommunities}">
			
		</c:forEach>
		
		<%-- TODO: remove later --%>
		<div class="media">
			<a class="pull-left" href="#">
				<img class="media-object nav-user-thumb" src="http://b.vimeocdn.com/ps/445/980/4459809_300.jpg" style="width: 32px; height: 32px;">
			</a>
			<div class="media-body">
				<h4 class="media-heading">Golcuk Depremi</h4>
			</div>
		</div>
		<div class="media">
			<a class="pull-left" href="#">
				<img class="media-object nav-user-thumb" src="http://b.vimeocdn.com/ps/445/980/4459809_300.jpg" style="width: 32px; height: 32px;">
			</a>
			<div class="media-body">
				<h4 class="media-heading">Van Golu Canavari</h4>
			</div>
		</div>
		<div class="media">
			<a class="pull-left" href="#">
				<img class="media-object nav-user-thumb" src="http://b.vimeocdn.com/ps/445/980/4459809_300.jpg" style="width: 32px; height: 32px;">
			</a>
			<div class="media-body">
				<h4 class="media-heading">Duzce Depremi</h4>
			</div>
		</div>
		
	</div><%-- end of latest tasks --%>
</div>