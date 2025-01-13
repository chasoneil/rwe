const PREFIX = "/rwe/consume_category/";

$().ready(function() {
	validateRule();

	let config = {
		'.chosen-select': {},
		'.chosen-select-deselect': {
			allow_single_deselect: true
		},
		'.chosen-select-no-single': {
			disable_search_threshold: 10
		},
		'.chosen-select-no-results': {
			no_results_text: 'Oops, nothing found!'
		},
		'.chosen-select-width': {
			width: "40%"
		}
	}
	for (let selector in config) {
		$(selector).chosen(config[selector]);
	}
});

document.addEventListener("DOMContentLoaded", function() {
	const selectElements = document.querySelectorAll("select");
	selectElements.forEach(select => {
		const selectedValue = select.getAttribute("value");
		if (selectedValue) {
			Array.from(select.options).forEach(option => {
				if (option.value === selectedValue) {
					option.selected = true;
					$(select).trigger("chosen:updated");
				}
			});
		}
	});
});

$.validator.setDefaults({
	submitHandler : function() {

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
