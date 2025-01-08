
$(document).ready(function () {

    initCalendar();

    $('.chart').easyPieChart({
        barColor: '#f8ac59',
        //                scaleColor: false,
        scaleLength: 5,
        lineWidth: 4,
        size: 80
    });

    $('.chart2').easyPieChart({
        barColor: '#1c84c6',
        //                scaleColor: false,
        scaleLength: 5,
        lineWidth: 4,
        size: 80
    });

    var mapData = {
        "US": 298,
        "SA": 200,
        "DE": 220,
        "FR": 540,
        "CN": 120,
        "AU": 760,
        "BR": 550,
        "IN": 200,
        "GB": 120,
    };

    $('#world-map').vectorMap({
        map: 'world_mill_en',
        backgroundColor: "transparent",
        regionStyle: {
            initial: {
                fill: '#e4e4e4',
                "fill-opacity": 0.9,
                stroke: 'none',
                "stroke-width": 0,
                "stroke-opacity": 0
            }
        },

        series: {
            regions: [{
                values: mapData,
                scale: ["#1ab394", "#22d6b1"],
                normalizeFunction: 'polynomial'
            }]
        },
    });

});

// echarts
let pieChart1 = echarts.init(document.getElementById('pieChart1'));
let lineChart1 = echarts.init(document.getElementById('lineChart1'));

let pieOption1 = {
    title: {
        text: '消费分布',
        left: 'center'
    },
    tooltip: {
        trigger: 'item'
    },
    legend: {
        orient: 'vertical',
        left: 'left'
    },
    series: [{
        name: '所占比例',
        type: 'pie',
        radius: '60%',
        data: [
            { value: 335, name: '食品' },
            { value: 310, name: '交通' },
            { value: 234, name: '娱乐' },
            { value: 135, name: '购物' },
        ],
        emphasis: {
            itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
        }
    }]
};

let lineOption1 = {
    title: {
        text: '每月花费趋势',
        left: 'center'
    },
    xAxis: {
        type: 'category',
        data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月'],
        boundaryGap: false,
        nameTextStyle: { color: '#fff' }
    },
    yAxis: {
        type: 'value',
        name: '花费（元）',
        nameTextStyle: { color: '#fff' }
    },
    series: [{
        data: [1200, 1300, 900, 1500, 1700, 1600, 1800, 2200],
        type: 'line',
        smooth: true
    }]
};

pieChart1.setOption(pieOption1);
lineChart1.setOption(lineOption1);

function initCalendar() {
    /*---- initialize the calendar ------*/
    let date = new Date();
    let d = date.getDate();
    let m = date.getMonth();
    let y = date.getFullYear();

    $('#calendar').fullCalendar({
        header: {
            left: 'prev,next',
            center: 'title',
            right: 'month,agendaWeek,agendaDay'
        },
        editable: true,
        droppable: true, // this allows things to be dropped onto the calendar !!!
        drop: function (date, allDay) { // this function is called when something is dropped

            // retrieve the dropped element's stored Event Object
            var originalEventObject = $(this).data('eventObject');

            // we need to copy it, so that multiple events don't have a reference to the same object
            var copiedEventObject = $.extend({}, originalEventObject);

            // assign it the date that was reported
            copiedEventObject.start = date;
            copiedEventObject.allDay = allDay;

            // render the event on the calendar
            // the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
            $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

            // is the "remove after drop" checkbox checked?
            if ($('#drop-remove').is(':checked')) {
                // if so, remove the element from the "Draggable Events" list
                $(this).remove();
            }

        },
        events: [
            {
                title: '日事件',
                start: new Date(y, m, 1)
            },
            {
                title: '长事件',
                start: new Date(y, m, d - 5),
                end: new Date(y, m, d - 2),
            },
            {
                id: 999,
                title: '重复事件',
                start: new Date(y, m, d - 3, 16, 0),
                allDay: false,
            },
            {
                id: 999,
                title: '重复事件',
                start: new Date(y, m, d + 4, 16, 0),
                allDay: false
            },
            {
                title: '会议',
                start: new Date(y, m, d, 10, 30),
                allDay: false
            },
            {
                title: '午餐',
                start: new Date(y, m, d, 12, 0),
                end: new Date(y, m, d, 14, 0),
                allDay: false
            },
            {
                title: '生日',
                start: new Date(y, m, d + 1, 19, 0),
                end: new Date(y, m, d + 1, 22, 30),
                allDay: false
            },
            {
                title: '打开百度',
                start: new Date(y, m, 28),
                end: new Date(y, m, 29),
                url: 'http://baidu.com/'
            }
        ],
    });
}

function updateClock() {
    const now = new Date();

    // 格式化日期
    const options = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' };
    const currentDate = now.toLocaleDateString('zh-CN', options);
    document.getElementById('date').textContent = currentDate;

    // 格式化时间
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    const currentTime = `${hours}:${minutes}:${seconds}`;
    document.getElementById('clock').textContent = currentTime;
}


// function generateCalendar() {
//     const calendar = document.getElementById('calendar');
//     const now = new Date();
//     const year = now.getFullYear();
//     const month = now.getMonth();
//
//     // 获取当前月份的第一天和最后一天
//     const firstDay = new Date(year, month, 1);
//     const lastDay = new Date(year, month + 1, 0);
//
//     // 找到第一天是星期几，注意这里要调整为周一开始
//     const startDay = (firstDay.getDay() + 6) % 7; // 将 Sunday (0) 转为 Saturday (6)
//     const daysInMonth = lastDay.getDate();
//
//     // 添加表头
//     const weekdays = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];
//     let headerRow = '<tr>';
//     weekdays.forEach(day => {
//         headerRow += `<th>${day}</th>`;
//     });
//     headerRow += '</tr>';
//     calendar.innerHTML += headerRow;
//
//     // 填充日期
//     let dateRow = '<tr>';
//     // 填充前面的空白
//     for (let i = 0; i < startDay; i++) {
//         dateRow += '<td></td>';
//     }
//
//     // 填充日期
//     for (let day = 1; day <= daysInMonth; day++) {
//         dateRow += `<td>${day}</td>`;
//         if ((day + startDay) % 7 === 0) { // 每七天换行
//             calendar.innerHTML += dateRow;
//             dateRow = '<tr>'; // 换行
//         }
//     }
//
//     // 如果当前行没有结束，添加行
//     if (dateRow !== '<tr>') {
//         calendar.innerHTML += dateRow;
//     }
// }
//
// generateCalendar();

setInterval(updateClock, 1000);
updateClock(); // 初始调用以显示时钟



