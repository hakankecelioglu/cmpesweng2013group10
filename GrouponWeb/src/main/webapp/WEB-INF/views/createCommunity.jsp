<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="row">
	<div class="span12">
		<div class="well">
			You can create a community like diamonds in the sky!
		</div>

		<form class="form-horizontal" id="createCommunityForm" action="<c:url value="/createCommunity" />">
			<div class="control-group">
				<label class="control-label" for="inputEmail">Name</label>
				<div class="controls">
					<input class="span4" type="text" id="inputName" placeholder="ex: Van Earthquake">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="inputDescription">Description</label>
				<div class="controls">
					<textarea class="span4" id="inputDescription" rows="10" cols="100"></textarea>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="inputPhoto">Community Picture</label>
				<div class="controls">
					<input class="span4" type="file" id="inputPhoto" >
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="inputTags">Tags</label>
				<div class="controls">
					<input class="span4" type="text" id="inputTags" placeholder="">
				</div>
			</div>
			
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn btn-success" id="createCommunityBtn">Create Community</button>
				</div>
			</div>
		</form>
	</div>
</div>