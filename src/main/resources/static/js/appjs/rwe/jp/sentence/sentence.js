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
                let sentences = JSON.parse(data.msg);

                let singles = sentences.singles;
                let dialogs = sentences.dialogs;

                // 将数组中的数据打乱
                singles.sort(() => Math.random() - 0.5);
                dialogs.sort(() => Math.random() - 0.5);
                
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
        title = single.title;
        let content = single.content;
        let split = content.split("-");
        let cn = split[0];
        let jp = split[1];
        setSingle(cn, jp, index)
    }
}

function initDialog(dialogs) {
    let title = "";
    for (let index = 0; index < dialogs.length; index++) {
        let dialog = dialogs[index];
        console.log(dialog);
        title = dialog.title;
        // let content = dialog.content;
        // let split = content.split("-");
        // let cn = split[0];
        // let jp = split[1];
        // setSingle(cn, jp, index)
    }
}

function setSingle(cn, jp, index) {

    let elements = `
        <h3>${cn}</h3>
        <div class="input-group" style="display: flex; align-items: center;">
            <input type="hidden" class="form-control" value="${jp}" id="jpa-${index}">
            <input type="text" class="form-control" placeholder="请将上面的中文翻译成日语" id="jpu-${index}">
            <button type="button" class="btn btn-sm btn-success" onclick="check('${index}')" style="margin-left:5px;margin-top:2px;">
                <i class="fa fa-check" aria-hidden="true"></i> 检查
            </button>
        </div>    
        <hr>
    `;

    $('#content').append(elements);

}

function check(index) {
    
    let resId = 'jpu-' + index;
    let ansId = 'jpa-' + index;

    let Q = $('#' + resId).val();
    let A = $('#' + ansId).val();

    if (Q === A) {
        layer.msg('回答正确');
    } else {
        layer.alert('回答错误，正确答案:' + A);
    }
}





