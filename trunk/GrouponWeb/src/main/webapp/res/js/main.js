$(function() {
	window.GrouponUtils = {
		siteBase: "http://" + window.location.host + $("#siteBaseUrl").val(),
		
		communityPage: function (id) {
			return GrouponUtils.siteBase + 'community/' + id;
		},
		
		taskPage: function (id) {
			return GrouponUtils.siteBase + 'task/show/' + id;
		},
		
		followTask: function (taskId) {
			var url = GrouponUtils.siteBase + 'task/followTask';
			var data = { taskId: taskId };
			return $.post(url, data);
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
			alert("Please enter username!");
			return false;
		}
		
		if (!password) {
			alert("Please enter password!");
			return false;
		}
		
		var userData = {};
		userData.username = username;
		userData.password = password;
		
		var url = $(this).attr("action");
		
		$.post(url, userData, function (resp) {
			if (resp.message == "OK") {
				window.location.reload();
			} else {
				alert(resp.message);
			}
		}).fail(function (err) {
			try {
				var jsonErr = JSON.parse(err.responseText);
				alert(jsonErr.error);
			} catch (e) {
				alert("An error occured!");
			}
		});
		
		return false;
	});
	
	$("#dropdownSignupForm").submit(function () {
		var username = $("#signupFormUsername").val();
		if (!username) {
			alert("Please provide a username!");
			return;
		}
		
		var email = $("#signupFormEmail").val();
		if (!email) {
			alert("Please provide an email address!");
			return;
		}
		
		var password = $("#signupFormPassword").val();
		if (!password) {
			alert("Please provide a password!");
			return;
		}
		
		var url = $(this).attr("action");
		var userData = {};
		userData.email = email;
		userData.username = username;
		userData.password = password;
		
		$.post(url, userData, function (resp) {
			if (resp.message == "OK") {
				window.location.reload();
			} else {
				alert(resp.message);
			}
		}).fail(function (err) {
			try {
				var jsonErr = JSON.parse(err.responseText);
				alert(jsonErr.error);
			} catch (e) {
				alert("An error occured!");
			}
		});
		
		return false;
	});
	
});