var formIdV = 0;
var bindingInputEntityId = null;

$(entityIdBindingInput).on("change",function(){
	$.post("dynamicForm/show.do", {
		formId : formIdV
	}, function(framgetHtml) {
		document.write(framgetHtml);
	}, "html");
});


