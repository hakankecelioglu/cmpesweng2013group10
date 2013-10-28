$(document).ready(function () {
	$("#createCommunityForm").submit(function () {
		var name = $("#inputName").val();
		var description = $("#inputDescription").val();
		
		var data = new FormData();
		data.append("name", name);
		data.append("description", description);
		
		$.each($("#inputPhoto")[0].files, function (i, file) {
			data.append("file", file);
			return false;
		});
		
		var url = $(this).attr('action');
		
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
		
		return false;
	});
});