$(function () {
	var scope = {
		communityId: $("#communityId").val(),
		
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
						imgUrl = GrouponUtils.communityPicture(community.picture);
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
		}
	};
	
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
	
	scope.getSimiliarCommunities(scope.communityId, 0, 10);
	
	
});