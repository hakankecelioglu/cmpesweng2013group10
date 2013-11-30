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
			}).fail(GrouponUtils.ajaxModalError).always(function () {
				that.show();
			});
		}
		return false;
	});
	
	$(document).on('click', '.btn-follow-task', function () {
		var that = $(this);
		var taskId = that.attr("data-taskid");
		if (taskId) {
			that.attr('disabled', 'disabled');
			GrouponUtils.followTask(taskId).done(function (resp) {
				that.removeClass('btn-follow-task btn-success').addClass('btn-unfollow-task btn-danger');
				that.html("Unfollow");
				that.closest(".well").find(".task-follower-count").html(GrouponUtils.followerCount(resp.followerCount));
			}).fail(GrouponUtils.ajaxModalError).always(function () {
				that.removeAttr('disabled');
			});
		}
		return false;
	});
	
	$(document).on('click', '.btn-unfollow-task', function () {
		var that = $(this);
		var taskId = that.attr("data-taskid");
		if (taskId) {
			that.attr('disabled', 'disabled');
			GrouponUtils.unfollowTask(taskId).done(function (resp) {
				that.removeClass('btn-unfollow-task btn-danger').addClass('btn-follow-task btn-success');
				that.html("Follow");
				that.closest(".well").find(".task-follower-count").html(GrouponUtils.followerCount(resp.followerCount));
			}).fail(GrouponUtils.ajaxModalError).always(function () {
				that.removeAttr('disabled');
			});
		}
		return false;
	});
	
	$(".sort-selection-menu li a").click(function () {
		var sort = $(this).attr("data-sort");
		var data = {sortBy: sort};
		var url = GrouponUtils.siteBase + 'setSorting';
		$.post(url, data).success(function (data) {
			window.location.reload();
		});
		$(".sort-selection-item span").html("SORT: " + sort);
		return false;
	});
	
	$(".sort-selection-item").click(function () {
		if ($(".sort-selection-menu").is(":visible")) {
			$(".sort-selection-menu").hide();
		} else {
			$(".sort-selection-menu").show();
		}
		return false;
	});
	
	$(document).on('click', function (e) {
		var t = $(e.target);
		if (t.not('.sort-selection-menu') || t.closest('.sort-selection-menu').length == 0) {
			$(".sort-selection-menu").hide();
		}
	});
	
	scope.loadCommunitiesOfUser(0, 10);
	scope.loadSuggestions(0, 10);
});