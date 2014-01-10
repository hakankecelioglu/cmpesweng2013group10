<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="row">
	<div class="span4">
		<div class="form-search">
			<p class="text-info">Find user by user id:</p>
			<input type="text" value="" class="input-medium search-query" id="userId" />
			<button id="getUserByIdBtn" class="btn btn-success">Get User</button>
		</div>
		
		<div class="form-search">
			<p class="text-info">Find user by username:</p>
			<input type="text" value="" class="input-medium search-query" id="username" />
			<button id="getUserByUsernameBtn" class="btn btn-success">Get User</button>
		</div>
	</div>
	
	<div class="span8">
		<table class="table table-stripped table-bordered">
			<tr>
				<th>ID</th>
				<td class="td-uid"></td>
			</tr>
			<tr>
				<th>Name</th>
				<td class="td-name"></td>
			</tr>
			<tr>
				<th>Surname</th>
				<td class="td-surname"></td>
			</tr>
			<tr>
				<th>Username</th>
				<td class="td-username"></td>
			</tr>
			<tr>
				<th>Email</th>
				<td class="td-email"></td>
			</tr>
			<tr>
				<th>Role</th>
				<td class="td-role"></td>
			</tr>
			<tr>
				<th>Status</th>
				<td class="td-status"></td>
			</tr>
			<tr>
				<th colspan="2">
					<button class="btn btn-danger">BAN</button>
					<button class="btn btn-info">ACTIVATE</button>
					<button class="btn btn-warning">DELETE</button>
					<button class="btn btn-inverse" id="makeAdmin">MAKE ADMIN</button>
				</th>
			</tr>
		</table>
	</div>
</div>

<div style="margin: 10px;"></div>