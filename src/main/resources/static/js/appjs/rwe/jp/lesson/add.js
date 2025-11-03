
var prefix = '/rwe/lesson';

$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});

function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : prefix + '/save',
		data : $('#signupForm').serialize(),
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			console.log(data);
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.loadLesson();
				var index = parent.layer.getFrameIndex(window.name);
				parent.layer.close(index);
			} else {
				parent.layer.msg(data.msg)
			}
		}
	});
}

function validateRule() {
	$("#signupForm").validate({

	})
}




