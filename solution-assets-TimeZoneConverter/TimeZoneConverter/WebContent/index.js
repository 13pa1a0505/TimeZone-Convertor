$(window).ready(function() {
	$.get("TimeConvertServlet", function(responseJson) {
		var $select = $("#inpDropdownlist,#outDropdownlist");
		$select.find("option").remove();
		$.each(responseJson, function(index, value) {
			$("<option>").val(value).text(value).appendTo($select);
		});

	});

});
// Getting data from the user and submitting the form
$(document).on("click", "#buttonSubmit", function() {
	var inpDate = $("#inpDate").val();

	if (inpDate != "") {
		var params = {
			inpDropdown : $("#inpDropdownlist option:selected").val(),
			outDropdown : $("#outDropdownlist option:selected").val(),
			inpDate : $("#inpDate").val()
		};

		// Posting the data to the servlet and getting response
		$.post("TimeConvertServlet", $.param(params), function(responseJson) {

			var error = responseJson.search("error");
			if (error != -1) {
				alert("Internal error");
			} else{
				$("#outDate").val(responseJson);
			}
		});
	} else {
		alert("Please choose date and time");
	}

});
