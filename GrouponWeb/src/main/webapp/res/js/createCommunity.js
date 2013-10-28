$(document).ready(function () {
	$("#createCommunityBtn").click(function () {
		var name = $("#inputName").val();
		var description = $("#inputDescription").val();
		
		var data = new FormData();
		data.append("name", name);
		data.append("description", description);
		
		$.each($("#inputPhoto")[0].files, function (i, file) {
			data.append("file", file);
			return false;
		});
		
		$.ajax({
			type: "POST",
			contentType: false,
			cache: false,
			url: '/createCommunity',
			processData: false,
			data: data,
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