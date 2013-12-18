<%@ include file="/WEB-INF/views/common/includes.jsp"%>
<div class="well community-task-well">
	<h3 class="pull-left">${task.title}</h3>
	<h6 class="pull-right">community: <a href="<c:url value="/community/${task.community.id}" />">${task.community.name}</a></h6>
	<div class="clearfix"></div>
	<div>
		<p class="pull-left">by <a href="<c:url value="/profile/${task.owner.username}" />"><b>${task.owner.username}</b></a></p>
		<!-- <p class="pull-right">Location: <b>Van/Turkey</b></p> -->
		<div class="clearfix"></div>
	</div>
	
	<hr class="clearfix">
	
	<h4 class="text-error">Description</h4>
	<p class="text-info">${fn:replace(task.description, newLineChar, "<br />")}</p>
	
	<c:forEach items="${taskAttributes}" var="attr">
		<h4 class="text-error">${attr.key}</h4>
		<p class="text-info">
			<c:choose>
				<c:when test="${grouponfn:isCollection(attr.value)}">
					<ul class="text-info">
						<c:forEach items="${attr.value}" var="value">
							<li>${value}</li>
						</c:forEach>
					</ul>
				</c:when>
				<c:otherwise>
					${attr.value}
				</c:otherwise>
			</c:choose>
		</p>
	</c:forEach>
	
	<div class="clearfix">
		<c:choose>
			<c:when test="${task.needType == 'GOODS'}">
				<div class="pull-left">Need: ${task.requirementQuantity} more ${task.requirementName}</div>
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
	
	<div class="progress progress-striped">
		<div class="bar bar-success" style="width: 1%"></div>
		<div class="bar bar-warning" style="width: 99%;"></div>
	</div>
	
	<div class="clearfix">
		<div class="pull-left">${grouponfn:dateDiff(task.deadline)} days left!</div>
		<div class="pull-right">
			<c:choose>
				<c:when test="${isFollower}">
					<button class="btn btn-danger btn-unfollow-task" data-taskid="${task.id}">Unfollow</button>
				</c:when>
				<c:otherwise>
					<button class="btn btn-success btn-follow-task" data-taskid="${task.id}">Follow</button>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<div class="well community-task-well">
	<c:if test="${task.needType eq 'GOODS'}">
		<form class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="goodQuantity">${task.requirementName}</label>
				<div class="controls">
					<input class="span2" type="text" id="goodQuantity"> out of ${task.requirementQuantity}
				</div>
			</div>
			
			<div class="clearfix">
				<button class="btn btn-success pull-right" id="followTask" data-taskid="${task.id}">Reply</button>
			</div>
		</form>
	</c:if>
</div>

<div class="clearfix" style="margin-bottom:20px;">
	<button class="btn btn-success pull-right" id="followTask" data-taskid="${task.id}">Send message to task owner</button>
</div>

<input type="hidden" value="${task.id}" id="hiddenTaskId" />