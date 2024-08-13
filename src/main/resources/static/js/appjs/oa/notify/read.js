/**
 * 读取附件信息
 * */
function filesShow(nodeName, filedValue)
{
	if (filedValue == null) 
	{
		return;
	}
	var files = filedValue.split(',');
	var txt = "";
	for(j = 0, len = files.length; j < len; j++)
	{
		fileInfos = files[j].split(':');
		strFileName = '-';
		strUrl = '';
		if (fileInfos.length == 2)
	    {
			strFileName = fileInfos[0];
			strUrl = fileInfos[1];
	    }
		else
	    {
			strUrl = files[j];
	    }
		
		var index1 = strUrl.lastIndexOf(".");
		var index2 = strUrl.length;
		var suffix = strUrl.substring(index1+1,index2);//后缀名
		
		if ('jpg,jpeg,png,gif,bmp'.indexOf(suffix) >  -1)
	    {
			txt = txt + "<div class='file-box'>" +
			"<div class='file'>" +
			"<a href=''>" +
			"<span class='corner'></span>" +
			"<div class='image'><img alt='image' class='img-responsive' src='"+ strUrl +"'></div>" +
			"<div class='file-name'><a href='"+ strUrl +"' target='_blank'>"+ strFileName +"</a></div>" +
			"</a>" +
			"</div>" +
			"</div>";
	    }
		else
		{
			txt = txt + "<div class='file-box'>" +
			"<div class='file'>" +
			"<a href=''>" +
			"<span class='corner'></span>" +
			"<div class='icon'><i class='fa fa-file'></i></div>" +
			"<div class='file-name'><a href='"+ strUrl +"' target='_blank'>"+ strFileName +"</a></div>" +
			"</a>" +
			"</div>" +
			"</div>";
		}

	}
	var objTools = document.getElementById(nodeName);
	objTools.innerHTML = txt;
}
