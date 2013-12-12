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
			
			<c:choose>
				<c:when test="${not empty taskType}">
					<c:forEach var="taskTypeField" items="${taskType.fields}" varStatus="status">
						<c:set value="taskTypeField-${status.index}" var="fieldId"></c:set>
					
						<div class="control-group attribute-control-group" data-field-type="${taskTypeField.fieldType}">
							<label class="control-label attribute-label" for="${fieldId}">${taskTypeField.name}</label>
							<div class="controls">
								<c:choose>
									<c:when test="${taskTypeField.fieldType == 'SHORT_TEXT'}">
										<input type="text" id="${fieldId}" class="short-text-input span4" data-type="SHORT_TEXT" />
									</c:when>
									<c:when test="${taskTypeField.fieldType == 'LONG_TEXT'}">
										<textarea id="${fieldId}" class="long-text-input span4" data-type="LONG_TEXT"></textarea>
									</c:when>
									<c:when test="${taskTypeField.fieldType == 'RADIO'}">
										<c:forEach var="attribute" items="${taskTypeField.attributes}">
											<c:if test="${fn:startsWith(attribute.name, 'option')}">
												<label class="radio">
													<input type="radio" name="${fieldId}" class="radio-input" value="${attribute.value}" />
													${attribute.value}
												</label>
											</c:if>
										</c:forEach>
									</c:when>
									<c:when test="${taskTypeField.fieldType == 'DATE'}">
										<input type="text" id="${fieldId}" class="date-input span4" />
									</c:when>
									<c:when test="${taskTypeField.fieldType == 'CHECKBOX'}">
										<c:forEach var="attribute" items="${taskTypeField.attributes}">
											<c:if test="${fn:startsWith(attribute.name, 'option')}">
												<label class="checkbox">
													<input type="checkbox" name="${fieldId}" class="checkbox-input" value="${attribute.value}" />
													${attribute.value}
												</label>
											</c:if>
										</c:forEach>
									</c:when>
									<c:when test="${taskTypeField.fieldType == 'SELECT'}">
										<select id="${fieldId}" class="select-input span4">
											<c:forEach var="attribute" items="${taskTypeField.attributes}">
												<c:if test="${fn:startsWith(attribute.name, 'option')}">
													<option value="${attribute.value}">${attribute.value}</option>
												</c:if>
											</c:forEach>
										</select>
									</c:when>
								</c:choose>
							</div>
						</div>
					</c:forEach>
					
					<input type="hidden" id="NEED_TYPE" value="${taskType.needType}" />
					
					<c:choose>
						<c:when test="${taskType.needType == 'GOODS'}">
							<div class="control-group">
								<label class="control-label" for="goodName">What is the need?</label>
								<div class="controls">
									<input class="span4" type="text" id="goodName">
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="goodQuantity">What is the required quantity of your need?</label>
								<div class="controls">
									<input class="span4" type="text" id="goodQuantity">
								</div>
							</div>
						</c:when>
						<c:when test="${taskType.needType == 'SERVICE'}">
							<div class="control-group">
								<label class="control-label" for="serviceName">What service do you need?</label>
								<div class="controls">
									<input class="span4" type="text" id="serviceName">
								</div>
							</div>
						</c:when>
					</c:choose>
				</c:when>
				<c:otherwise>
					<input type="hidden" id="NEED_TYPE" value="USER_DEFINED" />
					
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
				</c:otherwise>
			</c:choose>
			
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