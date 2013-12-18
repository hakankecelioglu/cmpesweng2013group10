$(document).ready(function () {
	var isMapOpen = false;
	var createTaskMap = null;
	
	$("#showWhenService").hide();
	var mapOptions = {
		center: new google.maps.LatLng(41, 29),
		zoom: 6,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	
	$("#inputLocation").click(function () {
		if (!isMapOpen) {
			if (createTaskMap == null) {
				createTaskMap = new google.maps.Map(document.getElementById("taskCreateMap"), mapOptions);
			}
			isMapOpen = true;
		}
	});
	
	$("#inputDeadline").datepicker({
		minDate: new Date()
	});
	
	$("#inputTags").select2({
		placeholder: 'Write tags',
		tokenSeparators: [","],
		multiple: true,
		tags: true,
		query: function (query) {
			var data = {results: []};
			$.ajax({
				type: "GET",
				contentType: 'application/json',
				cache: true,
				url: GrouponUtils.siteBase + 'tags/searchTags',
				data: {term: query.term, page: 0},
			    success: function(response) {
					if (response.tags) {
						$.each(response.tags, function (i, tag) {
							data.results.push({id: tag, text: tag});
						});
					}
				}
			}).always(function () {
				query.callback(data);
			});
		},
		createSearchChoice: function (term) {
			return {id: term, text: term};
		}
	});
	
	$("#inputNeedType").change(function(){
		var selected=$('#inputNeedType option:selected').val();
		
		switch(selected){
		case("GOODS"):
			$("#showWhenGoods").show();
			$("#showWhenService").hide();
			break;
		case("SERVICE"):
			$("#showWhenService").show();
			$("#showWhenGoods").hide();
			break;
		case("ONLY_FORM"):
			$("#showWhenGoods").hide();
			$("#showWhenService").hide();
			break;
		}
	});
	
	$("#createTaskButton").click(function () {
		var that = $(this);
		that.attr("disabled", "disabled");
		var task = {};
		task.name = $("#inputName").val();
		task.description = $("#inputDescription").val();
		task.deadline = $("#inputDeadline").val();
		
		if ($("#NEED_TYPE").val() == "USER_DEFINED") {
			task.type = $("#inputNeedType").val();
		} else {
			task.type = $("#NEED_TYPE").val();
		}
		
		task.tags = [];
		
		if(task.type!="ONLY_FORM") {
			if(task.type=="GOODS") {
				task.requirementName=$("#goodName").val();
				task.requirementQuantity=$("#goodQuantity").val();				
			} else{
				task.requirementName=$("#serviceName").val();	
			}
		}

		task.communityId = parseInt($("#communityId").val());
		$.each($("#inputTags").val().split(","), function (i, tag) {
			task.tags.push(tag);
		});
		
		task.attributes = [];
		
		$(".attribute-control-group").each(function () {
			var type = $(this).attr("data-field-type");
			var name = $(this).find(".attribute-label").text();
			var value = "";
			switch(type) {
			case 'SHORT_TEXT':
				value = $(this).find("input.short-text-input").val();
				break;
			case 'LONG_TEXT':
				value = $(this).find("textarea.long-text-input").val();
				break;
			case 'DATE':
				value = $(this).find("input.date-input").val();
				break;
			case 'RADIO':
				value = $(this).find(".radio-input:checked").val();
				break;
			case 'CHECKBOX':
				$(this).find(".checkbox-input:checked").each(function () {
					var val = $(this).val();
					task.attributes.push({name: name, value: val});
				});
				return true;
			case 'SELECT':
				value = $(this).find(".select-input").val();
				break;
			default:
				return true;
			}
			
			task.attributes.push({name: name, value: value});
		});
		
		if (UrlParameters.taskType) {
			var taskTypeId = parseInt(UrlParameters.taskType);
			if (!isNaN(taskTypeId)) {
				task.taskType = parseInt(taskTypeId);
			}
		}
		
		console.log(task);
		
		$.ajax({
			type: "POST",
			contentType: 'application/json',
			url: GrouponUtils.siteBase + 'task/create',
			data: JSON.stringify(task),
		    success: function(response) {
				if (response.taskId) {
					window.location.href = GrouponUtils.siteBase + "task/show/" + response.taskId;
				}
			}
		}).always(function () {
			that.removeAttr("disabled");
		});
	});
	
	$(".date-input").datepicker();
});