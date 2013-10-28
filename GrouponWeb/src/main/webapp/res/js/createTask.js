$(document).ready(function () {
	var isMapOpen = false;
	var createTaskMap = null;
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
	
	$("#createTaskButton").click(function () {
		var that = $(this);
		that.attr("disabled", "disabled");
		var task = {};
		task.name = $("#inputName").val();
		task.description = $("#inputDescription").val();
		task.deadline = $("#inputDeadline").val();
		task.type = $("#inputNeedType").val();
		task.tags = [];
		task.communityId = parseInt($("#communityId").val());
		$.each($("#inputTags").val().split(","), function (i, tag) {
			task.tags.push(tag);
		});
		
		$.ajax({
			type: "POST",
			contentType: 'application/json',
			url: GrouponUtils.siteBase + 'task/create',
			data: JSON.stringify(task),
		    success: function(response) {
				if (response.taskId) {
					window.location.href = GrouponUtils.siteBase + "task/" + response.taskId;
				}
			}
		}).always(function () {
			alert("always");
			that.removeAttr("disabled");
		});
	});
});