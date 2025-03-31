// 表单属性【右面板】
export const formConf = {
    formRef: `elForm${Math.random().toString(36).substring(2, 4)}`,
    formModel: 'formData',
    size: 'medium',
    labelPosition: 'top',
    labelWidth: 100,
    formRules: 'rules',
    gutter: 15,
    disabled: false,
    span: 24,
    formBtns: true
}


export const inputComponents = [{
    layout: 'colFormItem',
    propertyName: '单选框组',
    propertyCode: '',
    isShowDom: true,
    tag: 'el-radio-group',
    labelType: 'Radio',
    isFlex: true, //是否换行
    isEditOption: true, // PC 自用
    isShowSubTitle: false, //副标题
    subTitle: '',
    tagIcon: 'radio',
    defaultValue: undefined,
    span: 24,
    optionType: 'default',
    size: 'medium',
    disabled: false,
    required: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    options: [{
        label: '选项一',
        value: 'abcvalue',
        isShowInputValue: false,
    }],
    document: 'https://element.eleme.cn/#/zh-CN/component/radio'
},
{
    layout: 'colFormItem',
    propertyName: '多选框组',
    tag: 'el-checkbox-group',
    isShowDom: true,
    labelType: 'Checkbox',
    isFlex: true,
    isEditOption: true,
    isShowSubTitle: false, //副标题
    subTitle: '',
    tagIcon: 'checkbox',
    defaultValue: [],
    span: 24,
    labelWidth: null,
    optionType: 'default',
    size: 'medium',
    disabled: false,
    required: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    options: [{
        label: '选项一',
        value: 'abcvalue',
        isShowInputValue: false,
    }],
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/checkbox'
}, {
    layout: 'colFormItem',
    propertyName: '下拉选择',
    tag: 'el-select',
    isShowDom: true,
    labelType: 'Select',
    isFlex: true,
    tagIcon: 'select',
    placeholder: '请选择',
    defaultValue: undefined,
    span: 24,
    isEditOption: true,
    isShowSubTitle: false, //副标题
    subTitle: '',
    labelWidth: null,
    clearable: true,
    disabled: false,
    required: true,
    filterable: false,
    multiple: false,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    options: [{
        label: '选项一',
        value: 'abcvalue',
        isShowInputValue: false,
        inputType: ''
    }],
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/select'
}, {
    layout: 'colFormItem',
    propertyName: '级联选择',
    tag: 'el-cascader',
    isShowDom: true,
    labelType: 'Cascader',
    isFlex: true,
    tagIcon: 'cascader',
    placeholder: '请选择',
    isEditOption: true,
    isShowSubTitle: false, //副标题
    subTitle: '',
    defaultValue: [],
    span: 24,
    labelWidth: null,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    props: {
        props: {
            multiple: false,
            expandTrigger: 'click'
        }
    },
    'show-all-levels': true,
    disabled: false,
    clearable: true,
    filterable: true,
    required: true,
    options: [{
        value: 'abcvalue',
        label: '选项1',
        level: 1,
        children: undefined
    }],
    dataType: 'static',
    labelKey: 'label',
    valueKey: 'value',
    childrenKey: 'children',
    separator: '/',
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/cascader'
},
{
    layout: 'colFormItem',
    propertyName: '单行文本',
    tag: 'el-input',
    isShowDom: true,
    labelType: 'Input',
    isFlex: true,
    tagIcon: 'input',
    placeholder: '请输入',
    defaultValue: undefined,
    isShowSubTitle: false, //副标题
    subTitle: '',
    isShowUnit: false,
    unit: '',
    span: 24,
    labelWidth: null,
    clearable: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    // prepend: '',
    // append: '',
    // 'prefix-icon': '',
    // 'suffix-icon': '',
    maxlength: null,
    // scope: '',
    // softScope: '',
    min: undefined,
    max: undefined,
    'show-word-limit': false,
    // readonly: false,
    disabled: false,
    required: true,
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/input'
},
{
    layout: 'colFormItem',
    propertyName: '多行文本',
    labelType: 'Textarea',
    isShowDom: true,
    tag: 'el-input',
    isFlex: true,
    tagIcon: 'textarea',
    type: 'textarea',
    placeholder: '请输入',
    defaultValue: undefined,
    isShowSubTitle: false, //副标题
    subTitle: '',
    span: 24,
    labelWidth: null,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    autosize: {
        minRows: 4,
        maxRows: 4
    },
    maxlength: null,
    'show-word-limit': false,
    readonly: false,
    disabled: false,
    required: true,
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/input'
},
{
    layout: 'colFormItem',
    propertyName: '时间选择',
    tag: 'el-time-picker',
    isShowDom: true,
    labelType: 'timePicker',
    tagIcon: 'time',
    placeholder: '请选择',
    isShowSubTitle: false, //副标题
    subTitle: '',
    defaultValue: null,
    span: 24,
    labelWidth: null,
    disabled: false,
    clearable: true,
    required: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    'picker-options': {
        selectableRange: '00:00:00-23:59:59'
    },
    pickerOptions: {
        selectableRange: '00:00:00-23:59:59'
    },
    valueFormat: 'HH:mm',
    format: 'HH:mm',
    'value-format': 'HH:mm',
    regValue: [],
    isFlex: true,

    document: 'https://element.eleme.cn/#/zh-CN/component/time-picker'
},
{
    layout: 'colFormItem',
    propertyName: '日期选择',
    labelType: 'Date',
    isShowDom: true,
    tag: 'el-date-picker',
    isFlex: true,
    tagIcon: 'date',
    placeholder: '请选择',
    isShowSubTitle: false, //副标题
    subTitle: '',
    defaultValue: null,
    type: 'date',
    span: 24,
    labelWidth: null,
    disabled: false,
    clearable: true,
    required: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    format: 'yyyy-MM-dd',
    'value-format': 'yyyy-MM-dd',
    'picker-options': {},
    params: {
        valueFormat: 'yyyy-MM-dd',
    },
    startType: '1',
    endType: '1',
    pickerOptions: {
        minDate: null,
        maxDate: null,
    },
    readonly: false,
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/date-picker'
},
{
    layout: 'colFormItem',
    propertyName: '上传文件',
    isShowDom: true,
    labelType: 'upload',
    tag: 'el-upload',
    isShowSubTitle: false, //副标题
    subTitle: '',
    isFlex: true,
    tagIcon: 'upload',
    action: 'https://test.dchealthclinical.com/dchealthadmin/common/upload',
    defaultValue: null,
    labelWidth: null,
    disabled: false,
    required: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    accept: '',
    name: 'file',
    'auto-upload': true,
    showTip: true,
    buttonText: '点击上传',
    fileSize: 2,
    sizeUnit: 'MB',
    'list-type': 'text',
    multiple: false,
    limit: 9,
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/upload'
},
{
    layout: 'colFormItem',
    propertyName: '上传图片',
    tag: 'el-upload',
    isShowDom: true,
    labelType: 'Upload',
    isFlex: true,
    tagIcon: 'upload',
    action: 'https://test.dchealthclinical.com/dchealthadmin/common/upload',
    defaultValue: null,
    labelWidth: null,
    disabled: false,
    required: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    accept: 'image/*',
    name: 'file',
    'auto-upload': true,
    showTip: true,
    buttonText: '点击上传',
    isShowSubTitle: false, //副标题
    subTitle: '',
    fileSize: 2,
    sizeUnit: 'MB',
    'list-type': 'picture-card',
    multiple: false,
    fileList: [],
    limit: 9,
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/upload'
},
{
    layout: 'colFormItem',
    propertyName: '计数器',
    labelType: 'InputNumber',
    isShowDom: true,
    tag: 'el-input-number',
    isFlex: true,
    tagIcon: 'number',
    placeholder: '请输入',
    defaultValue: undefined,
    controls: false,
    isShowUnit: false,
    unit: '',
    isShowSubTitle: false, //副标题
    subTitle: '',
    isShowThresholdTit: false,
    span: 24,
    labelWidth: null,
    min: undefined,
    max: undefined,
    step: undefined,
    'step-strictly': false,
    precision: undefined,
    'controls-position': '',
    disabled: false,
    required: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/input-number'
}, {
    layout: 'colFormItem',
    propertyName: '开关',
    tag: 'el-switch',
    isShowDom: true,
    labelType: 'switch',
    isFlex: true,
    tagIcon: 'switch',
    defaultValue: false,
    isShowSubTitle: false, //副标题
    subTitle: '',
    span: 24,
    labelWidth: null,
    disabled: false,
    required: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    // 'active-text': '',
    // 'inactive-text': '',
    'active-color': null,
    'inactive-color': null,
    'active-value': '1',
    'inactive-value': '0',
    params: {
        activeColor: null,
        inactiveColor: null,
        activeValue: null,
        inactiveValue: null,
    },
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/switch'
},
{
    layout: 'colFormItem',
    propertyName: '滑块',
    tag: 'el-slider',
    isShowDom: true,
    labelType: 'slider',
    isFlex: true,
    tagIcon: 'slider',
    defaultValue: null,
    isShowSubTitle: false, //副标题
    subTitle: '',
    span: 24,
    labelWidth: null,
    disabled: false,
    required: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    min: 0,
    max: 100,
    step: 1,
    'show-stops': false,
    range: false,
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/slider'
}, {
    layout: 'rowFormItem',
    isShowDom: true,
    tagIcon: 'row',
    labelType: 'form',
    type: 'flex',
    isShowSubTitle: false, //副标题
    subTitle: '',
    justify: 'start',
    align: 'top',
    formCode: 'formCode',
    formName: '问题组',
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    openImage: '0', // 是否开启图片上传   默认开启是否开启APP端展示  默认开启
    openMultiple: '0', //是否动态添加
    imageAction: '',
    span:24,
    children: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/layout'
}, {
    layout: 'colFormItem',
    propertyName: '说明文字',
    isShowDom: true,
    labelType: 'Caption',
    assemblyType: 'tool',
    tag: 'com-caption',
    isFlex: true,
    tagIcon: 'language',
    isShowSubTitle: false, //副标题
    subTitle: '',
    defaultValue: 0,
    span: 24,
    labelWidth: null,
    type: "info",
    effect: 'light',
    description: '请设置说明文字',
    'show-icon': false,
    required: false,
    closable: false,
    isDoctorShow: true, //是否显示在医生端
    isPatientShow: true, //是否显示在患者端
    regValue: [],
}, {
    layout: 'colFormItem',
    propertyName: '评分',
    isShowDom: true,
    labelType: 'Rate',
    tag: 'el-rate',
    isFlex: true,
    tagIcon: 'rate',
    isShowSubTitle: false, //副标题
    subTitle: '',
    defaultValue: 0,
    span: 24,
    labelWidth: null,
    max: 5,
    'allow-half': false,
    'show-text': false,
    'show-score': false,
    params: {
        allowHalf: false,
        showScore: false,
    },
    disabled: false,
    required: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/rate'
},
{
    layout: 'colFormItem',
    propertyName: '省市县',
    tag: 'el-cascader',
    isShowDom: true,
    labelType: 'province',
    tagType: 'province',
    isFlex: true,
    tagIcon: 'cascader',
    placeholder: '请选择',
    isEditOption: false,
    isShowSubTitle: false, //副标题
    subTitle: '',
    defaultValue: [],
    span: 24,
    labelWidth: null,
    props: {
        props: {
            multiple: false,
            expandTrigger: 'click',
            value: 'code',
            label: 'name',
            children: 'children'
        }
    },
    disabled: false,
    clearable: true,
    filterable: true,
    required: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    dataType: 'static',
    labelKey: 'label',
    valueKey: 'value',
    separator: '/',
    options: [],
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/cascader'
}, {
    layout: 'colFormItem',
    propertyName: '科室选择器',
    isShowDom: true,
    labelType: 'Select',
    tagType: 'departments',
    tag: 'el-select',
    isFlex: true,
    tagIcon: 'select',
    placeholder: '请选择',
    defaultValue: undefined,
    span: 24,
    isEditOption: false,
    isShowSubTitle: false, //副标题
    subTitle: '',
    labelWidth: null,
    clearable: true,
    disabled: false,
    required: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    filterable: true,
    multiple: false,
    options: [],
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/select'
}, {
    layout: 'colFormItem',
    propertyName: '病种选择器',
    isShowDom: true,
    labelType: 'Select',
    tagType: 'diseases',
    tag: 'el-select',
    isFlex: true,
    tagIcon: 'select',
    placeholder: '请选择',
    defaultValue: undefined,
    span: 24,
    isEditOption: false,
    isShowSubTitle: false, //副标题
    subTitle: '',
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    labelWidth: null,
    clearable: true,
    disabled: false,
    required: true,
    filterable: true,
    multiple: false,
    options: [],
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/select'
}, {
    layout: 'colFormItem',
    propertyName: '民族选择器',
    isShowDom: true,
    labelType: 'Select',
    tagType: 'nation',
    tag: 'el-select',
    isFlex: true,
    tagIcon: 'select',
    placeholder: '请选择',
    defaultValue: undefined,
    span: 24,
    isEditOption: false,
    isShowSubTitle: false, //副标题
    subTitle: '',
    labelWidth: null,
    clearable: true,
    disabled: false,
    required: true,
    isDoctorShow: true,//是否显示在医生端
    isPatientShow: true,//是否显示在患者端
    filterable: true,
    multiple: false,
    options: [],
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/select'
},
{
    layout: 'colFormItem',
    propertyName: '时间计算器',
    tag: 'com-TimeNumeral',
    tagType: 'TIMENUMERAL',
    labelType: 'TIMENUMERAL',
    isShowDom: true,
    isFlex: true,
    tagIcon: 'date',
    placeholder: '请选择',
    type: 'date',
    isShowSubTitle: false, //副标题
    subTitle: '',
    calculationType: 'date',
    startDateLabel: '开始时间',
    endDateLabel: '结束时间',
    differenceLabel: '计算结果:',
    differenceUnit: '天',
    format:'yyyy-MM-dd',
    span: 24,
    labelWidth: null,
    clearable: true,
    isPatientShow: true, //是否显示在患者端
    isSetEndTime:true,//是否设置结束时间
    disabled: false,
    required: true,
    regValue: [],
}
]

export const selectComponents = [{
    layout: 'colFormItem',
    tagIcon: 'input',
    labelType: 'input',
    propertyName: '姓名',
    isShowSubTitle: false, //副标题
    subTitle: '',
    isShowDom: true,
    vModel: 'name',
    formId: 6,
    tag: 'el-input',
    isFlex: true,
    placeholder: '请输入',
    defaultValue: '',
    span: 24,
    clearable: true,
    prepend: '',
    append: '',
    'prefix-icon': 'el-icon-user',
    'suffix-icon': '',
    maxlength: 11,
    'show-word-limit': true,
    readonly: false,
    disabled: false,
    required: true,
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/input'
}, {
    layout: 'colFormItem',
    tagIcon: 'input',
    labelType: 'input',
    propertyName: '手机号',
    isShowSubTitle: false, //副标题
    subTitle: '',
    isShowDom: true,
    vModel: 'mobile',
    formId: 6,
    tag: 'el-input',
    isFlex: true,
    placeholder: '请输入',
    defaultValue: '',
    span: 24,
    clearable: true,
    prepend: '',
    append: '',
    'prefix-icon': 'el-icon-mobile',
    'suffix-icon': '',
    maxlength: 11,
    'show-word-limit': true,
    readonly: false,
    disabled: false,
    required: true,
    regValue: [{
        pattern: '/^1(3|4|5|7|8|9)\\d{9}$/',
        message: '手机号格式错误'
    }],
    document: 'https://element.eleme.cn/#/zh-CN/component/input'
}, {
    layout: 'colFormItem',
    tagIcon: 'input',
    labelType: 'input',
    propertyName: '邮箱',
    isShowSubTitle: false, //副标题
    subTitle: '',
    isShowDom: true,
    vModel: 'mobile',
    formId: 6,
    tag: 'el-input',
    isFlex: true,
    placeholder: '请输入',
    defaultValue: '',
    span: 24,
    clearable: true,
    prepend: '',
    append: '',
    'prefix-icon': 'el-icon-message',
    'suffix-icon': '',
    'show-word-limit': true,
    readonly: false,
    disabled: false,
    required: true,
    regValue: [{
        pattern: '/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/',
        message: '邮箱格式错误'
    }],
    document: 'https://element.eleme.cn/#/zh-CN/component/input'
}, {
    layout: 'colFormItem',
    tagIcon: 'input',
    propertyName: '身份证号',
    isShowDom: true,
    labelType: 'input',
    isShowSubTitle: false, //副标题
    subTitle: '',
    vModel: 'mobile',
    formId: 6,
    tag: 'el-input',
    isFlex: true,
    placeholder: '请输入',
    defaultValue: '',
    span: 24,
    clearable: true,
    prepend: '',
    append: '',
    'prefix-icon': 'el-icon-postcard',
    'suffix-icon': '',
    'show-word-limit': true,
    readonly: false,
    disabled: false,
    required: true,
    regValue: [{
        pattern: '/^\d{15}|\d{}18$/',
        message: '身份证号格式错误'
    }],
    document: 'https://element.eleme.cn/#/zh-CN/component/input'
}, {
    layout: 'colFormItem',
    propertyName: '性别',
    isShowDom: true,
    labelType: 'Radio',
    tag: 'el-radio-group',
    isFlex: true,
    isEditOption: true,
    isShowSubTitle: false, //副标题
    subTitle: '',
    tagIcon: 'radio',
    defaultValue: undefined,
    span: 24,
    labelWidth: null,
    optionType: 'default',
    size: 'medium',
    disabled: false,
    required: true,
    options: [{
        label: '男',
        value: 'male',
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '女',
        value: 'female',
        isShowInputValue: false,
        inputType: ''
    }],
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/radio'
}, {
    layout: 'colFormItem',
    propertyName: '年龄段',
    isShowDom: true,
    labelType: 'Select',
    tag: 'el-select',
    isFlex: true,
    tagIcon: 'select',
    placeholder: '请选择',
    defaultValue: undefined,
    span: 24,
    isEditOption: true,
    isShowSubTitle: false, //副标题
    subTitle: '',
    labelWidth: null,
    clearable: true,
    disabled: false,
    required: true,
    filterable: false,
    multiple: false,
    options: [{
        label: '18岁以下',
        value: 1,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '18~25',
        value: 2,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '26~30',
        value: 3,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '31~40',
        value: 4,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '41~50',
        value: 5,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '51~60',
        value: 6,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '60以上',
        value: 7,
        isShowInputValue: false,
        inputType: ''
    }],
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/select'
}, {
    layout: 'colFormItem',
    propertyName: '行业',
    isShowDom: true,
    labelType: 'Select',
    tag: 'el-select',
    isFlex: true,
    tagIcon: 'select',
    placeholder: '请选择',
    defaultValue: undefined,
    span: 24,
    isEditOption: true,
    isShowSubTitle: false, //副标题
    subTitle: '',
    labelWidth: null,
    clearable: true,
    disabled: false,
    required: true,
    filterable: false,
    multiple: false,
    options: [{
        label: 'IT/软硬件服务/电子商务/因特网运营',
        value: 1,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '快速消费品(食品/饮料/化妆品)',
        value: 2,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '批发/零售',
        value: 3,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '服装/纺织/皮革',
        value: 4,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '家具/工艺品/玩具',
        value: 5,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '教育/培训/科研/院校',
        value: 6,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '家电',
        value: 7,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '通信/电信运营/网络设备/增值服务',
        value: 8,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '制造业',
        value: 9,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '汽车及零配件',
        value: 10,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '餐饮/娱乐/旅游/酒店/生活服务',
        value: 11,
        isShowInputValue: false,
        inputType: ''
    }],
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/select'
}, {
    layout: 'colFormItem',
    propertyName: '职业',
    isShowDom: true,
    labelType: 'Select',
    tag: 'el-select',
    isFlex: true,
    tagIcon: 'select',
    placeholder: '请选择',
    defaultValue: undefined,
    span: 24,
    isEditOption: true,
    isShowSubTitle: false, //副标题
    subTitle: '',
    labelWidth: null,
    clearable: true,
    disabled: false,
    required: true,
    filterable: false,
    multiple: false,
    options: [{
        label: '全日制学生',
        value: 1,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '生产人员',
        value: 2,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '销售人员',
        value: 3,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '市场/公关人员',
        value: 4,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '客服人员',
        value: 5,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '行政/后勤人员',
        value: 6,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '人力资源',
        value: 7,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '财务/审计人员',
        value: 8,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '文职/办事人员',
        value: 9,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '技术/研发人员',
        value: 10,
        isShowInputValue: false,
        inputType: ''
    }, {
        label: '管理人员',
        value: 11,
        isShowInputValue: false,
        inputType: ''
    }],
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/select'
},]

export const layoutComponents = [{
    layout: 'colFormItem',
    propertyName: '评分',
    isShowDom: true,
    labelType: 'Rate',
    tag: 'el-rate',
    isFlex: true,
    tagIcon: 'rate',
    defaultValue: 0,
    span: 24,
    labelWidth: null,
    max: 5,
    isShowSubTitle: false, //副标题
    subTitle: '',
    'allow-half': false,
    'show-text': false,
    'show-score': false,
    disabled: false,
    required: true,
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/rate'
},
{
    layout: 'colFormItem',
    propertyName: '颜色选择',
    isShowDom: true,
    labelType: 'ColorPicker',
    tag: 'el-color-picker',
    isFlex: true,
    tagIcon: 'color',
    isShowSubTitle: false, //副标题
    subTitle: '',
    defaultValue: null,
    labelWidth: null,
    'show-alpha': false,
    'color-format': '',
    disabled: false,
    required: true,
    size: 'medium',
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/color-picker'
},
{
    layout: 'colFormItem',
    propertyName: '上传',
    isShowDom: true,
    labelType: 'upload',
    tag: 'el-upload',
    isShowSubTitle: false, //副标题
    subTitle: '',
    isFlex: true,
    tagIcon: 'upload',
    action: '',
    defaultValue: null,
    labelWidth: null,
    disabled: false,
    required: true,
    accept: '',
    name: 'file',
    'auto-upload': true,
    showTip: true,
    buttonText: '点击上传',
    fileSize: 2,
    sizeUnit: 'MB',
    'list-type': 'text',
    multiple: false,
    limit: 9,
    regValue: [],
    document: 'https://element.eleme.cn/#/zh-CN/component/upload'
}, {
    layout: 'colFormItem',
    propertyName: '编辑器',
    isShowDom: true,
    labelType: 'tinymce',
    showLabel: false,
    labelWidth: null,
    isShowSubTitle: false, //副标题
    subTitle: '',
    tag: 'tinymce',
    isFlex: true,
    tagIcon: 'textarea',
    defaultValue: null,
    span: 24,
    required: true,
    regValue: [],
    placeholder: '请输入',
    height: 300, // 编辑器高度
    branding: false, // 隐藏右下角品牌烙印
    document: 'http://tinymce.ax-z.cn'
},
{
    layout: 'colFormItem',
    propertyName: '按钮',
    isShowDom: true,
    labelWidth: null,
    labelType: 'button',
    isShowSubTitle: false, //副标题
    subTitle: '',
    assemblyType: 'tool',
    tag: 'el-button',
    tagIcon: 'button',
    span: 24,
    default: '主要按钮',
    type: 'primary',
    icon: 'el-icon-search',
    size: 'medium',
    disabled: false,
    document: 'https://element.eleme.cn/#/zh-CN/component/button'
}, {
    layout: 'colFormItem',
    tagIcon: 'tool',
    assemblyType: 'tool',
    labelType: 'divider',
    propertyName: '分割线',
    isShowSubTitle: false, //副标题
    subTitle: '',
    isShowDom: true,
    vModel: 'divider',
    formId: 6,
    tag: 'el-divider',
    span: 20,
    class: 'solid',
    direction: 'horizontal',
    lineType: 'solid',
    document: 'https://element.eleme.cn/#/zh-CN/component/input'
},
    // {
    // propertyName: '表格',
    // isShowDom: true,
    //     layout: 'colFormItem',
    //     tag: 'com-table',
    //     divider: 'table',
    //     isFlex: true,
    //     tagIcon: 'code',
    //     defaultValue: null,
    //     required: true,
    //     labelWidth: null,
    //     isEditOption: true,
    // isShowSubTitle:false,//副标题
    // subTitle:'',
    //     tableAlignType: 'center',
    //     tableHeaders: [{
    //         props: 'name',
    //         label: '姓名',
    //     }, {
    //         props: 'date',
    //         label: '日期',
    //     }, {
    //         props: 'address',
    //         label: '地址',
    //     }],
    //     options: [{
    //         "日期": '2016-05-02',
    //         "姓名": '王小虎',
    //         "地址": '上海市普陀区金沙江路 1518 弄',
    //     }, {
    //         "日期": '2016-05-04',
    //         "姓名": '王小虎',
    //         "地址": '上海市普陀区金沙江路 1517 弄',
    //     }, {
    //         "日期": '2016-05-01',
    //         "姓名": '王小虎',
    //         "地址": '上海市普陀区金沙江路 1519 弄',
    //     }, {
    //         "日期": '2016-05-03',
    //         "姓名": '王小虎',
    //         "地址": '上海市普陀区金沙江路 1516 弄',
    //     }],
    //     document: 'https://element.eleme.cn/#/zh-CN/component/table',
    // },
    // {
    //     propertyName: '时间选择',
    // isShowDom: true,
    //     tag: 'el-time-picker',
    //     tagIcon: 'time',
    //     placeholder: '请选择',
    //     defaultValue: null,
    //     span: 24,
    // isShowSubTitle:false,//副标题
    // subTitle:'',
    //     labelWidth: null,
    //     disabled: false,
    //     clearable: true,
    //     required: true,
    //     'picker-options': {
    //         selectableRange: '00:00:00-23:59:59'
    //     },
    //     format: 'HH:mm',
    //     'value-format': 'HH:mm',
    //     regValue: [],
    //  
    //     document: 'https://element.eleme.cn/#/zh-CN/component/time-picker'
    // },
    // {
    //     propertyName: '时间范围',
    // isShowDom: true,
    //     tag: 'el-time-picker',
    //     tagIcon: 'time-range',
    //     defaultValue: null,
    //     span: 24,
    //     labelWidth: null,
    //     disabled: false,
    // isShowSubTitle:false,//副标题
    // subTitle:'',
    //     clearable: true,
    //     required: true,
    //     'is-range': true,
    //     'range-separator': '至',
    //     'start-placeholder': '开始时间',
    //     'end-placeholder': '结束时间',
    //     format: 'HH:mm',
    //     'value-format': 'HH:mm',
    //     regValue: [],
    //  
    //     document: 'https://element.eleme.cn/#/zh-CN/component/time-picker'
    // },
]





// 组件rule的触发方式，无触发方式的组件不生成rule
export const trigger = {
    'el-input': 'blur',
    'el-input-number': 'blur',
    'el-select': 'change',
    'el-radio-group': 'change',
    'el-checkbox-group': 'change',
    'el-cascader': 'change',
    'el-time-picker': 'click',
    'el-date-picker': 'click',
    'el-rate': 'change',
    "el-color-picker": 'change',
    "el-upload": 'change',
    "el-switch": 'change',
    'el-slider': 'change',
    tinymce: 'blur',
    'com-TimeNumeral': 'change',
}