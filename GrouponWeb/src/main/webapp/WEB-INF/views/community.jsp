<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="well">
	<div class="media">
		<a class="pull-left" href="#">
			<c:choose>
				<c:when test="${not empty community.picture}">
					<img class="media-object community-icon" src="<c:url value="/community/thumb/medium/${community.picture}" />">
				</c:when>
				<c:otherwise>
					<img class="media-object community-icon" src="<c:url value="/res/img/default_com_picture.jpg" />">
				</c:otherwise>
			</c:choose>
		</a>
		
		<div class="media-body">
			<h1 class="media-heading">${community.name}</h1>
			<p class="lead">${community.description}</p>
		</div>
	</div>
</div>

<ul id="tabList" class="nav nav-tabs">
	<li class="active"><a class="tabListLink" data-toogle="tab" href="#tabHome" >Home</a></li>
	<li><a class="tabListLink" data-toogle="tab" href="#tabMembers">Members</a></li>
	<li><a  class="tabListLink" data-toogle="tab" href="#tabClosedTasks" >Closed Tasks</a></li>
	
	<li class="pull-right"><a class="createTaskLink" href="<c:url value="/task/create?communityId=${community.id}" />"><i class="icon-plus"></i> Create Task</a></li>
	
	<c:if test="${isOwner}">
		<li class="pull-right"><a href="<c:url value="/community/createTaskType?communityId=${community.id}" />"><i class="icon-plus"></i> Create Task Type</a></li>
	</c:if>
	
	<c:choose>
		<c:when test="${isMember}">
			<li class="pull-right"><a href="<c:url value="/community/leave?communityId=${community.id}" />">Leave Community</a></li>
		</c:when>
		<c:otherwise>
			<li class="pull-right"><a href="<c:url value="/community/join?communityId=${community.id}" />">Join Community</a></li>
		</c:otherwise>
	</c:choose>
</ul>

<div class="row">

	<div class="tab-content span8">
	
		<div class="tab-pane active" id="tabHome">
			<div class="clearfix">
				<h1 class="pull-left">Tasks</h1>
				<div class="pull-right">
					Sort by: 
					<select>
						<option>Latest</option>
						<option>Urgency</option>
					</select>
				</div>
			</div>
			
			<c:choose>
				<c:when test="${not empty tasks}">
					<c:forEach var="task" items="${tasks}">
						<div class="well community-task-well">
							<h3 class="pull-left"><a href="<c:url value="/task/show/${task.id}" />">${task.title}</a></h3>
							<h6 class="pull-right">community: ${task.community.name}</h6>
							<div class="clearfix"></div>
							<div>
								<p class="pull-left">by <b>${task.owner.username}</b></p>
								<p class="pull-right">Location: <b>Van/Turkey</b></p>
								<div class="clearfix"></div>
							</div>
							<hr class="clearfix">
							<p>
								${fn:replace(task.description, newLineChar, "<br />")}
							</p>
							
							<div class="clearfix">
								<div class="pull-left">Need: 55 more cadir</div>
								<div class="pull-right task-follower-count">
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
									<c:choose>
										<c:when test="${followedMap[task.id]}">
											<button class="btn btn-danger btn-unfollow-task" data-taskid="${task.id}">Unfollow</button>
										</c:when>
										<c:otherwise>
											<button class="btn btn-success btn-follow-task" data-taskid="${task.id}">Follow</button>
										</c:otherwise>
									</c:choose>
									<a class="btn btn-success" href="<c:url value="/task/show/${task.id}" />">Reply</a>
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
		
		<div class="tab-pane" id="tabMembers">
			<div class="clearfix">
				<h1 class="pull-left">Members</h1>
				<div class="pull-right">
					Sort by: 
					<select>
						<option>Latest</option>
						<option>Urgency</option>
					</select>
				</div>
			</div>
			
			<c:choose>
				<c:when test="${not empty tasks}">
					<c:forEach var="task" items="${tasks}">
						<div class="well community-task-well">
							<h3 class="pull-left"><a href="<c:url value="/task/show/${task.id}" />">${task.title}</a></h3>
							<h6 class="pull-right">community: ${task.community.name}</h6>
							<div class="clearfix"></div>
							<div>
								<p class="pull-left">by <b>${task.owner.username}</b></p>
								<p class="pull-right">Location: <b>Van/Turkey</b></p>
								<div class="clearfix"></div>
							</div>
							<hr class="clearfix">
							<p>
								${fn:replace(task.description, newLineChar, "<br />")}
							</p>
							
							<div class="clearfix">
								<div class="pull-left">Need: 55 more cadir</div>
								<div class="pull-right task-follower-count">
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
									<c:choose>
										<c:when test="${followedMap[task.id]}">
											<button class="btn btn-danger btn-unfollow-task" data-taskid="${task.id}">Unfollow</button>
										</c:when>
										<c:otherwise>
											<button class="btn btn-success btn-follow-task" data-taskid="${task.id}">Follow</button>
										</c:otherwise>
									</c:choose>
									<a class="btn btn-success" href="<c:url value="/task/show/${task.id}" />">Reply</a>
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
		
		<div class="tab-pane" id="tabClosedTasks">
			<div class="clearfix">
				<h1 class="pull-left">Closed Tasks</h1>
				<div class="pull-right">
					Sort by: 
					<select>
						<option>Latest</option>
						<option>Urgency</option>
					</select>
				</div>
			</div>
			
			<c:choose>
				<c:when test="${not empty tasks}">
					<c:forEach var="task" items="${tasks}">
						<div class="well community-task-well">
							<h3 class="pull-left"><a href="<c:url value="/task/show/${task.id}" />">${task.title}</a></h3>
							<h6 class="pull-right">community: ${task.community.name}</h6>
							<div class="clearfix"></div>
							<div>
								<p class="pull-left">by <b>${task.owner.username}</b></p>
								<p class="pull-right">Location: <b>Van/Turkey</b></p>
								<div class="clearfix"></div>
							</div>
							<hr class="clearfix">
							<p>
								${fn:replace(task.description, newLineChar, "<br />")}
							</p>
							
							<div class="clearfix">
								<div class="pull-left">Need: 55 more cadir</div>
								<div class="pull-right task-follower-count">
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
									<c:choose>
										<c:when test="${followedMap[task.id]}">
											<button class="btn btn-danger btn-unfollow-task" data-taskid="${task.id}">Unfollow</button>
										</c:when>
										<c:otherwise>
											<button class="btn btn-success btn-follow-task" data-taskid="${task.id}">Follow</button>
										</c:otherwise>
									</c:choose>
									<a class="btn btn-success" href="<c:url value="/task/show/${task.id}" />">Reply</a>
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
	</div>
	
	<div class="span4">
		<div class="h-similiar-communities">
			<h3>Similar Communities</h3>
			<div class="similiar-communities-body"></div>
		</div>
	</div><%-- end of latest tasks --%>
</div>

<input type="hidden" id="communityId" value="${community.id}" />

<!-- taskTypeSelectionModal -->
<div id="taskTypeSelectionModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3 id="myModalLabel">Select Task Type</h3>
	</div>
	<div class="modal-body">
		<p>
			<select id="taskTypeSelection">
				<option value="groupon::default">Default</option>
			</select>
		</p>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
		<button class="btn btn-primary" id="modalCreateTaskBtn">Create Task</button>
	</div>
</div>