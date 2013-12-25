<%@ include file="/WEB-INF/views/common/includes.jsp"%>
<div class="well community-task-well">
	<h3 class="pull-left">${task.title}</h3>
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
				<div class="pull-left">Need: ${task.requirementQuantity - completedQuantity} more ${task.requirementName}</div>
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
				<c:when test="${not empty needPercent}">
					<c:set var="moreNeeded" value="${100 - needPercent}" />
					<div class="bar bar-success" style="width: ${needPercent}%"></div>
					<div class="bar bar-warning" style="width: ${moreNeeded}%;"></div>
				</c:when>
				<c:otherwise>
					<div class="bar bar-success" style="width: 1%"></div>
					<div class="bar bar-warning" style="width: 99%;"></div>
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
	
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

<div class="well task-replies-well">
	<h3>Task Replies</h3>
	<div class="loading-anim"></div>
	
	<div class="replies-container" style="display: none;">No Replies</div>
</div>

<!-- Hidden, only to copy with Javascript -->
<div class="media reply-item-hidden" style="display: none;">
	<a class="pull-left replier-profile" href="#">
		<img class="media-object replier-pic" style="width: 70px; height: 70px;" src="http://b.vimeocdn.com/ps/445/980/4459809_300.jpg" />
	</a>
	<div class="media-body">
		<h4 class="media-heading replier-name"><a class="replier-profile"></a></h4>
		<p class="reply-body"></p>
	</div>
</div>
<!-- END OF : Hidden, only to copy with Javascript -->

<div class="well reply-form-well">
	<h3>Reply This Task</h3>
	<div class="loading-anim"></div>
	
	<div class="replyFormContainer" style="display: none;">
		<form id="replyForm" class="form-horizontal">
			<c:if test="${task.needType eq 'GOODS'}">
				<div class="control-group tt-quantity-field">
					<label class="control-label" for="goodQuantity">${task.requirementName}</label>
					<div class="controls">
						<input class="span2" type="text" id="goodQuantity"> out of ${task.requirementQuantity - completedQuantity}
					</div>
				</div>
			</c:if>
		</form>
		
		<div class="clearfix">
			<button style="margin-left: 463px;" class="btn btn-warning btn-large" id="replyTaskBtn" data-taskid="${task.id}" data-loading-text="Sending...">Reply</button>
		</div>
	</div>
</div>

<div class="clearfix" style="margin-bottom:20px;">
	<button class="btn btn-success pull-right" id="followTask" data-taskid="${task.id}" data-loading-text="Sending...">Send message to task owner</button>
</div>

<input type="hidden" value="${task.id}" id="hiddenTaskId" />