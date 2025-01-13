
let prefix = '/rwe/consume_category';

$().ready(function() {
	validateRule();
	$(".chosen-select").chosen();
});

$.validator.setDefaults({
	submitHandler : function() {
		// set default value for budget
		if ($("#budget").val() === '') {
			$("#budget").val(0.0);
		} else {
			let value = $("#budget").val();
			if (isNaN(value) || parseFloat(value) < 0) {
				parent.layer.alert("预算必须为大于0的数字");
				return;
			}
		}

		if ($('#inOut').val() === '收入' && $('#budget').val() !== 0) {
			parent.layer.alert("收入不能设置预算");
			return;
		}

		save();
	}
});

// 新增一级菜单
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
			categoryName : {
				required : true
			},
			inOut : {
				required : true
			}
		},
		messages : {
			categoryName : {
				required : icon + "不能为空"
			},
			inOut : {
				required : icon + "不能为空"
			}
		}
	})
}




