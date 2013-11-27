$(document).ready(function () {
	function loadCommunitiesOfUser(page, max) {
		var url = GrouponUtils.siteBase + "";
		$.ajax({
			type: "POST",
			contentType: false,
			cache: false,
			url: url,
			processData: false,
			data: data,
		    success: function(response) {
				if (response.message == "OK" && response.communityId) {
					window.location.href = GrouponUtils.siteBase + "community/" + response.communityId;
				} else {
					alert("err");
				}
			},
		    error: function(response){
		    	alert("err");
			}
		});
	}
	
	loadCommunitiesOfUser(0, 10);
});