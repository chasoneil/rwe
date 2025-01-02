const PREFIX = "/rwe/deepType/";

$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});

function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : PREFIX + "update",
		data : $('#signupForm').serialize(),
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code === 0) {
				parent.layer.msg("操作成功");
				parent.reload();
				let index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
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
			deepTypeName : {
				required : true
			}
		},
		messages : {
			deepTypeName : {
				required : icon + "不能为空"
			}
		}
	})
}
