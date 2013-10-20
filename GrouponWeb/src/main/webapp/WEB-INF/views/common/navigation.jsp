<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="container">
	<div class="row" style="padding-top: 10px; padding-bottom: 10px;">
		<div class="span5">
			<h1>TASK TOGETHER</h1>
		</div>
		
		<div class="span4">
			<c:if test="${!empty user}">
				<div class="media pull-right">
					<a class="pull-left" href="#">
						<img class="media-object nav-user-thumb" src="http://b.vimeocdn.com/ps/445/980/4459809_300.jpg">
					</a>
				
					<div class="media-body">
						<h4 class="media-heading">Welcome</h4>
						${user.username}
					</div>
				</div>
			</c:if>
		</div>
		
		<div class="span3">
			<c:if test="${!empty user}">
				<div style="text-align: right;">
					<a href="#" class="btn btn-warning">Notifications <span class="badge badge-important">6</span></a>
					<a href="#" class="btn btn-success">Followed Tasks</a>
				</div>
			</c:if>
			
			<div style="margin-top: 5px;">
				<form class="form-search" style="margin-bottom: 0;">
				  <div class="input-append pull-right">
				    <input type="text" class="span2 search-query">
				    <button type="submit" class="btn"><i class="icon-search"></i></button>
				  </div>
				</form>
			</div>
		</div>
	</div>
</div>

<div class="navbar navbar-static-top navbar-inverse">
	<div class="navbar-inner">
		<div class="container">
			<ul class="nav">
				<li class="active"><a href="#"><i class="icon-home"></i> Home</a></li>
				<li><a href="#"><i class="icon-info-sign"></i> About Us</a></li>
				<li><a href="#"><i class="icon-user"></i> Top Helpful Users</a></li>
				<li><a href="#"><i class="icon-download"></i> Android Version</a></li>
				<li><a href="#"><i class="icon-question-sign"></i> Contact Us</a></li>
				<li><a href="#"><i class="icon-search"></i> Advanced Search</a></li>
			</ul>
			
			<ul class="nav pull-right">
				<c:choose>
					<c:when test="${empty user}">
						<li><a href="#">Sign Up</a></li>
						<li class="dropdown">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#">Login <strong class="caret"></strong></a>
							<div class="dropdown-menu" style="padding: 20px;">
								<form class="form-horizontal" id="dropdownLoginForm">
									<div class="control-group">
										<div class="input-prepend">
										  <span class="add-on">Username: </span>
										  <input class="span2" id="loginFormUsername" type="text" placeholder="Username">
										</div>
									</div>
									
									<div class="control-group">
										<div class="input-prepend">
										  <span class="add-on">Password: </span>
										  <input class="span2" id="loginFormPassword" type="password" placeholder="Password">
										</div>
									</div>
									
									<div class="control-group">
										<label class="checkbox">
											<input type="checkbox"> Remember me
										</label>
										<button type="submit" class="btn">Sign in</button>
									</div>
								</form>
							</div>
						</li>
					</c:when>
					<c:otherwise>
						<li><a href="<c:url value="/logout" />">Logout</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
</div>