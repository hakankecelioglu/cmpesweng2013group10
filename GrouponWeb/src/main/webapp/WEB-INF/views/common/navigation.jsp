<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<input type="hidden" id="siteBaseUrl" value="${contextPath}" />

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
				<div id="topRightButtons">
					<a href="#" class="btn btn-warning" id="notifications">Notifications <span class="badge badge-important" style="display: none;"></span></a>
					<a href="#" class="btn btn-success">Followed Tasks</a>
				</div>
			</c:if>
			
			<div id="notificationsPanel" style="display: none;">
				<div class="notification-panel" style="width: 100%">
					<h5 style="border-bottom: 1px solid #ccc;">Notifications</h5>
					<div class="notifications-body">
						<div class="notifications-loading" style="text-align: center;">Loading...</div>
					</div>
				</div>
			</div>
			
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

<div class="navbar navbar-static-top navbar-inverse header-navbar">
	<div class="navbar-inner">
		<div class="container">
			<ul class="nav">
				<c:choose>
					<c:when test="${page eq 'home'}">
						<li class="active"><a href="<c:url value="/" />"><i class="icon-home"></i> Home</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="<c:url value="/" />"><i class="icon-home"></i> Home</a></li>
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${empty user}">
						<li><a href="#"><i class="icon-info-sign"></i> About Us</a></li>
						<li><a href="#"><i class="icon-user"></i> Top Helpful Users</a></li>
						<li><a href="#"><i class="icon-download"></i> Android Version</a></li>
						<li><a href="#"><i class="icon-question-sign"></i> Contact Us</a></li>
						<li><a href="#"><i class="icon-search"></i> Advanced Search</a></li>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${page eq 'myprofile'}">
								<li class="active"><a href="<c:url value="/profile" />"><i class="icon-plus"></i> My Profile</a></li>
							</c:when>
							<c:otherwise>
								<li><a href="<c:url value="/profile" />"><i class="icon-plus"></i> My Profile</a></li>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${page eq 'createCommunity'}">
								<li class="active"><a href="<c:url value="/createCommunity" />"><i class="icon-plus"></i> Create Community</a></li>
							</c:when>
							<c:otherwise>
								<li><a href="<c:url value="/createCommunity" />"><i class="icon-plus"></i> Create Community</a></li>
							</c:otherwise>
						</c:choose>
						<li><a href="#"><i class="icon-user"></i> Top Helpful Users</a></li>
						<li><a href="#"><i class="icon-search"></i> Advanced Search</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
			
			<ul class="nav pull-right onairuserforms">
				<c:choose>
					<c:when test="${empty user}">
						<li class="dropdown">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#">Sign Up <strong class="caret"></strong></a>
							<div class="dropdown-menu" style="padding: 20px;">
								<form class="form-horizontal" id="dropdownSignupForm" action="<c:url value="/signup" />" method="POST">
									<div class="control-group">
										<div class="input-prepend">
										  <span class="add-on">Username: </span>
										  <input class="span2" id="signupFormUsername" type="text" placeholder="Username">
										</div>
									</div>
									
									<div class="control-group">
										<div class="input-prepend">
										  <span class="add-on">Email: </span>
										  <input class="span2" id="signupFormEmail" type="text" placeholder="Email">
										</div>
									</div>
									
									<div class="control-group">
										<div class="input-prepend">
										  <span class="add-on">Password: </span>
										  <input class="span2" id="signupFormPassword" type="password" placeholder="Password">
										</div>
									</div>
									
									<div class="control-group">
										<button type="submit" class="btn btn-info">Sign up</button>
									</div>
								</form>
							</div>
						</li>
						<li class="dropdown">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#">Login <strong class="caret"></strong></a>
							<div class="dropdown-menu" style="padding: 20px;">
								<form class="form-horizontal" id="dropdownLoginForm" action="<c:url value="/login" />" method="post">
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
										<button type="submit" class="btn btn-info">Login</button>
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