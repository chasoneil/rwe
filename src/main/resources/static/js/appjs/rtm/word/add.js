
var prefix = '/rwe/word';

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
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			word : {
				required : true
			},
			wordType : {
				required : true
			},
			lesson : {
				required : true
			},
			zhMean : {
				required : true
			}
		},
		messages : {
			word : {
				required : icon + "不能为空"
			},
			wordType : {
				required : icon + "不能为空"
			},
			lesson : {
				required : icon + "不能为空"
			},
			zhMean : {
				required : icon + "不能为空"
			}

		}
	})
}




