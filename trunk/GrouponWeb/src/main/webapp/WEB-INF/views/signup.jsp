<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="row">
	<div class="span12">
		<div class="well">
			Signup now!
		</div>

		<form class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="inputEmail">Name</label>
				<div class="controls">
					<input class="span4" type="text" id="inputName" placeholder="ex: Van Earthquake">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="inputPassword">Description</label>
				<div class="controls">
					<textarea class="span4" id="inputDescription" rows="10" cols="100"></textarea>
				</div>
			</div>
			
			<div class="control-group">
				<div class="controls">
					<button type="button" class="btn btn-success" id="createCommunityBtn">Create Community</button>
				</div>
			</div>
		</form>
	</div>
</div>