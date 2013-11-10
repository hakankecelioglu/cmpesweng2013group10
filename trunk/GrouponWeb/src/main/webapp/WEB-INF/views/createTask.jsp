<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<ul class="breadcrumb">
	<li><a href="<c:url value="/" />">Home</a> <span class="divider">/</span></li>
	<li><a href="<c:url value="/community/${community.id}" />">${community.name}</a> <span class="divider">/</span></li>
	<li class="active">Create Task</li>
</ul>

<div class="row">
	<div class="span7">
		<form class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="inputEmail">Name</label>
				<div class="controls">
					<input class="span4" type="text" id="inputName" placeholder="ex: Send Tents">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="inputDescription">Description</label>
				<div class="controls">
					<textarea class="span4" id="inputDescription" rows="10" cols="100"></textarea>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="inputDeadline">Deadline</label>
				<div class="controls">
					<input class="span4" type="text" id="inputDeadline" placeholder="28.10.2013">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="inputLocation">Location</label>
				<div class="controls">
					<button type="button" class="btn btn-inverse" id="inputLocation">Choose</button>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="inputNeedType">Type of need</label>
				<div class="controls">
					<select id="inputNeedType" class="span4">
						<option value="GOODS">Goods</option>
						<option value="SERVICE">Service</option>
						<option value="ONLY_FORM">Only Form</option>
					</select>
				</div>

			</div>
			<div id="showWhenGoods" class="control-group">
					<div class="control-group">
						<label class="control-label" for="goodName">Name</label>
						<div class="controls">
							<input class="span1" type="text" id="goodName">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="goodQuantity">Quantity</label>
						<div class="controls">
							<input class="span1" type="text" id="goodQuantity">
						</div>
					</div>						
			</div>
			
			<div id="showWhenService" class="control-group">
					<div class="control-group">
						<label class="control-label" for="serviceName">Name</label>
						<div class="controls">
							<input class="span1" type="text" id="serviceName">
						</div>
					</div>				
			</div>			
			
			<div class="control-group">
				<label class="control-label" for="inputAddExtraForm">Add Extra Form Field</label>
				<div class="controls">
					<div class="clearfix" style="margin-bottom: 10px">
						<label class="pull-left" for="inputAddExtraForm" style="width: 50px;">Type: </label>
						<select id="inputAddExtraForm" class="span3">
							<option>Text Input</option>
							<option>Textarea</option>
							<option>Photo</option>
							<option>Multiple Choice</option>
							<option>Single Choice</option>
							<option>Optional Set</option>
						</select>
					</div>
						
					<div class="clearfix">
						<label class="pull-left" for="inputAddExtraFormLabel" style="width: 50px;">Label: </label>
						<input class="span3" type="text" id="inputAddExtraFormLabel" placeholder="">
					</div>
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
					<button type="button" class="btn btn-success" id="createTaskButton">Create Task</button>
				</div>
			</div>
			
			<input type="hidden" value="${community.id}" id="communityId" />
		</form>
	</div>
	
	<div class="span5">
		<div class="mapContainer" id="taskCreateMap" style="width: 100%; height: 400px;"></div>
	</div>
</div>

<script type="text/javascript" src="//maps.googleapis.com/maps/api/js?sensor=false&libraries=visualization"></script>