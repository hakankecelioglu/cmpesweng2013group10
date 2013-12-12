<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="navbar navbar-inverse navbar-fixed-bottom footer-nav">
	<div class="navbar-inner">
		<div class="container">
			<ul class="nav">
				<li class="active"><a href="#"><i class="icon-home"></i> Home</a></li>
				<li><a href="<c:url value="/aboutUs" />"><i class="icon-info-sign"></i> About Us</a></li>
				<li><a href="#"><i class="icon-user"></i> Top Helpful Users</a></li>
				<li><a href="<c:url value="/androidDown" />"><i class="icon-download"></i> Android Version</a></li>
				<li><a href="<c:url value="/contactUs" />"><i class="icon-question-sign"></i> Contact Us</a></li>
				<li><a href="<c:url value="/search" />"><i class="icon-search"></i> Advanced Search</a></li>
			</ul>
		</div>
	</div>
</div>

<div id="errorModal" class="modal hide fade">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3>Upps! An error occured!</h3>
	</div>
	<div class="modal-body">
		<p class="error-body"></p>
	</div>
	<div class="modal-footer">
		<a href="#" data-dismiss="modal" class="btn btn-info">Close</a>
	</div>
</div>