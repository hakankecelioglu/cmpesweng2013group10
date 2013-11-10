$(function () {
	
	var form = {};
	
	form.lastId = 1;
	
	form.addNewField = function ($list, id, doneClass) {
		var $group = $('<button class="close removeFormField"><i class="icon-trash"></i></button>');
		$group = $group.add('<button class="close"><i class="icon-arrow-up"></i></button>');
		$group = $group.add('<button class="close"><i class="icon-arrow-down"></i></button>');
		$group = $group.add($list);
		$group = $group.add('<p><button class="btn btn-success ' + doneClass + ' whenOpen">Done</button></p>');
		
		var item = $('<div class="top-bottom-border formInputLine"></div>').append($group);
		$("#taskTypeFields").append(item);
		
		$("#noFieldText").addClass('hide');
	};
	
	form.openClosedInput = function () {
		var parent = $(this);
		parent.find('.whenClosed').addClass('hide');
		parent.find('.whenOpen').removeClass('hide');
	};
	
	form.removeField = function () {
		var parent = $(this).closest('.formInputLine');
		parent.remove();
		if ($('.formInputLine').length == 0) {
			$("#noFieldText").removeClass('hide');
		}
	};
	
	form.addTextInput = function () {
		var id = form.lastId++;
		var $group = $('<label class="text-success whenOpen" for="text_input_' + id + '">Question title:</label>');
		$group = $group.add('<input class="span6 whenOpen singleTextInput" id="text_input_' + id + '" type="text" placeholder="Write question title">');
		
		$group = $group.add('<label class="text-success whenClosed hide singleTextValue"></label>');
		$group = $group.add('<input disabled="disabled" class="span6 whenClosed hide" type="text" placeholder="This is how it looks!">');
		
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
	
	form.addMultipleTextInput = function () {
		var id = form.lastId++;
		var $group = $('<label class="text-success whenOpen" for="large_text_input_' + id + '">Question title:</label>');
		$group = $group.add('<input class="span6 whenOpen multipleTextInput" id="large_text_input_' + id + '" type="text" placeholder="Write question title">');
		
		$group = $group.add('<label class="text-success whenClosed hide multipleTextValue"></label>');
		$group = $group.add('<textarea disabled="disabled" class="span6 whenClosed hide" placeholder="This is how it looks!"></textarea>');
		
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
	
	/**
	 * DOM Listeners for adding form field actions
	 */
	$(document).on('click', '.removeFormField', form.removeField);
	
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
	
	/**
	 * Code of the popover which is opening when the user pressed 'add' button
	 */
	var isPopoverOpen = false;
	
	$('#popoverExtraInput').popover({
		placement: 'right',
		html: true,
		content: function () {
			isPopoverOpen = true;
			return $("#inputTypeForm").html();
		}
	});
	
	$(document).on('click', function (e) {
		var $target = $(e.target);
		if ($target.is("#popoverExtraInput") || $target.closest("#popoverExtraInput").length != 0) {
			return;
		}
		
		if ($target.closest('.popover').length == 0 && isPopoverOpen) {
			$('#popoverExtraInput').popover('hide');
			isPopoverOpen = false;
			return;
		}
		
		if ($target.is('.popover button') || $target.closest('.popover button').length != 0) {
			$('#popoverExtraInput').popover('hide');
			isPopoverOpen = false;
			return;
		}
	});
	
	$("body").tooltip({
		selector: '.popover .popover-add-item button'
	});
});