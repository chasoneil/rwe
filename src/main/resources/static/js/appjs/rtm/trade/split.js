let PREFIX = "/rwe/trade";

// 用于存储用户新增的行数据
let userAddedRows = [];

// 监听“新增一行”按钮的点击事件
document.getElementById('addRow').addEventListener('click', function () {
	// 获取当前行数
	const rowCount = document.querySelectorAll('.auto-line').length;

	// 创建新的行
	const newRow = document.createElement('div');
	newRow.className = 'form-group auto-line';
	newRow.innerHTML = `
        <label class="col-sm-2 control-label">商品说明：</label>
        <div class="col-sm-2">
            <input id="product_${rowCount + 1}" class="form-control" type="text">
        </div>
        <label class="col-sm-1 control-label">金额：</label>
        <div class="col-sm-2">
            <input id="amount_${rowCount + 1}" class="form-control" onchange="calculateTotal()" type="text">
        </div>
        <label class="col-sm-1 control-label">备注：</label>
        <div class="col-sm-2">
            <input id="tradeComment_${rowCount + 1}" class="form-control" type="text">
        </div>
        <div class="col-sm-2">
            <button class="btn btn-danger" type="button" onclick="removeRow(this)">
                <i class="fa fa-trash" aria-hidden="true"></i> 删除
            </button>
        </div>
    `;

	// 将新行添加到表单中
	document.getElementById('auto-line').appendChild(newRow);

	// 保存新行的引用
	userAddedRows.push(newRow);
});

// 删除行的函数
function removeRow(button) {
	const row = button.closest('.form-group');
	row.remove();

	// 从 userAddedRows 中移除对应的行
	userAddedRows = userAddedRows.filter(item => item !== row);
}

function calculateTotal() {
	let amountTotal = $('#amount').val();

	let rowIndex = 1;
	while (true) {
		let amountId = "amount_" + rowIndex;
		let amountElement = document.getElementById(amountId);

		if (!amountElement) {
			break;
		}

		let value =  amountElement.value;
		if (!isNaN(parseFloat(value)) && isFinite(value)) {
			if (value < 0) {
				layer.alert("金额必须为大于0的数字！");
			}
		} else {
			layer.alert("金额必须为大于0的数字！");
		}
		amountTotal -= amountElement.value;
		rowIndex++;
	}
	$('#remainAmount').text(amountTotal);
}

function doSplit() {
	// 处理分割的账单信息
	let products = [];
	let amounts = [];
	let tradeComments = [];
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

		products[rowIndex - 1] = productElement.value;
		amounts[rowIndex - 1] = amountElement.value;
		tradeComments[rowIndex - 1] = tradeCommentElement.value;
		rowIndex++;
	}

	$.ajax({
		cache : true,
		type : "POST",
		url : PREFIX + "/doSplit",
		data : {
			"products" : products,
			"amounts" : amounts,
			"tradeComments" : tradeComments,
			"orderId" : document.getElementById("orderId").value
		},
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code === 0) {
				parent.layer.msg("拆分成功");
				parent.reload();
				let index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);
			} else {
				parent.layer.alert(data.msg)
			}
		}
	});
}



