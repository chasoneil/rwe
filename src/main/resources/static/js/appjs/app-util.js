/**
 * 针对系统各模块使用的一些帮助类
 * 根据数据字典的分类得到下拉列表
 * @param dictType : 数据字典里的分类
 * @param selectedValue ：选中的值
 * @param chosen_select ：回写的css标志
 *
 * @return 把内容回写至chosen_select中
 * */
function loadDictType(dictType, selectedValue, chosen_select){
	var html = "";
	var chosen_select = "." + chosen_select;
	$.ajax({
		url : '/common/sysDict/list/' + dictType,
		success : function(data) {
			for (var i = 0; i < data.length; i++)
			{
				if (data[i].value == selectedValue)
				{
					html += '<option value="' + data[i].value + '" selected="selected">' + data[i].name + '</option>'
				}
				else
				{
					html += '<option value="' + data[i].value + '">' + data[i].name + '</option>'
				}

			}
			$(chosen_select).append(html);
			$(chosen_select).chosen({
				maxHeight : 200
			});
			//点击事件
			$(chosen_select).on('change', function(e, params) {
				var opt = {
					query : {
						type : params.selected,
					}
				}
				$('#exampleTable').bootstrapTable('refresh', opt);
			});
		}
	});
}

/**
 * 得到所有空间的下拉列表
 * @param selectedValue ：选中的值
 * @param chosen_select ：回写的css标志
 * @return 把内容回写至chosen_select中
 *
 * */
function loadSpaces(selectedValue, chosen_select){
	var html = "";
	var chosen_select = "." + chosen_select;
	$.ajax({
		url : '/rwe/space/listAll',
		success : function(data) {
			//加载数据
			for (var i = 0; i < data.length; i++)
			{
				if (data[i].spaceCode == selectedValue)
		        {
					html += '<option value="' + data[i].spaceCode + '" selected="selected">' + data[i].spaceAddress + '</option>'
		        }
				else
			    {
					html += '<option value="' + data[i].spaceCode + '">' + data[i].spaceAddress + '</option>'
			    }

			}
			$(chosen_select).append(html);
			$(chosen_select).chosen({
				maxHeight : 200
			});
			//点击事件
			$(chosen_select).on('change', function(e, params) {
				console.log(params.selected);
				var opt = {
					query : {
						type : params.selected,
					}
				}
				$('#exampleTable').bootstrapTable('refresh', opt);
			});
		}
	});
}
