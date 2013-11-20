<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<ul class="breadcrumb">
	<li><a href="<c:url value="/" />">Home</a> <span class="divider">/</span></li>
	<li><a href="<c:url value="/community/${community.id}" />">${community.name}</a> <span class="divider">/</span></li>
	<li class="active">Create Task Type</li>
</ul>

<div class="row">
	<div class="span4">
		<div class="well clearfix">
			<h4 class="text-success">Define a Task Type for your community!</h4>
			<p class="text-info">This will let you help task creators while they are creating a specific type of task.</p>
			
			<label class="text-error" for="taskTypeName">Name your type:</label> 
			<input class="span3" id="taskTypeName" type="text" placeholder="Task type name">
			
			<label class="text-error" for="taskTypeDesc">Write a description:</label> 
			<textarea class="span3" id="taskTypeDesc" placeholder="Task type description"></textarea>
			
			<h4 class="text-success">Add Extra Form Fields</h4>
			<p class="text-info">You can add any type of and any number of form fields like text, date, photo, etc... </p>
			<button type="button" id="popoverExtraInput" class="btn btn-large btn-success span3 no-margin-left"><i class="icon-plus"></i> Add</button>
		</div>
	</div>
	
	<div class="span7">
		<div class="well clearfix">
			<h3>Preview</h3>
			<p class="text-success">Edit, move, or delete fields of your task type below.</p>
		</div>
		
		<div class="well clearfix" id="taskTypeFields">
			<p class="text-error" id="noFieldText">Currently, there is no field in your task type. Use the add button at the left hand side of this page.</p>
		</div>
		
		<div class="well clearfix">
			<button class="btn-large btn btn-success span6" type="button" id="createTaskType">Create Task Type</button>
		</div>
	</div>
</div>

<div id="inputTypeForm" style="display: none;">
	<div class="popover-add-item">
		<table class="table input-type-table">
			<tr class="info">
				<td>
					<button title="Single line text input" class="btn btn-info span2 no-margin-left addSingleText">
						<i class="sprite-icon sprite-icon-text"></i>Text
					</button>
				</td>
				<td>
					<button title="Multiple line text input" class="btn btn-info span2 no-margin-left addMultipleText">
						<i class="sprite-icon sprite-icon-paragraph"></i>Paragraph Text
					</button>
				</td>
			</tr>
			<tr class="info">
				<td>
					<button title="Let them choose one of the multiple choices" class="btn btn-info span2 no-margin-left addMultipleChoice">
						<i class="sprite-icon sprite-icon-multiple-choice"></i>Multiple Choice
					</button>
				</td>
				<td>
					<button title="Ask a yes/no question or let them choose some of the choices you provide!" class="btn btn-info span2 no-margin-left addCheckboxes">
						<i class="sprite-icon sprite-icon-checkboxes"></i>Checkboxes
					</button>
				</td>
			</tr>
			<tr class="info">
				<td>
					<button title="Let them choose their options from a list of existing options by scrolling." class="btn btn-info span2 no-margin-left addDropdown">
						<i class="sprite-icon sprite-icon-dropdown"></i>Drop Down
					</button>
				</td>
				<td>
					<button title="Provide them a beautiful date picker to choose a date, of course!" class="btn btn-info span2 no-margin-left addDate">
						<i class="sprite-icon sprite-icon-date"></i>Date
					</button>
				</td>
			</tr>
			<tr class="info">
				<td>
					<button title="A single line input field which can be filled with only integral numbers." class="btn btn-info span2 no-margin-left">
						<i class="sprite-icon sprite-icon-integer"></i>Integral Number
					</button>
				</td>
				<td>
					<button title="A single line input field which can be filled with only decimal numbers." class="btn btn-info span2 no-margin-left">
						<i class="sprite-icon sprite-icon-decimal-number"></i>Decimal Number
					</button>
				</td>
			</tr>
			<tr class="info">
				<td>
					<button title="Let them upload a photo for their tasks!" class="btn btn-info span2 no-margin-left">
						<i class="sprite-icon sprite-icon-image"></i>Image
					</button>
				</td>
				<td>
					<button title="A single line input field which can be filled with a phone number" class="btn btn-info span2 no-margin-left">
						<i class="sprite-icon sprite-icon-phone-number"></i>Phone Number
					</button>
				</td>
			</tr>
			<tr class="info">
				<td colspan="2">
					<button title="Show them a map and let them choose a location!" class="btn btn-info span2 no-margin-left">
						<i class="sprite-icon sprite-icon-location-map"></i>Location (Map)
					</button>
				</td>
			</tr>
		</table>
	</div>
</div>