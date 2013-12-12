$(function () {
	var scope = {
		communityId: $("#communityId").val(),
		
		taskTypes: {
			list: [],
			status: 'NA'
		},
		
		getSimiliarCommunities: function (communityId, page, max) {
			var data = {};
			data.page = page;
			data.max = max;
			data.communityId = communityId;
			
			var url = GrouponUtils.siteBase + "getSimiliarCommunities";
			
			return $.post(url, data).success(function (res) {
				if (res.communities && res.communities.length > 0) {
					var container = $(".similiar-communities-body");
					$.each(res.communities, function (i, community) {
						var $ctnr = $('<div class="media"></div>');
						var $imgLink = $('<a class="pull-left" href="' + GrouponUtils.communityPage(community.id) + '"></a>');
						imgUrl = GrouponUtils.communityThumb(community.picture, 'small');
						var $img = $('<img class="media-object nav-user-thumb" src="' + imgUrl + '" style="width: 32px; height: 32px;">');
						$img.appendTo($imgLink);
						$imgLink.appendTo($ctnr);
						var $head = $('<div class="media-body"></div>').append('<h4 class="media-heading">' + community.name + '</h4>');
						$head.appendTo($ctnr);
						$ctnr.appendTo(container);
					});
				}
			}).fail(function () {
				console.log("error getting similiar communities");
			});
		},
		
		getTaskTypes: function () {
			var data = {};
			data.communityId = scope.communityId;
			
			var url = GrouponUtils.siteBase + "community/taskTypes";
			
			$.get(url, data).success(function (res) {
				if (res.taskTypes && res.taskTypes.length) {
					$.each(res.taskTypes, function (i, e) {
						$("#taskTypeSelection").append('<option value="' + e.id + '">' + e.name + '</option>');
					});
				}
			}).fail(function () {
				alert("fail");
			}).always(function () {
				$("#taskTypeSelectionModal").modal('show');
			});
		},
		
		getCommunityMembers: function () {
			var data = {};
			data.communityId = scope.communityId;
			
			var url = GrouponUtils.siteBase + "community/" + data.communityId + "/members";
			
			$.get(url, data).success(function (res) {
				if (res.members && res.members.length > 0) {
					$.each(res.members, function (i, e) {
						//$("#taskTypeSelection").append('<option value="' + e.id + '">' + e.name + '</option>');
					});
				}
			}).fail(function () {
				alert("fail");
			}).always(function () {
				//$("#taskTypeSelectionModal").modal('show');
			});
		}
	};
	
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
	
	//enables tab switching
    $('#tabList .tabListLink').click(function (e) {
    	e.preventDefault();
    	$(this).tab('show');
    });
    
	
	scope.getSimiliarCommunities(scope.communityId, 0, 10);
	
	$(".createTaskLink").click(function () {
		scope.getTaskTypes();
		return false;
	});
});