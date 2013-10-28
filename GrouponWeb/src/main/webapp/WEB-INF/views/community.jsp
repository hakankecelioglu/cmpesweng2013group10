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
	
	<li class="pull-right"><a href="<c:url value="/task/create?communityId=${community.id}" />">+ Create Task</a></li>
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
		<c:forEach var="task" items="${tasks}">
			<div class="well task-well">
				<p>${task.title}</p>
				<hr>
				<p>${task.description}</p>
				<hr>
				<table class="table table-bordered task-table">
					<thead>
						<tr>
							<th>Need</th>
							<th>Amount</th>
							<th>Progress</th>
							<th></th>
						</tr>
					</thead>
					
					<tbody>
						<c:forEach var="taskRequirement" items="${task.requirements}">
							<tr>
								<td>${taskRequirement.requirement.name}</td>
								<td>${!empty taskRequirement.amount ? taskRequirement.amount : 'unlimited'}</td>
								<td>33%</td>
								<td>
									<span class="label label-important"><i class="icon-minus"></i></span>
									<%-- <span class="label label-success"><i class="icon-ok"></i></span> --%>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:forEach>
		
		<%-- TODO: Remove later --%>
		<div class="well community-task-well">
			<h3>Cadir Yardimi</h3>
			<div>
				<p class="pull-left">by <b>Serdar Kuzucu</b></p>
				<p class="pull-right">Location: <b>Van/Turkey</b></p>
				<div class="clearfix"></div>
			</div>
			<hr class="clearfix">
			<p>
				Depremde evsiz kalan kardeslerimiz icin cadir topluyoruz. Su adrese gonderin: <br>
				Gul sokak, Aydogan apartmani, <br>
				Daire: 1, Kat: 1, <br>
				Besiktas/VAN
			</p>
			
			<div class="clearfix">
				<div class="pull-left">Need: 55 more cadir</div>
				<div class="pull-right">72 followers</div>
			</div>
			
			<div class="progress progress-striped">
				<div class="bar bar-success" style="width: 40%"></div>
				<div class="bar bar-warning" style="width: 60%;"></div>
			</div>
			
			<div class="clearfix">
				<div class="pull-left">5 days left!</div>
				<div class="pull-right">
					<button class="btn btn-success">Follow</button>
					<button class="btn btn-success">Reply</button>
				</div>
			</div>
		</div>
		
		<div class="well community-task-well">
			<h3>Cadir Yardimi</h3>
			<div>
				<p class="pull-left">by <b>Serdar Kuzucu</b></p>
				<p class="pull-right">Location: <b>Van/Turkey</b></p>
				<div class="clearfix"></div>
			</div>
			<hr class="clearfix">
			<p>
				Depremde evsiz kalan kardeslerimiz icin cadir topluyoruz. Su adrese gonderin: <br>
				Gul sokak, Aydogan apartmani, <br>
				Daire: 1, Kat: 1, <br>
				Besiktas/VAN
			</p>
			
			<div class="clearfix">
				<div class="pull-left">Need: 55 more cadir</div>
				<div class="pull-right">72 followers</div>
			</div>
			
			<div class="progress progress-striped">
				<div class="bar bar-success" style="width: 40%"></div>
				<div class="bar bar-warning" style="width: 60%;"></div>
			</div>
			
			<div class="clearfix">
				<div class="pull-left">5 days left!</div>
				<div class="pull-right">
					<button class="btn btn-success">Follow</button>
					<button class="btn btn-success">Reply</button>
				</div>
			</div>
		</div>
		
		<div class="well community-task-well">
			<h3>Cadir Yardimi</h3>
			<div>
				<p class="pull-left">by <b>Serdar Kuzucu</b></p>
				<p class="pull-right">Location: <b>Van/Turkey</b></p>
				<div class="clearfix"></div>
			</div>
			<hr class="clearfix">
			<p>
				Depremde evsiz kalan kardeslerimiz icin cadir topluyoruz. Su adrese gonderin: <br>
				Gul sokak, Aydogan apartmani, <br>
				Daire: 1, Kat: 1, <br>
				Besiktas/VAN
			</p>
			
			<div class="clearfix">
				<div class="pull-left">Need: 55 more cadir</div>
				<div class="pull-right">72 followers</div>
			</div>
			
			<div class="progress progress-striped">
				<div class="bar bar-success" style="width: 40%"></div>
				<div class="bar bar-warning" style="width: 60%;"></div>
			</div>
			
			<div class="clearfix">
				<div class="pull-left">5 days left!</div>
				<div class="pull-right">
					<button class="btn btn-success">Follow</button>
					<button class="btn btn-success">Reply</button>
				</div>
			</div>
		</div>
		
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