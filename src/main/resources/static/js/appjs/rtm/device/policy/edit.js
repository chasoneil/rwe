$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});

function update() {
	var selectElement = document.getElementById('spaceCode');
	var timingButton = document.getElementById('timingFlag');
	var delayButton = document.getElementById('delayFlag');
	var groupCodes = "";
	for(var i = 0; i < selectElement.length; i++)
	{
		if(selectElement.options[i].selected)
		{
			groupCodes += (selectElement.options[i].value + ',');
		}
	}
	var timingFlag = "00";
	var delayFlag = "00";
	if(timingButton.checked)
	{
		timingFlag = "11";
	}
	if(delayButton.checked)
	{
		delayFlag = "11";
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "/rwe/device/policy/timeSetDo",
		data : {
			'groupCodes' : groupCodes,
			'timingFlag' : timingFlag,
			'delayFlag'  : delayFlag,
			'timingOpenTime' : $('#timingOpenTime').val(),
			'timingCloseTime': $('#timingCloseTime').val(),
			'delayedOpenTime': $('#delayedOpenTime').val(),
			'now' : new Date()
		},
		async : false,
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
				parent.layer.msg(data.msg)
			}

		}
	});

}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			name : {
				required : true
			}
		},
		messages : {
			name : {
				required : icon + "请输入名字"
			}
		}
	})
}
