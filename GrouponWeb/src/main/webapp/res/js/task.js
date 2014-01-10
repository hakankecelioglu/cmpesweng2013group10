$(function () {
	var taskId = $("#hiddenTaskId").val();
	
	$(document).on('click', '.btn-follow-task', function () {
		var that = $(this);
		var taskId = that.attr("data-taskid");
		if (taskId) {
			that.attr('disabled', 'disabled');
			GrouponUtils.followTask(taskId).done(function (resp) {
				that.removeClass('btn-follow-task btn-success').addClass('btn-unfollow-task btn-danger');
				that.html("Unfollow");
				that.closest(".well").find(".task-follower-count").html(GrouponUtils.followerCount(resp.followerCount));
			}).fail(GrouponUtils.ajaxModalError).always(function () {
				that.removeAttr('disabled');
			});
		}
		return false;
	});
	
	$(document).on('click', '.btn-unfollow-task', function () {
		var that = $(this);
		var taskId = that.attr("data-taskid");
		if (taskId) {
			that.attr('disabled', 'disabled');
			GrouponUtils.unfollowTask(taskId).done(function (resp) {
				that.removeClass('btn-unfollow-task btn-danger').addClass('btn-follow-task btn-success');
				that.html("Follow");
				that.closest(".well").find(".task-follower-count").html(GrouponUtils.followerCount(resp.followerCount));
			}).fail(GrouponUtils.ajaxModalError).always(function () {
				that.removeAttr('disabled');
			});
		}
		return false;
	});
	
	function getReplyBody(reply) {
		var replyStr = "";
		
		if (reply.attributes) {
			$.each(reply.attributes, function (i, attr) {
				if (attr.name == "TT_RES_QUANTITY") {
					attr.name = "Amount";
				}
				replyStr += '<p>' + attr.name + ": " + attr.value + '</p>';
			});
		}
		
		return replyStr;
	}
	
	GrouponUtils.getTaskReplies(taskId).success(function (resp) {
		$(".replies-container").html("");
		
		$.each(resp.replies, function (i, reply) {
			var clone = $(".reply-item-hidden").clone().removeClass("reply-item-hidden");
			clone.find(".replier-pic").attr("src", GrouponUtils.getUserPicture(reply.replier.picture));
			clone.find(".replier-name .replier-profile").text(reply.replier.username);
			clone.find(".reply-body").html(getReplyBody(reply));
			clone.find(".replier-profile").attr("href", GrouponUtils.userProfilePage(reply.replier.username));
			$(".replies-container").append(clone);
			clone.show();
		});
		
		$(".task-replies-well .loading-anim").hide();
		$(".replies-container").show();
	});
	
	GrouponUtils.getReplyForm(taskId).success(function (resp) {
		var fields = resp.fields;
		var parent = $("#replyForm");
		
		$.each(fields, function (i, field) {
			var fieldId = "reply-field-" + i;
			var formControl = $('<div class="control-group attribute-label" />').attr("data-field-type", field.fieldType);
			
			$('<label class="control-label"></label>').attr('for', fieldId).text(field.name).appendTo(formControl);
			var controls = $('<div class="controls" />').appendTo(formControl);
			
			switch (field.fieldType) {
			case 'SHORT_TEXT':
				$('<input type="text" class="short-text-input span4" data-type="SHORT_TEXT" />').attr("id", fieldId).appendTo(controls);
				break;
			case 'LONG_TEXT':
				$('<textarea class="long-text-input span4" data-type="LONG_TEXT"></textarea>').attr("id", fieldId).appendTo(controls);
				break;
			case 'RADIO':
				$.each(field.attributes, function (i, attr) {
					if (attr.name.lastIndexOf('option', 0) === 0) {
						var optLabel = $('<label class="radio" />');
						var optInput = $('<input type="radio" class="radio-input" />').attr('name', fieldId).attr('value', attr.value);
						optLabel.append(optInput);
						optLabel.append(attr.value);
						optLabel.appendTo(controls);
					}
				});
				break;
			case 'DATE':
				$('<input type="text" class="date-input span4" />').attr('id', fieldId).appendTo(controls);
				break;
			case 'CHECKBOX':
				$.each(field.attributes, function (i, attr) {
					if (attr.name.lastIndexOf('option', 0) === 0) {
						var optLabel = $('<label class="checkbox" />');
						var optInput = $('<input type="checkbox" class="checkbox-input" />').attr('name', fieldId).attr('value', attr.value);
						optLabel.append(optInput);
						optLabel.append(attr.value);
						optLabel.appendTo(controls);
					}
				});
				break;
			case 'SELECT':
				var select = $('<select class="select-input span4" />').attr('id', fieldId);
				$.each(field.attributes, function (i, attr) {
					if (attr.name.lastIndexOf('option', 0) === 0) {
						$('<option />').attr('value', attr.value).text(attr.value).appendTo(select);
					}
				});
				select.appendTo(controls);
				break;
			}
			
			formControl.appendTo(parent);
		});
		
		$(".replyFormContainer").show();
		$(".reply-form-well .loading-anim").hide();
		$(".date-input").datepicker();
	});
	
	$("#replyTaskBtn").click(function () {
		var btn = $(this);
		
		var reply = {
			taskId: taskId,
			fields: []
		};
		
		$.each($("#replyForm .control-group"), function (i) {
			var type = $(this).attr("data-field-type");
			var name = $(this).find(".control-label").text();
			var value = "";
			switch(type) {
			case 'SHORT_TEXT':
				value = $(this).find("input.short-text-input").val();
				break;
			case 'LONG_TEXT':
				value = $(this).find("textarea.long-text-input").val();
				break;
			case 'DATE':
				value = $(this).find("input.date-input").val();
				break;
			case 'RADIO':
				value = $(this).find(".radio-input:checked").val();
				break;
			case 'CHECKBOX':
				$(this).find(".checkbox-input:checked").each(function () {
					var val = $(this).val();
					reply.fields.push({name: name, value: val});
				});
				return true;
			case 'SELECT':
				value = $(this).find(".select-input").val();
				break;
			default:
				return true;
			}
			
			reply.fields.push({name: name, value: value});
		});
		
		var quantityForm = $(".tt-quantity-field");
		if (quantityForm.length == 1) {
			var value = parseFloat(quantityForm.find("#goodQuantity").val());
			if (isNaN(value)) {
				GrouponUtils.modalError("Please enter a valid quantity!");
				return false;
			}
			reply.fields.push({name: 'TT_RES_QUANTITY', value: value});
		}
		
		btn.attr('disabled', 'disabled');
		$.ajax({
			type: "POST",
			contentType: 'application/json',
			url: GrouponUtils.siteBase + 'task/reply',
			data: JSON.stringify(reply),
		    success: function(response) {
		    	window.location.reload();
			}
		}).always(function () {
			$("#replyForm .control-group input[type=text], #replyForm .control-group textarea, #replyForm .control-group select").val("");
			$("#replyForm .control-group input[type=radio], #replyForm .control-group input[type=checkbox]").removeAttr("checked");
			btn.removeAttr("disabled");
		});
	});
	
	$(document).on('click', ".task-vote .vote-up-off", function () {
		var that = $(this);
		var countHolder = $(".task-vote .vote-count-post");
		countHolder.html(parseInt(countHolder.html()) + 1);
		that.removeClass("vote-up-off").addClass("vote-up-on");
		
		var downArrowOn = $(".task-vote .vote-down-on");
		if (downArrowOn.length == 1) {
			downArrowOn.removeClass("vote-down-on").addClass("vote-down-off");
			countHolder.html(parseInt(countHolder.html()) + 1);
		}
		
		var url = GrouponUtils.siteBase + 'task/voteTask';
		var data = {};
		data.direction = 'UP';
		data.taskId = taskId;
		$.post(url, data, function(resp) {
			// VOTE IS OK!
		}).fail(function () {
			countHolder.html(parseInt(countHolder.html()) - 1);
			that.removeClass("vote-up-on").addClass("vote-up-off");
			
			if (downArrowOn.length == 1) {
				downArrowOn.addClass("vote-down-on").removeClass("vote-down-off");
				countHolder.html(parseInt(countHolder.html()) - 1);
			}
		});
	});
	
	$(document).on('click', ".task-vote .vote-up-on", function () {
		var that = $(this);
		var countHolder = $(".task-vote .vote-count-post");
		countHolder.html(parseInt(countHolder.html()) - 1);
		that.removeClass("vote-up-on").addClass("vote-up-off");
		
		var url = GrouponUtils.siteBase + 'task/unvoteTask';
		var data = {};
		data.taskId = taskId;
		$.post(url, data, function(resp) {
			// VOTE IS OK!
		}).fail(function () {
			countHolder.html(parseInt(countHolder.html()) + 1);
			that.addClass("vote-up-on").removeClass("vote-up-off");
		});
	});
	
	$(document).on('click', ".task-vote .vote-down-off", function () {
		var that = $(this);
		var countHolder = $(".task-vote .vote-count-post");
		countHolder.html(parseInt(countHolder.html()) - 1);
		that.removeClass("vote-down-off").addClass("vote-down-on");
		
		var upArrowOn = $(".task-vote .vote-up-on");
		if (upArrowOn.length == 1) {
			upArrowOn.removeClass("vote-up-on").addClass("vote-up-off");
			countHolder.html(parseInt(countHolder.html()) - 1);
		}
		
		var url = GrouponUtils.siteBase + 'task/voteTask';
		var data = {};
		data.direction = 'DOWN';
		data.taskId = taskId;
		$.post(url, data, function(resp) {
			// VOTE IS OK!
		}).fail(function () {
			countHolder.html(parseInt(countHolder.html()) + 1);
			that.removeClass("vote-down-on").addClass("vote-down-off");
			
			if (upArrowOn.length == 1) {
				upArrowOn.addClass("vote-down-on").removeClass("vote-down-off");
				countHolder.html(parseInt(countHolder.html()) + 1);
			}
		});
	});

	$(document).on('click', ".task-vote .vote-down-on", function () {
		var that = $(this);
		var countHolder = $(".task-vote .vote-count-post");
		countHolder.html(parseInt(countHolder.html()) + 1);
		that.removeClass("vote-down-on").addClass("vote-down-off");
		
		var url = GrouponUtils.siteBase + 'task/unvoteTask';
		var data = {};
		data.taskId = taskId;
		$.post(url, data, function(resp) {
			// VOTE IS OK!
		}).fail(function () {
			countHolder.html(parseInt(countHolder.html()) - 1);
			that.addClass("vote-down-on").removeClass("vote-down-off");
		});
	});
	
	function getUserVotes() {
		var url = GrouponUtils.siteBase + 'task/getUserVotes?taskId=' + taskId;
		$.get(url, function (resp) {
			if (resp.task) {
				if (resp.task == "UP") {
					$(".task-vote .vote-up-off").removeClass("vote-up-off").addClass("vote-up-on");
				} else if (resp.task == "DOWN") {
					$(".task-vote .vote-down-off").removeClass("vote-down-off").addClass("vote-down-on");
				}
			}
		});
	}
	
	getUserVotes();
	
});
