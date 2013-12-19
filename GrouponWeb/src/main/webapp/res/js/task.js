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
	
	GrouponUtils.getTaskReplies(taskId).success(function (resp) {
		alert(JSON.stringify(resp));
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
		btn.attr('disabled', 'disabled');
		
		var reply = {
			taskId: taskId,
			fields: []
		};
		
		$.each($("#replyForm .control-group"), function (i) {
			var type = $(this).attr("data-field-type");
			var name = $(this).find(".attribute-label").text();
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
		
		$.ajax({
			type: "POST",
			contentType: 'application/json',
			url: GrouponUtils.siteBase + 'task/reply',
			data: JSON.stringify(reply),
		    success: function(response) {
		    	
			}
		}).always(function () {
			$("#replyForm .control-group input[type=text], #replyForm .control-group textarea, #replyForm .control-group select").val("");
			$("#replyForm .control-group input[type=radio], #replyForm .control-group input[type=checkbox]").removeAttr("checked");
			btn.removeAttr("disabled");
		});
	});
	
	
});
