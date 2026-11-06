export function warningInfo(warnEarlyConditionForms) {
    return {
        isCondition: '1',
        warnPreDesc:"",
        [warnEarlyConditionForms]: [{
            conditionType: undefined,
            earlyWarnValue: undefined,
            earlyWarnStart: undefined,
            earlyWarnEnd: undefined,
            propertyId: undefined,
            followId: undefined,
            earlyWarnLevel: undefined,
            sexCondition: undefined,
            ageConditionType: undefined,
            ageEarlyWarnValue: undefined,
            ageEarlyWarnStart: undefined,
            ageEarlyWarnEnd: undefined
        }]
    }
}
export function dateTimeFormat() {
    return {
        date: 'yyyy-MM-dd',
        week: 'yyyy 第 WW 周',
        month: 'yyyy-MM',
        year: 'yyyy',
        datetime: 'yyyy-MM-dd HH:mm',
        daterange: 'yyyy-MM-dd',
        monthrange: 'yyyy-MM',
        datetimerange: 'yyyy-MM-dd HH:mm'
    }
}
export function daterangeTypes() {
    return [{
        label: '不限制',
        value: 1,
    }, {
        label: '指定日期',
        value: 2,
    }, {
        label: '起始日期后',
        value: 3,
    }]
}

export function dateTypeOptions() {
    return [{
        label: '日(date)',
        value: 'date',
        dateUnit:'天',
    },
    {
        label: '周(week)',
        value: 'week',
        dateUnit:'周',
    },
    {
        label: '月(month)',
        value: 'month',
        dateUnit:'月',
    },
    {
        label: '年(year)',
        value: 'year',
        dateUnit:'年',
    },
    {
        label: '日期时间(datetime)',
        value: 'datetime',
        dateUnit:'',
    }]
}

export function dateRangeTypeOptions() {
    return [{
        label: '日期范围(daterange)',
        value: 'daterange'
    },
    {
        label: '月范围(monthrange)',
        value: 'monthrange'
    },
    {
        label: '日期时间范围(datetimerange)',
        value: 'datetimerange'
    }]
}

export function colorFormatOptions() {
    return [{
        label: 'hex',
        value: 'hex'
    },
    {
        label: 'rgb',
        value: 'rgb'
    },
    {
        label: 'rgba',
        value: 'rgba'
    },
    {
        label: 'hsv',
        value: 'hsv'
    },
    {
        label: 'hsl',
        value: 'hsl'
    }]
}
export function justifyOptions() {
    return [{
        label: 'start',
        value: 'start'
    },
    {
        label: 'end',
        value: 'end'
    },
    {
        label: 'center',
        value: 'center'
    },
    {
        label: 'space-around',
        value: 'space-around'
    },
    {
        label: 'space-between',
        value: 'space-between'
    }]
}
export function regListOptions() {
    return [{
        value: '',
        label: '不限',
        message: "",
        pattern: ""
    },
    {
        value: 'number',
        label: '数字',
        message: "数字格式错误",
        pattern: "/^[0-9]*$/"
    },
    {
        value: 'email',
        label: '邮箱',
        message: "邮箱格式错误",
        pattern: "/^\w+[-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/"
    },
    {
        value: 'mobile',
        label: '手机号',
        message: "手机号格式错误",
        // pattern: "/^1(3|4|5|7|8|9)\\d{ 9}$/"
        pattern: "/^1[3|4|5|6|7|8|9][0-9]\d{8}$/"
    },
    {
        value: 'IDNumber',
        label: '身份证号',
        message: "身份证号格式错误",
        pattern: "/^\d{15}|\d{}18$/"
    },
    {
        value: 'chinese',
        label: '中文',
        message: "中文格式错误",
        pattern: "/^[\u4e00-\u9fa5],{0,}$/"
    },
    {
        value: 'letters',
        label: '字母',
        message: "字母格式错误",
        pattern: "/^[A-Za-z]+$/"
    }]
}

export function warningSymbolOptions() {
    return [{
        label: '等于',
        value: '1'
    }, {
        label: '大于',
        value: '2'
    }, {
        label: '大于等于',
        value: '3'
    }, {
        label: '小于',
        value: '4'
    }, {
        label: '小于等于',
        value: '5'
    }, {
        label: '区间',
        value: '6'
    }]
}
export function conditionOptions() {
    return [{
        //     label: '不限制',
        //     value: '0'
        // },{
        label: '等于',
        value: '1'
    }, {
        label: '大于',
        value: '2'
    }, {
        label: '大于等于',
        value: '3'
    }, {
        label: '小于',
        value: '4'
    }, {
        label: '小于等于',
        value: '5'
    }, {
        label: '区间',
        value: '6'
    }]
}
export const toolbarData =
    [
        ["bold", "italic", "underline", "strike"], // 加粗 斜体 下划线 删除线
        [{
            color: []
        }, {
            background: []
        }], // 字体颜色、字体背景颜色
        [{
            list: "ordered"
        }, {
            list: "bullet"
        }], // 有序、无序列表
        // [{
        //     align: []
        // }], // 对齐方式
        ["clean"], // 清除文本格式
        ["image"], // 链接、图片、视频
        [
            { table: "TD" },
            { "table-insert-row": "TIR" },
            { "table-insert-column": "TIC" },
            { "table-delete-row": "TDR" },
            { "table-delete-column": "TDC" },
        ]
    ]