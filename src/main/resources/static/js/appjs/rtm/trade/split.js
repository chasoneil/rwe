let PREFIX = "/rwe/trade";

$().ready(function() {
	validateRule();
});

document.getElementById('addRow').addEventListener('click', function() {
	// 获取#split-form的父元素
	let splitForm = document.getElementById('split-form');
	// 克隆当前的行
	let newRow = splitForm.cloneNode(true);

	// 获取当前行数
	let rowCount = document.querySelectorAll('.form-group[id^="split-form"]').length + 1;

	// 更新新行的id
	newRow.id = 'split-form_' + rowCount;

	// 更新新行中所有输入框的id
	let inputs = newRow.getElementsByTagName('input');
	for (let i = 0; i < inputs.length; i++) {
		let oldId = inputs[i].id;
		if (oldId) {
			let newId = oldId.split('_')[0] + '_' + rowCount;
			inputs[i].id = newId;
		}
	}

	// 清空新行中的输入框的值
	for (let i = 0; i < inputs.length; i++) {
		inputs[i].value = '';
	}

	// 将新行添加到表单中
	splitForm.parentNode.insertBefore(newRow, splitForm.nextSibling);
});

$.validator.setDefaults({
	submitHandler : function() {
		doSplit();
	}
});

function doSplit() {

	// 处理分割的账单信息
	let products;
	let amounts;
	let tradeComments;
	let rowIndex = 1;

	while (true) {
		let productId = "product_" + rowIndex;
		let productElement = document.getElementById(productId);
		let amountId = "amount_" + rowIndex;
		let amountElement = document.getElementById(amountId);
		let tradeCommentId = "tradeComment_" + rowIndex;
		let tradeCommentElement = document.getElementById(tradeCommentId);

		if (!productElement) {
			break; // 如果找不到该行，退出循环
		}

		products += productElement.value + ",";
		amounts += amountElement.value + ",";
		tradeComments += tradeCommentElement.value + ",";
		rowIndex++; // 处理下一行
	}

	// 将分割的账单信息放入表单中
	document.getElementById("product").value = products.substring(0, products.length - 1);
	document.getElementById("amount").value = amounts.substring(0, amounts.length - 1);
	document.getElementById("tradeComment").value = tradeComments.substring(0, tradeComments.length - 1);

	$.ajax({
		cache : true,
		type : "POST",
		url : PREFIX + "/doSplit",
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
			tradeTime : {
				required : true
			},
			amount : {
				required : true
			},
			tradeType : {
				required : true
			},
			payFor : {
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
			tradeType : {
				required : icon + "不能为空"
			},
			payFor : {
				required : icon + "不能为空"
			}
		}
	})
}

