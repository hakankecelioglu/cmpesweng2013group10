$(function () {
	var currentUserId = null;
	
	function showUserInTable(resp) {
		$(".td-uid").html(resp.user.id);
		$(".td-name").html(resp.user.name);
		$(".td-surname").html(resp.user.surname);
		$(".td-username").html(resp.user.username);
		$(".td-email").html(resp.user.email);
		$(".td-role").html(resp.role);
		$(".td-status").html(resp.status);
		
		currentUserId = resp.user.id;
	}

	function getUserById(userid) {
		var url = GrouponUtils.siteBase + "admin/getUserById?userId=" + userid;
		$.get(url, function (resp) {
			showUserInTable(resp);
		}).fail(GrouponUtils.ajaxModalError);
	}
	
	$("#getUserByIdBtn").click(function () {
		var userid = $("#userId").val();
		getUserById(userid);
	});
	
	$("#makeAdmin").click(function () {
		if (currentUserId == null) {
			GrouponUtils.modalError("First get a user!");
			return;
		}
		
		var url = GrouponUtils.siteBase + "admin/makeAdmin?userId=" + currentUserId;
		$.post(url, function () {
			alert("OK");
		}).fail(GrouponUtils.ajaxModalError);
	});
});