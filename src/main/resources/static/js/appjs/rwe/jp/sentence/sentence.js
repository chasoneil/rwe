const prefix = "/rwe/jp/sentence";

$(function () {
    loadData();
});

function loadData() {

    let lessonName = $('#lessonName').val();

    $.ajax({
        cache: false,
        type: "POST",
        url : prefix + "/data",
        async: false,
        data : {
            'lessonName': lessonName
        },
        error : function(request) {
            parent.layer.alert("加载句子数据失败");
        },
        success : function(data) {
            if (data.code == 0) {
                sentences = JSON.parse(data.msg);
                length = sentenceArray.length;

                let singles = sentences.singles();
                let dialogs = sentences.dialogs();

                // 将数组中的数据打乱
                singles.sort(() => Math.random() - 0.5);
                dialogs.sort(() => Math.random() - 0.5);
                
                console.log(dialogs);
                initSingle(singles);
                initDialog(dialogs);

            } else {
                parent.layer.msg("加载句子数据失败");
            }
        }

    });
}


function initSingle(singles) {
    
    let title = "";
    for (let index = 0; index < singles.length; index++) {
        let single = singles[index];
        console.log(single);
        title = single.title();
        let content = single.content();
        let split = content.split("-");
        let cn = split[0];
        let jp = split[1];
        setSingle(cn, jp)
    }
}

function initDialog(dialogs) {

}

function setSingle() {

}



function passed() {
    
}




