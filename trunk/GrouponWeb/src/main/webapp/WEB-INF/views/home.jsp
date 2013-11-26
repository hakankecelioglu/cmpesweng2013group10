<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="row">

	<%------------------------------------------- FOLLOWED TASKS ----------------------------------------------------%>
	<div class="span4">
		<span class="label label-success label-home-header">Followed Tasks</span>
		
		<c:choose>
			<c:when test="${not empty followedTasks}">
				<c:forEach var="task" items="${followedTasks}">
					<div class="well community-task-well">
					
						<h5><a href="<c:url value="/task/show/${task.id}" />">${task.title}</a></h5>
						<h6 class="text-right">community: <a href="<c:url value="/community/${task.community.id}" />">${task.community.name}</a></h6>
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
								<a class="btn btn-success" href="<c:url value="/task/show/${task.id}" />">Reply</a>
							</div>
						</div>
					</div>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<div class="well community-task-well">
					<h5>Nothing to display here</h5>
					<hr class="clearfix">
					<p>
						You don't follow any task.
					</p>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	<%------------------------------------------- END OF FOLLOWED TASKS ----------------------------------------------------%>
	
	<%---------------------------------------------- COMMUNITY TASKS -------------------------------------------------------%>
	<div class="span4">
		<span class="label label-important label-home-header">Community Tasks</span>
		
		<c:forEach var="task" items="${communityTasks}">
			<div class="well community-task-well">
			
				<h5><a href="<c:url value="/task/show/${task.id}" />">${task.title}</a></h5>
				<h6 class="text-right">community: <a href="<c:url value="/community/${task.community.id}" />">${task.community.name}</a></h6>
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
						<a class="btn btn-success" href="<c:url value="/task/show/${task.id}" />">Reply</a>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
	<%---------------------------------------------- END OF COMMUNITY TASKS -------------------------------------------------------%>
	
	<%-------------------------------------------------- SUGGESTED TASKS ----------------------------------------------------------%>
	<div class="span4">
		<span class="label label-warning label-home-header">Suggested Tasks</span>
		
		<c:forEach var="task" items="${recommendedTasks}">
				<div class="well community-task-well">
			
				<h5><a href="<c:url value="/task/show/${task.id}" />">${task.title}</a></h5>
				<h6 class="text-right">community: <a href="<c:url value="/community/${task.community.id}" />">${task.community.name}</a></h6>
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
						<a class="btn btn-success" href="<c:url value="/task/show/${task.id}" />">Reply</a>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
	<%---------------------------------------------- END OF SUGGESTED TASKS -------------------------------------------------------%>
</div>