const PREFIX = "/rwe/trade/";

$().ready(function() {
	validateRule();
	$(".chosen-select").chosen();
});

// document.addEventListener("DOMContentLoaded", function() {
// 	const selectElements = document.querySelectorAll("select");
//
// 	selectElements.forEach(select => {
// 		const selectedValue = select.getAttribute("value");
// 		if (selectedValue) {
// 			Array.from(select.options).forEach(option => {
// 				if (option.value === selectedValue) {
// 					option.selected = true;
// 					$(select).trigger("chosen:updated");
// 				}
// 			});
// 		}
// 	});
// });

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});

function getTypes () {

	const categoryTypeSelect = document.getElementById('categoryType');

	$.ajax({
		cache : true,
		type : "POST",
		url : PREFIX + "types",
		data : {
			"categoryName" : $("#categoryName").val(),
		},
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code === 0) {
				if (data.types.length === 0) {
					// 将分类类型下拉框清空
					categoryTypeSelect.innerHTML = "<option value=''>选择类型</option>";
					$(categoryTypeSelect).trigger("chosen:updated");
				} else {
					categoryTypeSelect.innerHTML = "<option value=''>选择类型</option>";
					$(categoryTypeSelect).trigger("chosen:updated");
					data.types.forEach(type => {
						const option = document.createElement("option");
						option.value = type;
						option.textContent = type;
						categoryTypeSelect.appendChild(option);
						$(categoryTypeSelect).trigger("chosen:updated");
					});
				}
			} else {
				parent.layer.alert("获取分类类型失败");
			}
		}
	});
}

function update() {

	if ($("#categoryType").val() === "") {
		parent.layer.alert("二级分类不能为空");
		return;
	}

	$.ajax({
		cache : true,
		type : "POST",
		url : PREFIX + "update",
		data : {
			"orderId" : $("#orderId").val(),
			"categoryType" : $("#categoryType").val(),
		},
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
			categoryName : {
				required : true
			}
		},
		messages : {
			categoryName : {
				required : icon + "不能为空"
			}
		}
	})
}
