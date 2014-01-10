$(function () {
	var scope = {
		getNotifications: function (page, max) {
			var url = GrouponUtils.siteBase + 'notification/get';
			var data = {page: page, max: max};
			return $.post(url, data);
		},
		
		getNotificationCount: function () {
			var url = GrouponUtils.siteBase + 'notification/count';
			$.post(url).success(function (data) {
				if (data.count > 0) {
					$("#notifications .badge").html(data.count).show();
				} else {
					$("#notifications .badge").hide();
				}
			});
		},
		
		timeparts: [
			{name: 'year', div: 31536000000, mod: 10000},
			{name: 'day', div: 86400000, mod: 365},
			{name: 'hour', div: 3600000, mod: 24},
			{name: 'minute', div: 60000, mod: 60},
			{name: 'second', div: 1000, mod: 60}
		],
		
		timeAgo: function (comparisonDate) {
            var i = 0, l = scope.timeparts.length, calc, values = [], 
            	interval = new Date().getTime() - comparisonDate.getTime();
            
            while (i < l) {
               calc = Math.floor(interval / scope.timeparts[i].div) % scope.timeparts[i].mod;
               if (calc) {
                  values.push(calc + ' ' + scope.timeparts[i].name + (calc != 1 ? 's' : ''));
               }
               i += 1;
            }
            
            if (values.length === 0) { values.push('0 seconds'); }
            return values.join(', ') + ' ago';
         }
	};
	
	$("#notifications").click(function () {
		scope.getNotifications(0, 10).success(function (data) {
			var parent = $(".popover .notifications-body").html("");
			if (data.notifications.length == 0) {
				parent.append('There is no notification for you. Sorry :(');
			} else {
				$.each(data.notifications, function (i, notif) {
					var notifContainer = $('<div class="notif-outer"></div>');
					var str = "";
					
					if (notif.type == "TASK_CREATED_IN_FOLLOWED_COMMUNITY") {
						str += 'A <b>new task</b> is opened ';
						str += 'in the community <b>' + notif.community.name + '</b>';
					} else if (notif.type == "TASK_REPLY") {
						str += notif.source.uname + ' has post <b>a reply</b> to <b>' + notif.task.name + '</b>.';
					} else if (notif.type == "TASK_UPVOTE") {
						str += "You got <b>" + notif.reputChange + "</b> reputation from <b>" + notif.task.name + "</b>";
					} else if (notif.type == "TASK_DOWNVOTE") {
						str += "You lost <b>" + notif.reputChange + "</b> reputation from <b>" + notif.task.name + "</b>";
					}
					
					if (!notif.isRead) {
						notifContainer.addClass('new-notif');
					}
					
					notifContainer.append(str);
					notifContainer.append('<div class="clearfix"><i class="sprite2 sprite2-pin"></i><div class="pull-left">' + scope.timeAgo(new Date(notif.date)) + '</div></div>');
					parent.append(notifContainer);
					
					notifContainer.click(function () {
						window.location.href = GrouponUtils.taskPage(notif.task.id);
					});
				});
			}
		});
		
		return false;
	});
	
	$('#notifications').popover({
		placement: 'bottom',
		html: true,
		content: function () {
			return $("#notificationsPanel").html();
		}
	});
	
	
	setInterval(scope.getNotificationCount, 5000);
	scope.getNotificationCount();
});