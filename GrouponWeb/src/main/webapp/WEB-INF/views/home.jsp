<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="row">
	<%------------------------------------ LEFT FRAME ----------------------------------------------%>
	<jsp:include page="common/leftMenu.jsp"></jsp:include>
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
					
						<h5 class="pull-left"><a href="<c:url value="/task/show/${task.id}" />">${task.title}</a></h5>
						<h6 class="pull-right">&nbsp;Community: <a href="<c:url value="/community/${task.community.id}" />">${task.community.name}</a></h6>
						<a class="pull-right" href="<c:url value="/community/${task.community.id}" />">
							<c:choose>
								<c:when test="${not empty task.community.picture}">
									<img class="media-object" height="60" width="60" src="<c:url value="/community/thumb/medium/${task.community.picture}" />">
								</c:when>
								<c:otherwise>
									<img class="media-object" height="60" width="60" src="<c:url value="/res/img/default_com_picture.jpg" />">
								</c:otherwise>
							</c:choose>
						</a>
						<div class="clearfix"></div>
						<div>
							<p class="pull-left">by <a href="<c:url value="/profile/${task.owner.username}" />"><b>${task.owner.username}</b></a></p>
							<p class="pull-right">at 
								<a href="<c:url value="/task/show/${task.id}" />">
									<b><fmt:formatDate value="${task.createDate}" pattern="dd.MM.yyyy hh:mm"/></b>
								</a>
							</p>
							<div class="clearfix"></div>
						</div>
						<hr class="clearfix">
						<p>
							${fn:replace(task.description, newLineChar, "<br />")}
						</p>
						
						<div class="clearfix">
							<c:choose>
								<c:when test="${task.needType == 'GOODS'}">
									<c:set var="completedQuantity" value="${quantityCompleted[task.id]}" />
									<c:choose>
										<c:when test="${not empty completedQuantity}">
											<div class="pull-left">Need: ${task.requirementQuantity - completedQuantity} more ${task.requirementName}</div>
										</c:when>
										<c:otherwise>
											<div class="pull-left">Need: ${task.requirementQuantity} more ${task.requirementName}</div>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${task.needType == 'SERVICE'}">
									<div class="pull-left">Need: ${task.requirementName}</div>
								</c:when>
							</c:choose>
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
						
						<c:if test="${task.needType == 'GOODS'}">
							<div class="progress progress-striped">
								<c:choose>
									<c:when test="${not empty percentCompleted[task.id]}">
										<c:set var="percent" value="${percentCompleted[task.id]}" />
										<c:set var="remainingPercent" value="${100 - percent}"/>
										<div class="bar bar-success" style="width: ${percent}%"></div>
										<div class="bar bar-warning" style="width: ${remainingPercent}%;"></div>
									</c:when>
									<c:otherwise>
										<div class="bar bar-success" style="width: 1%"></div>
										<div class="bar bar-warning" style="width: 99%;"></div>
									</c:otherwise>
								</c:choose>
							</div>
						</c:if>
						
						<div class="clearfix">
							<c:set var="remainingDays" value="${grouponfn:dateDiff(task.deadline)}" />
							<c:choose>
								<c:when test="${remainingDays == 1}">
									<div class="pull-left">1 day left!</div>
								</c:when>
								<c:when test="${remainingDays <= 0}">
									Task deadline is over!
								</c:when>
								<c:otherwise>
									<div class="pull-left">${remainingDays} days left!</div>
								</c:otherwise>
							</c:choose>
							
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