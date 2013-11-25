$(document).ready(function () {
	
	$("#inputTags").select2({
		placeholder: 'Write tags',
		tokenSeparators: [","],
		multiple: true,
		tags: true,
		query: function (query) {
			var data = {results: []};
			$.ajax({
				type: "GET",
				contentType: 'application/json',
				cache: true,
				url: GrouponUtils.siteBase + 'tags/searchTags',
				data: {term: query.term, page: 0},
			    success: function(response) {
					if (response.tags) {
						$.each(response.tags, function (i, tag) {
							data.results.push({id: tag, text: tag});
						});
					}
				}
			}).always(function () {
				query.callback(data);
			});
		},
		createSearchChoice: function (term) {
			return {id: term, text: term};
		}
	});
	
	$("#createCommunityForm").submit(function () {
		var name = $("#inputName").val();
		var description = $("#inputDescription").val();
		
		var data = new FormData();
		data.append("name", name);
		data.append("description", description);
		
		var tags = [];
		$.each($("#inputTags").val().split(","), function (i, tag) {
			tags.push(tag);
		});
		data.append("tags", JSON.stringify(tags));
		
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