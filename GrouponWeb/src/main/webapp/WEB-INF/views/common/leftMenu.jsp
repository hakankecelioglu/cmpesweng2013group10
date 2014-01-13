<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="span2" id="home-left-frame">
	<h4 class="small-heading">Filter</h4>
	<ul class="nav nav-tabs nav-stacked">
		<li><a href="<c:url value="/" />">Home Feed</a></li>
		<li><a href="<c:url value="/communities/newest" />">Newest Communities</a></li>
		<li><a href="<c:url value="/tasks/newest" />">Newest Tasks</a></li>
	</ul>
	
	<div class="h-user-communities" style="display: none;">
		<h4 class="small-heading">Communities</h4>
		<ul class="nav nav-tabs nav-stacked"></ul>
	</div>
</div>