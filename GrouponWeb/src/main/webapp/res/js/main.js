$(function() {
	// Setup drop down menu
	$('.dropdown-toggle').dropdown();

	// Fix input element click problem
	$('#dropdownLoginForm').click(function(e) {
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
		
		$.post("http://" + window.location.host + "/login", userData, function (resp) {
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