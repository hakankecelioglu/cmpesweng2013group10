<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="row">
	<div class="span3">
		<span class="label label-success label-home-header">Try | Help | Solve</span>
		
		<c:forEach var="task" items="${alltasks}">
			<div class="well community-task-well">
			
				<h5>${task.title}</h5>
				<h6 class="text-right">community: ${task.community.name}</h6>
				<div class="clearfix"></div>
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
						<button class="btn btn-success" id="replyTask">Reply</button>
					</div>
				</div>
			</div>
		</c:forEach>
	</div><%-- End of random tasks --%>
	<div class="span9">
		<span class="label label-important label-home-header">Latest Communities</span>
		
		<c:forEach var="community" items="${allcommunities}">
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
						<h2 class="media-heading">${community.name}</h1>
						<p class="lead">${community.description}</p>
					</div>
				</div>
			</div>
		</c:forEach>
	</div><%-- End of random tasks --%>
</div>