<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="row">
	<div class="well">
		You can search according to tasks or communities!<br>
	    You should log in to search!
	</div>
	
	<div class="span6">
		<form class="form-search" method="get" action="<c:url value="/task/search" />">
			<label class="control-label" for="taskQueryText">Search in Tasks</label>
  			<input type="text" class="input-medium search-query" name="q" id="taskQueryText" />
  			<button type="submit" class="btn">Search</button>
		</form>
		<div style="height:300px"></div>
	</div>
	
	<div class="span6">
		<form class="form-search" method="get" action="<c:url value="/community/search" />">
			<label class="control-label" for="communityQueryText">Search in Communities</label>
  			<input type="text" class="input-medium search-query" name="q" id="communityQueryText" />
  			<button type="submit" class="btn">Search</button>
		</form>
	</div>
</div>