$(document).ready(function () {
	var scope = {
		
		loadCommunitiesOfUser: function (page, max) {
			var url = GrouponUtils.siteBase + "getCommunitiesOfUser";
			
			var data = {};
			
			$.ajax({
				type: "POST",
				contentType: false,
				cache: false,
				url: url,
				processData: false,
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
						alert("err no item");
					}
				},
			    error: function(response){
			    	alert("err");
				}
			});
		},
		
		
	};
	
	scope.loadCommunitiesOfUser(0, 10);
});