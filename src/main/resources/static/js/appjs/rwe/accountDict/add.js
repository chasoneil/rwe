
const prefix = '/rwe/account/dict';

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
			if (data.code === 0) {
				parent.layer.msg("操作成功");
				parent.reload();
				const index = parent.layer.getFrameIndex(window.name);
				parent.layer.close(index);
			} else {
				parent.layer.alert(data.msg)
			}
		}
	});
}

function validateRule() {
	let icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			dictName : {
				required : true
			},
			dictType : {
				required : true
			}
		},
		messages : {
			dictName : {
				required : icon + "不能为空"
			},
			dictType : {
				required : icon + "不能为空"
			}
		}
	})
}




