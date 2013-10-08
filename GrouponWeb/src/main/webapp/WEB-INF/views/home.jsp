<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="row">
	<div class="span4">
		<span class="label label-success label-home-header">Random Tasks</span>
		<div class="well task-well">
			<p>We need a doctor near Taksim.</p>
			<hr>
			<p>We are waiting for doctors inside of the Taksim Burger which is placed Imam Adnan Sokak. We have only gas masks!</p>
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
					<tr>
						<td>Doctor</td>
						<td>3</td>
						<td>33%</td>
						<td><span class="label label-important"><i class="icon-minus"></i></span></td>
					</tr>
					
					<tr>
						<td>First aid outfit</td>
						<td>unlimited</td>
						<td>-</td>
						<td><span class="label label-success"><i class="icon-ok"></i></span></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	<div class="span4">
		<span class="label label-important label-home-header">Urgent Tasks</span>
	</div>
	
	<div class="span4">
		<span class="label label-warning label-home-header">Latest Tasks</span>
	</div>
</div>