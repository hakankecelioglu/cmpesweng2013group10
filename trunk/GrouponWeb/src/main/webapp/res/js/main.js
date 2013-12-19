var UrlParameters = function() {
	// This function is anonymous, is executed immediately and
	// the return value is assigned to QueryString!
	var query_string = {};
	var query = window.location.search.substring(1);
	var vars = query.split("&");
	for ( var i = 0; i < vars.length; i++) {
		var pair = vars[i].split("=");
		// If first entry with this name
		if (typeof query_string[pair[0]] === "undefined") {
			query_string[pair[0]] = pair[1];
			// If second entry with this name
		} else if (typeof query_string[pair[0]] === "string") {
			var arr = [ query_string[pair[0]], pair[1] ];
			query_string[pair[0]] = arr;
			// If third or later entry with this name
		} else {
			query_string[pair[0]].push(pair[1]);
		}
	}
	return query_string;
}();

$(function() {
	window.GrouponUtils = {
		siteBase: "http://" + window.location.host + $("#siteBaseUrl").val(),
		
		communityPage: function (id) {
			return GrouponUtils.siteBase + 'community/' + id;
		},
		
		createTaskPage: function (communityId, taskTypeId) {
			var link = GrouponUtils.siteBase + 'task/create?communityId=' + communityId;
			if (taskTypeId) {
				link += "&taskType=" + taskTypeId;
			}
			return link;
		},
		
		taskPage: function (id) {
			return GrouponUtils.siteBase + 'task/show/' + id;
		},
		
		followTask: function (taskId) {
			var url = GrouponUtils.siteBase + 'task/followTask';
			var data = { taskId: taskId };
			return $.post(url, data);
		},
		
		unfollowTask: function (taskId) {
			var url = GrouponUtils.siteBase + 'task/unfollowTask';
			var data = { taskId: taskId };
			return $.post(url, data);
		},
		
		getReplyForm: function (taskId) {
			var url = GrouponUtils.siteBase + 'task/getReplyForm';
			var data = {taskId: taskId};
			return $.get(url, data);
		},
		
		getTaskReplies: function (taskId) {
			var url = GrouponUtils.siteBase + 'task/replies?taskId=' + taskId;
			return $.get(url);
		},
		
		communityPicture: function (name) {
			if (name) {
				return GrouponUtils.siteBase + 'community/picture/' + name;
			}
			return GrouponUtils.siteBase + 'res/img/default_com_picture.jpg';
		},
		
		communityThumb: function (name, size) {
			if (!name) {
				return GrouponUtils.siteBase + 'res/img/default_com_picture.jpg';
			}
			if (size && size == 'medium') {
				return GrouponUtils.siteBase + 'community/thumb/medium/' + name;
			}
			return GrouponUtils.siteBase + 'community/thumb/small/' + name;
		},
		
		modalError: function (msg) {
			$("#errorModal .error-body").html(msg);
			$('#errorModal').modal();
		},
		
		ajaxModalError: function (jqxhr) {
			var msg;
			try {
				var jsonErr = JSON.parse(jqxhr.responseText);
				if (jsonErr.error) {
					msg = jsonErr.error;
				} else {
					msg = "An error occured! Please try again later.";
				}
			} catch (e) {
				msg = "An error occured! Please try again later.";
			}
			
			GrouponUtils.modalError(msg);
		},
		
		validateEmail: function (email) {
			var regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
			return String(email).search(regex) != -1;
		},
		
		followerCount: function (i) {
			if (i <= 0) {
				return 'No follower';
			} else if (i == 1) {
				return '1 follower';
			} else {
				return i + ' follower';
			}
		}
	};
	
	// Setup drop down menu
	$('.dropdown-toggle').dropdown();

	// Fix input element click problem
	$('#dropdownLoginForm, #dropdownSignupForm').closest('.dropdown-menu').click(function(e) {
		e.stopPropagation();
	});
	
	$("#dropdownLoginForm").submit(function () {
		var username = $("#loginFormUsername").val();
		var password = $("#loginFormPassword").val();
		
		if (!username) {
			GrouponUtils.modalError("Please enter username!");
			return false;
		}
		
		if (!password) {
			GrouponUtils.modalError("Please enter password!");
			return false;
		}
		
		var userData = {};
		userData.username = username;
		userData.password = password;
		
		var url = $(this).attr("action");
		
		$.post(url, userData, function (resp) {
			window.location.reload();
		}).fail(GrouponUtils.ajaxModalError);
		
		return false;
	});
	
	$("#dropdownSignupForm").submit(function () {
		var username = $("#signupFormUsername").val();
		if (!username) {
			GrouponUtils.modalError("Please provide a username!");
			return false;
		}
		
		var email = $("#signupFormEmail").val();
		if (!email) {
			GrouponUtils.modalError("Please provide an email address!");
			return false;
		}
		
		if (!GrouponUtils.validateEmail(email)) {
			GrouponUtils.modalError("Please provide a valid email address!");
			return false;
		}
		
		var password = $("#signupFormPassword").val();
		if (!password) {
			GrouponUtils.modalError("Please provide a password!");
			return false;
		}
		
		var url = $(this).attr("action");
		var userData = {};
		userData.email = email;
		userData.username = username;
		userData.password = password;
		
		$.post(url, userData, function (resp) {
			window.location.reload();
		}).fail(GrouponUtils.ajaxModalError);
		
		return false;
	});
});