$(document).ready(function () {
	var scope = {
		
		loadCommunitiesOfUser: function (page, max) {
			var url = GrouponUtils.siteBase + "getCommunitiesOfUser";
			
			var data = {};
			
			if (!isNaN(page) && !isNaN(max)) {
				data.page = page;
				data.max = max;
			}
			
			$.ajax({
				type: "POST",
				url: url,
				data: data,
			    success: function(response) {
					if (response.communities && response.communities.length > 0) {
						$(".h-user-communities").show();
						var communities = response.communities;
						var $ul = $(".h-user-communities ul");
						$.each(communities, function (i, community) {
							var $li = $('<li><a href="' + GrouponUtils.communityPage(community.id) + '">' + community.name + '</a></li>');
							$li.appendTo($ul);
						});
					} else {
						// TODO
					}
				},
			    error: function(response){
			    	// TODO
				}
			});
		},
		
		loadSuggestions: function (page, max) {
			var url = GrouponUtils.siteBase + "task/suggest";
			
			var data = {};
			
			if (!isNaN(page) && !isNaN(max)) {
				data.page = page;
				data.max = max;
			}
			
			$.ajax({
				type: "GET",
				url: url,
				data: data,
			    success: function(response) {
					if (response.tasks && response.tasks.length > 0) {
						var tasks = response.tasks;
						var $container = $(".h-suggestions div.list");
						$.each(tasks, function (i, task) {
							var $item = $('<div class="list-item"></div>');
							$('<a />').html(task.title).attr('href', GrouponUtils.taskPage(task.id)).appendTo($item);
							$('<p />').html(task.description).appendTo($item);
							$('<a href="#" />').html('Follow Task').addClass('rli-follow-task').attr('data-taskid', task.id).appendTo($item);
							$item.appendTo($container);
						});
					} else {
						$(".no-suggestion-info").show();
					}
				},
			    error: function(response){
			    	$(".no-suggestion-info").removeClass("text-info").addClass("text-error").show().html("An error occured!");
				}
			});
		}
	};
	
	$(document).on('click', '.rli-follow-task', function () {
		var that = $(this);
		that.hide();
		var taskId = that.attr('data-taskid');
		if (taskId) {
			GrouponUtils.followTask(taskId).done(function (resp) {
				if (resp.followerCount) {
					that.closest('div.list-item').remove();
				}
			}).fail(function (jxhr) {
				alert("An error occured!");
			}).always(function () {
				that.show();
			});
		}
		return false;
	});
	
	scope.loadCommunitiesOfUser(0, 10);
	scope.loadSuggestions(0, 10);
});