$(function () {
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
});
