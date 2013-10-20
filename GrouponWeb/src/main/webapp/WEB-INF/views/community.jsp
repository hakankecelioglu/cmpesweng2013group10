<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="well">
	${community.name}
</div>

<div class="well">
	${community.description}
</div>

<div class="row">
	<div class="span4">
		<span class="label label-success label-home-header">Random Tasks</span>
		
		<c:forEach var="task" items="${randomTasks}">
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
	</div><%-- End of random tasks --%>
	
	<div class="span4">
		<span class="label label-important label-home-header">Urgent Tasks</span>
		
		<c:forEach var="task" items="${urgentTasks}">
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
	</div><%-- end of urgent tasks --%>
	
	<div class="span4">
		<span class="label label-warning label-home-header">Latest Tasks</span>
		
		<c:forEach var="task" items="${latestTasks}">
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
	</div><%-- end of latest tasks --%>
</div>