$(function () {
	$(document).on('click', '#followTask', function () {
		var that = $(this);
		that.attr("disabled", "disabled");
		var taskId = that.attr("data-taskid");
		
		$.ajax({
			type: "POST",
			url: GrouponUtils.siteBase + 'task/followTask',
			data: {taskId: taskId},
		    success: function(response) {
				if (response.followerCount) {
					alert(response.followerCount);
					that.attr("id", "unfollowTask");
				}
			}
		}).always(function () {
			that.removeAttr("disabled");
		});
	});
});