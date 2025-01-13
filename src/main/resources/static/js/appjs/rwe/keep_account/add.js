
const prefix = '/rwe/keep_account';

$().ready(function() {
	$(".chosen-select").chosen();
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});

function changeTradeType(categoryName) {

	const secondLevelSelect = document.getElementById('tradeType');

	if (categoryName === "") {
		parent.layer.alert("请选择一级菜单");
		return;
	}

	$.ajax({
		cache : true,
		type : "POST",
		url : "/rwe/trade/types",
		data : {
			"categoryName" : categoryName
		},
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code === 0) {
				if (data.types.length === 0) {
					secondLevelSelect.innerHTML = "<option value=''>选择二级分类</option>";
					$(secondLevelSelect).trigger("chosen:updated");
				} else {
					secondLevelSelect.innerHTML = "<option value=''>选择二级分类</option>";
					$(secondLevelSelect).trigger("chosen:updated");
					data.types.forEach(type => {
						const option = document.createElement("option");
						option.value = type;
						option.textContent = type;
						secondLevelSelect.appendChild(option);
						$(secondLevelSelect).trigger("chosen:updated");
					});
				}
			} else {
				parent.layer.alert("获取分类类型失败");
			}
		}
	});

}


function changeInOut(inOut) {

	const firstLevelSelect = document.getElementById('tradeVariety');

	if (inOut === "") {
		parent.layer.alert("请选择收支类型");
		return;
	}

	$.ajax({
		cache : true,
		type : "POST",
		url : prefix + "/inout",
		data : {
			"inOut" : inOut
		},
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code === 0) {
				if (data.names.length === 0) {
					// 将分类类型下拉框清空
					firstLevelSelect.innerHTML = "<option value=''>选择一级分类</option>";
					$(firstLevelSelect).trigger("chosen:updated");
				} else {
					firstLevelSelect.innerHTML = "<option value=''>选择一级分类</option>";
					$(firstLevelSelect).trigger("chosen:updated");
					data.names.forEach(name => {
						const option = document.createElement("option");
						option.value = name;
						option.textContent = name;
						firstLevelSelect.appendChild(option);
						$(firstLevelSelect).trigger("chosen:updated");
					});
				}
			} else {
				parent.layer.alert("获取分类类型失败");
			}
		}
	});

}

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
			tradeTime : {
				required : true
			},
			amount : {
				required : true
			},
			tradeVariety : {
				required : true
			},
			tradeType : {
				required : true
			},
			inOut : {
				required : true
			}
		},
		messages : {
			tradeTime : {
				required : icon + "不能为空"
			},
			amount : {
				required : icon + "不能为空"
			},
			tradeVariety : {
				required : icon + "不能为空"
			},
			tradeType : {
				required : icon + "不能为空"
			},
			inOut : {
				required : icon + "不能为空"
			}
		}
	})
}




