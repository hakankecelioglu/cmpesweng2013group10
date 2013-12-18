$(function () {
	var isPopoverOpen = false;
	var isPopover2Open = false;
	
	var isOrChild = function (el, str) {
		return el.is(str) || el.closest(str).length > 0;
	};
	
	var form = {};
	
	form.lastId = 1;
	
	form.addNewField = function ($list, id, doneClass) {
		var $group = $('<button class="close removeFormField"><i class="icon-trash"></i></button>');
		$group = $group.add('<button class="close move-field-up"><i class="icon-arrow-up"></i></button>');
		$group = $group.add('<button class="close move-field-down"><i class="icon-arrow-down"></i></button>');
		$group = $group.add($list);
		$group = $group.add('<p><button class="btn btn-success ' + doneClass + ' whenOpen">Done</button></p>');
		
		var item = $('<div class="top-bottom-border formInputLine"></div>').append($group);
		
		if (isPopoverOpen) {
			$("#taskTypeFields").append(item);
			$("#noFieldText").addClass('hide');
		} else if (isPopover2Open) {
			$("#popoverExtraReplyInput").before(item);
			$("#noReplyFieldText").addClass('hide');
		}
	};
	
	form.openClosedInput = function (e) {
		var t = $(e.target);
		if (isOrChild(t, ".removeFormField") || isOrChild(t, ".move-field-up") || isOrChild(t, ".move-field-down")) {
			return true;
		}
		var parent = $(this);
		parent.find('.whenClosed').addClass('hide');
		parent.find('.whenOpen').removeClass('hide');
	};
	
	form.removeField = function () {
		var parent = $(this).closest('.formInputLine');
		if (parent.closest("#taskTypeReplyFields").length != 0) {
			parent.remove();
			
			if ($('#taskTypeReplyFields .formInputLine:visible').length == 0) {
				$("#noReplyFieldText").removeClass('hide');
			}
		} else if (parent.closest("#taskTypeFields").length != 0) {
			parent.remove();
			
			if ($('#taskTypeFields .formInputLine').length == 0) {
				$("#noFieldText").removeClass('hide');
			}
		}
	};
	
	form.moveFieldUp = function () {
		var line = $(this).closest('.formInputLine');
		var prev = line.prev(".formInputLine");
		if (prev.length > 0) {
			prev.before(line);
		}
		return false;
	};
	
	form.moveFieldDown = function () {
		var line = $(this).closest('.formInputLine');
		var next = line.next(".formInputLine");
		if (next.length > 0) {
			next.after(line);
		}
		return false;
	};
	
	/**
	 * Single line text input api
	 */
	form.addTextInput = function () {
		var id = form.lastId++;
		var $group = $('<label class="text-success whenOpen" for="text_input_' + id + '">Question title:</label>');
		$group = $group.add('<input class="span6 whenOpen singleTextInput" id="text_input_' + id + '" type="text" placeholder="Write question title">');
		
		$group = $group.add('<label class="text-success whenClosed hide singleTextValue"></label>');
		$group = $group.add('<input disabled="disabled" class="span6 whenClosed hide" type="text" placeholder="This is how it looks!">');
		$group = $group.add('<input type="hidden" id="inputLineType" value="SINGLE_TEXT" />');
		
		form.addNewField($group, id, 'doneSingleText');
	};
	
	form.doneSingleText = function () {
		var parent = $(this).closest('.formInputLine');
		parent.find('.whenOpen').addClass('hide');
		var val = parent.find('input.singleTextInput').val();
		parent.find("label.singleTextValue").html(val);
		parent.find('.whenClosed').removeClass('hide');
		parent.one('click', form.openClosedInput);
	};
	
	form.readSingleTextData = function (e) {
		var field = {};
		field.name = e.find('.singleTextInput').val();
		field.type = 'SHORT_TEXT';
		return field;
	};
	
	/**
	 * Multiple line text api
	 */
	form.addMultipleTextInput = function () {
		var id = form.lastId++;
		var $group = $('<label class="text-success whenOpen" for="large_text_input_' + id + '">Question title:</label>');
		$group = $group.add('<input class="span6 whenOpen multipleTextInput" id="large_text_input_' + id + '" type="text" placeholder="Write question title">');
		
		$group = $group.add('<label class="text-success whenClosed hide multipleTextValue"></label>');
		$group = $group.add('<textarea disabled="disabled" class="span6 whenClosed hide" placeholder="This is how it looks!"></textarea>');
		$group = $group.add('<input type="hidden" id="inputLineType" value="MULTIPLE_TEXT" />');
		
		form.addNewField($group, id, 'doneMultipleText');
	};
	
	form.doneMultipleText = function () {
		var parent = $(this).closest('.formInputLine');
		parent.find('.whenOpen').addClass('hide');
		var val = parent.find('input.multipleTextInput').val();
		parent.find("label.multipleTextValue").html(val);
		parent.find('.whenClosed').removeClass('hide');
		parent.one('click', form.openClosedInput);
	};
	
	form.readMultipleTextData = function (e) {
		var field = {};
		field.name = e.find('.multipleTextInput').val();
		field.type = 'LONG_TEXT';
		return field;
	};
	
	/**
	 * Radio buttons api
	 */
	form.createRadioButton = function (removable) {
		var $pLine = $('<p class="multipleChoiceLine"></p>');
		$pLine.append('<label class="radio whenOpen"><input type="radio" disabled="disabled" /><input type="text" class="input-medium multipleChoiceOptionInput" value="Option" placeholder="Option description" /></label>');
		$pLine.append('<label class="radio text-success whenClosed hide multipleChoiceOptionValue"></label>');
		if (removable) {
			$pLine.append('<i class="icon-trash removeMultipleChoiceOption whenOpen"></i>');
		}
		return $pLine;
	};
	
	form.addMultipleChoice = function () {
		var id = form.lastId++;
		var $group = $('<label class="text-success whenOpen" for="multiple_choice_input_' + id + '">Question title:</label>');
		$group = $group.add('<input class="span6 whenOpen multipleChoiceInput" id="multiple_choice_input_' + id + '" type="text" placeholder="Write question title" value="Question">');
		
		$group = $group.add('<label class="text-success whenClosed hide multipleChoiceValue"></label>');
		
		var $choiceGroup = $('<form class="form-inline"></form>');
		
		var $pLine1 = form.createRadioButton(false);
		var $pLine2 = form.createRadioButton(false);
		
		$choiceGroup.append($pLine1);
		$choiceGroup.append($pLine2);
		
		$group = $group.add($choiceGroup);
		$group = $group.add('<p class="whenOpen"><button class="btn btn-info multipleChoiceAddAnotherOption" type="button">Add another option</button></p>');
		$group = $group.add('<input type="hidden" id="inputLineType" value="MULTIPLE_CHOICE" />');
		
		form.addNewField($group, id, 'doneMultipleChoice');
	};
	
	form.doneMultipleChoice = function () {
		var parent = $(this).closest('.formInputLine');
		parent.find('.whenOpen').addClass('hide');
		var val = parent.find('input.multipleChoiceInput').val();
		parent.find("label.multipleChoiceValue").html(val);
		
		$.each(parent.find('.form-inline .multipleChoiceLine'), function (i, e) {
			var line = $(e);
			var val = line.find('input.multipleChoiceOptionInput').val();
			line.find('.multipleChoiceOptionValue').html('<input type="radio" disabled="disabled" />' + val);
		});
		
		parent.find('.whenClosed').removeClass('hide');
		parent.one('click', form.openClosedInput);
	};
	
	form.removeMultipleChoiceOption = function () {
		var parent = $(this).closest('.multipleChoiceLine');
		parent.remove();
	};
	
	form.multipleChoiceAddAnotherOption = function () {
		var parent = $(this).closest('.formInputLine');
		var $choiceGroup = parent.find('.form-inline');
		
		var $pLine = form.createRadioButton(true);
		$choiceGroup.append($pLine);
	};
	
	form.readMultipleChoiceData = function (t) {
		var field = {};
		field.name = t.find('.multipleChoiceInput').val();
		field.type = 'RADIO';
		field.attributes = [];
		
		$.each(t.find('.form-inline .multipleChoiceLine'), function (i, e) {
			var line = $(e);
			var val = line.find('input.multipleChoiceOptionInput').val();
			field.attributes.push({name: 'option' + i, value: val});
		});
		
		return field;
	};
	
	/**
	 * Checkboxes api
	 */
	form.createCheckbox = function (removable) {
		var $pLine = $('<p class="checkboxLine"></p>');
		$pLine.append('<label class="checkbox whenOpen"><input type="checkbox" disabled="disabled" /><input type="text" class="input-medium checkboxOptionInput" value="Option" placeholder="Option description" /></label>');
		$pLine.append('<label class="checkbox text-success whenClosed hide checkboxOptionValue"></label>');
		if (removable) {
			$pLine.append('<i class="icon-trash removeCheckboxOption whenOpen"></i>');
		}
		return $pLine;
	};
	
	form.addCheckboxes = function () {
		var id = form.lastId++;
		var $group = $('<label class="text-success whenOpen" for="checkbox_input_' + id + '">Question title:</label>');
		$group = $group.add('<input class="span6 whenOpen checkboxInput" id="checkbox_input_' + id + '" type="text" placeholder="Write question title" value="Question">');
		
		$group = $group.add('<label class="text-success whenClosed hide checkboxValue"></label>');
		
		var $choiceGroup = $('<form class="form-inline"></form>');
		
		var $pLine1 = form.createCheckbox(false);
		$choiceGroup.append($pLine1);
		
		$group = $group.add($choiceGroup);
		$group = $group.add('<p class="whenOpen"><button class="btn btn-info checkboxAddAnotherOption" type="button">Add another option</button></p>');
		$group = $group.add('<input type="hidden" id="inputLineType" value="CHECKBOX" />');
		
		form.addNewField($group, id, 'doneCheckbox');
	};
	
	form.doneCheckbox = function () {
		var parent = $(this).closest('.formInputLine');
		parent.find('.whenOpen').addClass('hide');
		var val = parent.find('input.checkboxInput').val();
		parent.find("label.checkboxValue").html(val);
		
		$.each(parent.find('.form-inline .checkboxLine'), function (i, e) {
			var line = $(e);
			var val = line.find('input.checkboxOptionInput').val();
			line.find('.checkboxOptionValue').html('<input type="checkbox" disabled="disabled" />' + val);
		});
		
		parent.find('.whenClosed').removeClass('hide');
		parent.one('click', form.openClosedInput);
	};
	
	form.removeCheckboxOption = function () {
		var parent = $(this).closest('.checkboxLine');
		parent.remove();
	};
	
	form.checkboxAddAnotherOption = function () {
		var parent = $(this).closest('.formInputLine');
		var $choiceGroup = parent.find('.form-inline');
		
		var $pLine = form.createCheckbox(true);
		$choiceGroup.append($pLine);
	};
	
	form.readCheckboxData = function (t) {
		var field = {};
		field.name = t.find('.checkboxInput').val();
		field.type = 'CHECKBOX';
		field.attributes = [];
		
		$.each(t.find('.form-inline .checkboxLine'), function (i, e) {
			var line = $(e);
			var val = line.find('input.checkboxOptionInput').val();
			field.attributes.push({name: 'option' + i, value: val});
		});
		
		return field;
	};
	
	/**
	 * Dropdown api
	 */
	form.createDropdownOption = function (removable) {
		var $pLine = $('<p class="dropdownLine"></p>');
		$pLine.append('<input type="text" class="input-medium dropdownOptionInput" value="Option" placeholder="Option description" />');
		$pLine.append('<label class="checkbox text-success whenClosed hide dropdownOptionValue"></label>');
		if (removable) {
			$pLine.append('<i class="icon-trash removeDropdownOption whenOpen"></i>');
		}
		return $pLine;
	};
	
	form.addDropdown = function () {
		var id = form.lastId++;
		var $group = $('<label class="text-success whenOpen" for="dropdown_input_' + id + '">Question title:</label>');
		$group = $group.add('<input class="span6 whenOpen dropdownInput" id="dropdown_input_' + id + '" type="text" placeholder="Write question title" value="Question">');
		
		$group = $group.add('<label class="text-success whenClosed hide dropdownValue"></label>');
		$group = $group.add('<select disabled="disabled" class="input-medium span6 whenClosed hide"></select>');
		
		var $choiceGroup = $('<form class="form-inline whenOpen"></form>');
		
		var $pLine1 = form.createDropdownOption(false);
		var $pLine2 = form.createDropdownOption(false);
		$choiceGroup.append($pLine1);
		$choiceGroup.append($pLine2);
		
		$group = $group.add($choiceGroup);
		$group = $group.add('<p class="whenOpen"><button class="btn btn-info dropdownAddAnotherOption" type="button">Add another option</button></p>');
		$group = $group.add('<input type="hidden" id="inputLineType" value="DROPDOWN" />');
		
		form.addNewField($group, id, 'doneDropdown');
	};
	
	form.doneDropdown = function () {
		var parent = $(this).closest('.formInputLine');
		parent.find('.whenOpen').addClass('hide');
		var val = parent.find('input.dropdownInput').val();
		parent.find("label.dropdownValue").html(val);
		
		parent.find('select').html('');
		$.each(parent.find('.form-inline .dropdownLine'), function (i, e) {
			var line = $(e);
			var val = line.find('input.dropdownOptionInput').val();
			var opt = $("<option></option>").val(val).html(val);
			parent.find('select').append(opt);
		});
		
		parent.find('.whenClosed').removeClass('hide');
		parent.one('click', form.openClosedInput);
	};
	
	form.removeDropdownOption = function () {
		var parent = $(this).closest('.dropdownLine');
		parent.remove();
	};
	
	form.dropdownAddAnotherOption = function () {
		var parent = $(this).closest('.formInputLine');
		var $choiceGroup = parent.find('.form-inline');
		
		var $pLine = form.createDropdownOption(true);
		$choiceGroup.append($pLine);
	};
	
	form.readDropdownData = function (t) {
		var field = {};
		field.name = t.find('.dropdownInput').val();
		field.type = 'SELECT';
		field.attributes = [];
		
		$.each(t.find('.form-inline .dropdownLine'), function (i, e) {
			var line = $(e);
			var val = line.find('input.dropdownOptionInput').val();
			field.attributes.push({name: 'option' + i, value: val});
		});
		
		return field;
	};
	
	/**
	 * Date api
	 */
	form.addDate = function () {
		var id = form.lastId++;
		var $group = $('<label class="text-success whenOpen" for="date_input_' + id + '">Question title:</label>');
		$group = $group.add('<input class="span6 whenOpen dateInput" id="date_input_' + id + '" type="text" placeholder="Write question title">');
		
		$group = $group.add('<label class="text-success whenClosed hide dateValue"></label>');
		$group = $group.add('<input disabled="disabled" class="span6 whenClosed hide" type="text" placeholder="10/10/2014">');
		$group = $group.add('<input type="hidden" id="inputLineType" value="DATE" />');
		
		form.addNewField($group, id, 'doneDate');
	};
	
	form.doneDate = function () {
		var parent = $(this).closest('.formInputLine');
		parent.find('.whenOpen').addClass('hide');
		var val = parent.find('input.dateInput').val();
		parent.find("label.dateValue").html(val);
		parent.find('.whenClosed').removeClass('hide');
		parent.one('click', form.openClosedInput);
	};
	
	form.readDateDate = function (t) {
		var field = {};
		field.name = t.find('.dateInput').val();
		field.type = 'DATE';
		return field;
	};
	
	/**
	 * DOM Listeners for adding form field actions
	 */
	$(document).on('click', '.removeFormField', form.removeField);
	$(document).on('click', '.move-field-up', form.moveFieldUp);
	$(document).on('click', '.move-field-down', form.moveFieldDown);
	
	// Single line text listeners
	$(document).on('click', '.addSingleText', form.addTextInput);
	$(document).on('click', '.doneSingleText', form.doneSingleText);
	
	// Multi line text listeners
	$(document).on('click', '.addMultipleText', form.addMultipleTextInput);
	$(document).on('click', '.doneMultipleText', form.doneMultipleText);
	
	// Multiple choice listeners
	$(document).on('click', '.addMultipleChoice', form.addMultipleChoice);
	$(document).on('click', '.doneMultipleChoice', form.doneMultipleChoice);
	$(document).on('click', '.removeMultipleChoiceOption', form.removeMultipleChoiceOption);
	$(document).on('click', '.multipleChoiceAddAnotherOption', form.multipleChoiceAddAnotherOption);
	
	// Checkbox listeners
	$(document).on('click', '.addCheckboxes', form.addCheckboxes);
	$(document).on('click', '.doneCheckbox', form.doneCheckbox);
	$(document).on('click', '.removeCheckboxOption', form.removeCheckboxOption);
	$(document).on('click', '.checkboxAddAnotherOption', form.checkboxAddAnotherOption);
	
	// Dropdown listeners
	$(document).on('click', '.addDropdown', form.addDropdown);
	$(document).on('click', '.doneDropdown', form.doneDropdown);
	$(document).on('click', '.removeDropdownOption', form.removeDropdownOption);
	$(document).on('click', '.dropdownAddAnotherOption', form.dropdownAddAnotherOption);
	
	// Date listeners
	$(document).on('click', '.addDate', form.addDate);
	$(document).on('click', '.doneDate', form.doneDate);
	
	var replyForm = {};
	
	replyForm.onNeedTypeChanged = function () {
		var needType = $(this).val();
		if (needType == "GOODS") {
			$(".whenGoodType").show();
		} else if (needType == "SERVICE") {
			$(".whenGoodType").hide();
		} else {
			$(".whenGoodType").hide();
		}
		
		if ($("#taskTypeReplyFields .formInputLine:visible").length != 0) {
			$("#noReplyFieldText").addClass("hide");
		} else {
			$("#noReplyFieldText").removeClass('hide');
		}
	};
	
	$(document).on('change', '#taskTypeNeedType', replyForm.onNeedTypeChanged);
	$('#taskTypeNeedType').change();
	
	/**
	 * Code of the popover which is opening when the user pressed 'add' button
	 */
	
	$('#popoverExtraInput').popover({
		placement: 'right',
		html: true,
		content: function () {
			isPopoverOpen = true;
			return $("#inputTypeForm").html();
		}
	});
	
	$("#popoverExtraReplyInput").popover({
		placement: 'top',
		html: true,
		content: function () {
			isPopover2Open = true;
			return $("#inputTypeForm").html();
		}
	});
	
	$(document).on('click', function (e) {
		var $target = $(e.target);
		var isClickedPopoverBtn = $target.is("#popoverExtraInput") || $target.closest("#popoverExtraInput").length != 0;
		var isClickedPopover2Btn = $target.is("#popoverExtraReplyInput") || $target.closest("#popoverExtraReplyInput").length != 0;
		
		if (isClickedPopoverBtn) {
			if (isPopover2Open) {
				$("#popoverExtraReplyInput").popover('hide');
			}
			return;
		} else if (isClickedPopover2Btn) {
			if (isPopoverOpen) {
				$('#popoverExtraInput').popover('hide');
			}
			return;
		}
		
		if ($target.closest('.popover').length == 0 && (isPopoverOpen || isPopover2Open)) {
			$('#popoverExtraInput').popover('hide');
			$("#popoverExtraReplyInput").popover('hide');
			isPopoverOpen = false;
			isPopover2Open = false;
			return;
		}
		
		if ($target.is('.popover button') || $target.closest('.popover button').length != 0) {
			$('#popoverExtraInput').popover('hide');
			$("#popoverExtraReplyInput").popover('hide');
			isPopoverOpen = false;
			isPopover2Open = false;
			return;
		}
	});
	
	$("body").tooltip({
		selector: '.popover .popover-add-item button'
	});
	
	form.readAllFields = function (parent) {
		var fields = [];
		
		$.each($(parent).find(".formInputLine"), function (i, e) {
			var line = $(e);
			var type = line.find("#inputLineType").val();
			switch (type) {
			case 'SINGLE_TEXT':
				var field = form.readSingleTextData(line);
				fields.push(field);
				break;
			case 'MULTIPLE_TEXT':
				var field = form.readMultipleTextData(line);
				fields.push(field);
				break;
			case 'MULTIPLE_CHOICE':
				var field = form.readMultipleChoiceData(line);
				fields.push(field);
				break;
			case 'CHECKBOX':
				var field = form.readCheckboxData(line);
				fields.push(field);
				break;
			case 'DROPDOWN':
				var field = form.readDropdownData(line);
				fields.push(field);
				break;
			case 'DATE':
				var field = form.readDateDate(line);
				fields.push(field);
				break;
			}
		});
		
		return fields;
	};

	$("#createTaskType").click(function () {
		var that = $(this);
		that.attr('disabled', 'disabled');
		
		var taskType = {};
		taskType.name = $("#taskTypeName").val();
		taskType.description = $("#taskTypeDesc").val();
		taskType.needType = $("#taskTypeNeedType").val();
		taskType.communityId = parseInt(UrlParameters.communityId);
		taskType.fields = form.readAllFields("#taskTypeFields");
		taskType.replyFields = form.readAllFields("#taskTypeReplyFields");
		
		var url = GrouponUtils.siteBase + 'community/createTaskType';
		
		$.ajax({
			contentType: 'application/json',
		    data: JSON.stringify(taskType),
		    dataType: 'json',
		    processData: false,
		    type: 'POST',
		    url: url
		}).fail(function (jqxhr) {
			that.removeAttr('disabled');
			GrouponUtils.ajaxModalError(jqxhr);
		}).success(function (data) {
			window.location.href = GrouponUtils.communityPage(taskType.communityId);
		});
	});
});