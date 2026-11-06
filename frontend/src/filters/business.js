const _checkIsNull = function (val) {
    // isNaN 在检测字符串时候也为true
    // return val == undefined || val === null || isNaN(val) || val.toString().trim() === '';
    return val == undefined || val === null || val.toString().trim() === '';
}
export default {
    convertDeviceConfig(value, type) {
        try {
            console.log(value, type)
            value = JSON.parse(value);
            return value[type] || '';
        } catch (e) {
            console.log(e);
            return '';
        }
    },
    convertTagTypeStr(status) {
        const type = {
            'Radio': '单选框组', 'Checkbox': '多选框组', 'Select': '下拉选择', 'Cascader': '级联选择',
            'province': '级联选择器', 'Input': '单行文本', 'Textarea': '多行文本', 'timePicker': '时间选择',
            'Date': '日期选择', 'Upload': '上传图片', 'upload': '上传文件', 'InputNumber': '计数器',
            'switch': '开关', 'slider': '滑块',
        }
        return _checkIsNull(status) ? '--' : type[status] ? type[status] : '--'
    },
    convertAgentStr(status) {
        const arrayStemp = [
            { value: 0, label: '未知' },
            { value: 1, label: '男' },
            { value: 2, label: '女' }
        ];
        return status && arrayStemp.find(f => f.value == status) ? arrayStemp.find(f => f.value == status).label : '--'
    },
    convertArticleType(type) {
        const arrayStemp = { 1: '文章', 2: '问卷', 3: '视频', 4: '分类', }
        return arrayStemp[type] || '--'
    },
    // 
    convertArticleDelFlag(type) {
        const arrayStemp = { 'N': '正常', "Y": '失效', 'n': '正常', "y": '失效' }
        return arrayStemp[type] || '--'
    },
    convertArticleIndexType(type) {
        const arrayStemp = { '0': '无', "1": '轮播图', '2': '首页列表' }
        return arrayStemp[type] || '--'
    },
    convertOutboundEndCode(type) {
        const arrayStemp = {
            1: '正常回答', 2: '自动留言', 3: '接听不便', 4: '居民已死亡', 5: '家属不能代答', 6: '不愿配合', 7: '号码错误', 8: '中断'
        }
        return arrayStemp[type] || '--'
    },
    convertOutboundResult(type) {
        const arrayStemp = {
            0: '成功', 2: '通话中', 3: '无法接通', 4: '关机', 5: '用户正忙',
            6: '空号', 7: '号码错误', 9: '停机', 15: '客户接听后并主动挂机', 20: '用户未接',
            22: '来电提醒转来电提醒', 23: '呼入限制', 26: '网络故障', 28: '线路故障', 30: '呼叫失败', 300: '任务执行失败'
        }
        return arrayStemp[type] || '--'
    },
    // 文化程度 
    convertDegreeOfEducation(type) {
        const arrayStemp = {
            1: '研究生', 2: '大学本科', 3: '大学专科和专科学校', 4: '中等专业学校', 5: '技工学校', 6: '高中',
            7: '初中', 8: '小学', 9: '文盲或半文盲', 10: '不详'
        }
        return arrayStemp[type] || '--'
    },
    convertAbo(type) {
        const arrayStemp = {
            1: 'A型', 2: 'B型', 3: 'O型', 4: 'AB型', 5: '不详'
        }
        return arrayStemp[type] || '--'
    },
    // 患者状态
    reversePatientStatus(info) {
        let statusList = ['未填写', '基线', '随访', '完成', '失访']
        return statusList[info] || '--'
    },
    // 随访患者状态
    convertHandleStatus(info) {
        let statusList = ['', '更新随访时间', '忽略本次', '失访']
        return statusList[info] || '--'
    },
    // 是否绑定
    convertBindStatus(info) {
        let statusList = ['未绑定', '已绑定']
        return statusList[info] || '--'
    },
    // 日期格式 yyyy-MM-dd
    dateOnlyFormat(val) {
        if (!val) return '--'
        let date = new Date(val)
        let y = date.getFullYear()
        let m = (date.getMonth() + 1).toString().padStart(2, '0')
        let d = date.getDate().toString().padStart(2, '0')
        return `${y}-${m}-${d}`
    },

}