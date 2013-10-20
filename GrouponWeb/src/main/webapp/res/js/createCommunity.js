$(document).ready(function () {
	$("#createCommunityBtn").click(function () {
		var name = $("#inputName").val();
		var description = $("#inputDescription").val();
		
		var communityData = {
			name: name,
			description: description
		};
		
		$.ajax({
			type: "POST",
			contentType: 'application/json',
			url: '/createCommunity',
			data: JSON.stringify(communityData),
		    success: function(response) {
				if (response.message == "OK" && response.communityId) {
					window.location.href = "http://" + window.location.host + "/community/" + response.communityId;
				} else {
					alert("err");
				}
			},
		    error: function(response){
		    	alert("err");
			}
		});
		
		return false;
	});
});