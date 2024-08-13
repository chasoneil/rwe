//导入设备分组文件
$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});

function save() {
	//var formData = new FormData(document.forms.namedItem("signupForm"));
	var formData = new FormData($('#signupForm')[0]);
	$.ajax({
		type: 'POST',
        url: "/rwe/device/group/importDo",
        data:formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			files : {
				required : true
			}
		},
		messages : {
			files : {
				required : icon + "请选择文件"
			}
		}
	})
}

