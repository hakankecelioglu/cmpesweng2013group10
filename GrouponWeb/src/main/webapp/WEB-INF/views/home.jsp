<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="row">
	<%------------------------------------ LEFT FRAME ----------------------------------------------%>
	<div class="span2" id="home-left-frame">
		<h4 class="small-heading">Filter</h4>
		<ul class="nav nav-tabs nav-stacked">
			<li><a href="#">Home Feed</a></li>
			<li><a href="#">Community Feed</a></li>
			<li><a href="#">Followed Tasks</a></li>
		</ul>
		
		<div class="h-user-communities" style="display: none;">
			<h4 class="small-heading">Communities</h4>
			<ul class="nav nav-tabs nav-stacked"></ul>
		</div>
	</div>
	<%------------------------------------ LEFT FRAME ENDS ----------------------------------------------%>
	
	<%------------------------------------ MIDDLE FRAME BEGINS ----------------------------------------------%>
	<div class="span7">
		<div class="sort-selection-line">
			<a href="javascript:;" class="sort-selection-item"><span>SORT: ${sortby}</span> <strong class="caret"></strong></a>
			<ul class="sort-selection-menu nav nav-tabs nav-stacked">
				<li><a href="#" data-sort="DEADLINE">Deadline</a></li>
				<li><a href="#" data-sort="LATEST">Latest</a></li>
			</ul>
		</div>
		
		<c:choose>
			<c:when test="${not empty homeFeedTasks}">
				<c:if test="${emptyHomeFeed}">
					<div class="well text-info">
						<p>You are neither following a task nor a member of a community. Therefore, here we the list of recent tasks below.</p>
					</div>
				</c:if>
				<c:forEach var="task" items="${homeFeedTasks}">
					<div class="well community-task-well">
					
						<h5><a href="<c:url value="/task/show/${task.id}" />">${task.title}</a></h5>
						<h6 class="text-right">community: <a href="<c:url value="/community/${task.community.id}" />">${task.community.name}</a></h6>
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
	<%------------------------------------ MIDDLE FRAME ENDS ----------------------------------------------%>
	
	<%------------------------------------ RIGHT FRAME BEGINS ----------------------------------------------%>
	<div class="span3">
		<div class="h-suggestions">
			<h4 class="small-heading">Suggested Tasks for You</h4>
			<div class="list"><!-- Javascript will fill here! --></div>
			<p class="text-info no-suggestion-info well" style="display: none;">We don't have any recommendation for you now.</p>
		</div>
	</div>
	<%------------------------------------ RIGHT FRAME ENDS ----------------------------------------------%>
</div>