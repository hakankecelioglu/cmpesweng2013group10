<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="row">
	<div class="well">
			You can search according to tasks or communities!
	</div>
	<div class="span6">
		
		<form class="form-search">
			<label class="control-label" for="ASDD">Search in Tasks</label>
  			<input type="text" class="input-medium search-query">
  			<button type="submit" class="btn">Search</button>
		</form>
		<div style="height:300px">
		</div>
	
	</div>
	<div class="span6">
		
		<form class="form-search">
			<label class="control-label" for="ASDD">Search in Communities</label>
  			<input type="text" class="input-medium search-query">
  			<button type="submit" class="btn">Search</button>
		</form>
	</div>
</div>