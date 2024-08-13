
/**
 * 设置select单选默认选中
 * @param selectId select元素的Id
 * @param svalue 需要默认设置选中的值
 * @returns
 */
function setOptions(var selectId, var svalue){
	var select_ele = document.getElementById(selectId);
	if(select_ele == null){
		return;
	}
	for(var i=0; i<select_ele.options.length; i++){
		if(select_ele.options[i].value == svalue){
			select_ele.options[i].selected = true;
			return;
		}
	}
}