const PREFIX = "/rwe/jp/word";

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
		url : PREFIX + "/update",
		data : $('#signupForm').serialize(),
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code === 0) {
				parent.layer.msg("操作成功");
				parent.reload();
				let index = parent.layer.getFrameIndex(window.name);
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
            word : {
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
            zhMean : {
                required : icon + '不能为空'
            }
        }
    })
}
