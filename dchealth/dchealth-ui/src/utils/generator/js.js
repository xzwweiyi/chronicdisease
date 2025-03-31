import {
    exportDefault,
    titleCase
} from '@/utils/index'
import {
    trigger,
} from './config'

const units = {
    KB: '1024',
    MB: '1024 / 1024',
    GB: '1024 / 1024 / 1024'
}
let confGlobal
const inheritAttrs = {
    file: '',
    dialog: 'inheritAttrs: false,'
}



export function makeUpJs(conf, type) {
    confGlobal = conf = JSON.parse(JSON.stringify(conf))
    const dataList = []
    const ruleList = []
    const optionsList = []
    const propsList = []
    const methodList = mixinMethod(type)
    const uploadVarList = []
    const watchList = []
    const computedList = []

    conf.fields.forEach(el => {
        buildAttributes(el, dataList, ruleList, optionsList, methodList, propsList, uploadVarList, watchList, computedList)
    })

    const script = buildexport(
        conf,
        type,
        dataList.join('\n'),
        computedList.join('\n'),
        ruleList.join('\n'),
        optionsList.join('\n'),
        uploadVarList.join('\n'),
        propsList.join('\n'),
        watchList.join('\n'),
        methodList.join('\n'),
    )
    confGlobal = null
    return script
}

function buildAttributes(el, dataList, ruleList, optionsList, methodList, propsList, uploadVarList, watchList, computedList) {
    buildData(el, dataList)
    buildRules(el, ruleList)

    if (el.options && el.options.length) {
        buildOptions(el, optionsList)
    }
    if (el.tag === 'el-date-picker') {
        buildPickerOptions(el, optionsList)
    }

    if (el.props && el.props.props) {
        buildProps(el, propsList)
    }
    if (el.tag === 'com-caption') {
        buildvalDescription(el, propsList)
    }

    if (el.tag === 'el-upload') {
        uploadVarList.push(
            `${el.vModel}Action: '${el.action}', ${el.vModel}fileList: [],`
        )

        methodList.push(buildBeforeUpload(el))
        if (!el['auto-upload']) {
            methodList.push(buildSubmitUpload(el))
        }
    }
    if (el.tag === 'com-BMI') {
        methodList.push(buildBMIChange(el))
    }
    if (el.tag === 'com-TimeNumeral') {
        methodList.push(buildTimeNumeralChange(el))
        buildTimeNumeralData(el, propsList)
    }
    if (el.tag === 'com-Maternity') {
        methodList.push(buildMaternityChange(el))
        buildMaternityModel(el, propsList)
    }
    if (el.tag === 'el-checkbox-group') {
        methodList.push(buildCheckboxChange(el))
    }
    if (el.tag === 'com-popQ') {
        methodList.push(buildPopQInputNumberChange(el))
        buildPopQModel(el, propsList)
        buildPopQWatchData(el, watchList)
        buildPopComputed(el, computedList)
    }

    if (el.children) {
        el.children.forEach(el2 => {
            buildAttributes(el2, dataList, ruleList, optionsList, methodList, propsList, uploadVarList, watchList, computedList)
        })
    }
}

function mixinMethod(type) {
    const list = [];
    const minxins = {
        file: confGlobal.formBtns ? {
            submitForm: `submitForm() {
                this.$refs['${confGlobal.formRef}'].validate(valid => {
                if(!valid) return
                // TODO 提交表单
                })
            },`,
            resetForm: `resetForm() {
                this.$refs['${confGlobal.formRef}'].resetFields()
            },`,
            formItemChange: `formItemChange(itemKey, itemVal){
                let fields = this.confGlobalData.fields;
                fields.forEach((item, index) => {
                    if (item.vModel == itemKey) {
                        item.defaultValue = itemVal[itemKey]
                        if(item.labelType == 'TIMENUMERAL'){
                            item.startTimeVal = this[item.vModel+'Start']
                            item.endTimeVal = this[item.vModel+'End']
                        }
                    }
                    // item.isShowDom = this.convertIsShowDom(item, fields)
                    let labelList = ['Checkbox','Radio','Select','Input','Statistic','CaseSpecification']
                    if(labelList.findIndex(f=>f==item.labelType)>-1){
                        item.formScores = this.convertItemFormScores(item)||0;
                    }
                    if (item.labelType == 'form' && item.children && item.children.length) {
                        item.children = this.convertFormRelation(itemKey, itemVal, item.children)
                    }
                })
                fields.forEach(forItem => {
                    if (forItem.labelType == 'CaseSpecification') {
                        this['${confGlobal.formModel}'][forItem.vModel] = this.convertItemDescription(forItem,fields)
                        forItem.defaultValue = this['${confGlobal.formModel}'][forItem.vModel]
                    }
                })
                this.$nextTick(() => {
                    this.$parent.updatePreviewData(fields)
                })
            },
            returnItemInfo(code,fields){
                let fieldsData = this.convertFieldsDataLoop(this.confGlobalData.fields)
                if(fieldsData.findIndex(f=>f.propertyCode==code)>-1){
                    return fieldsData.find(f=>f.propertyCode==code)
                }
            },
            convertFormRelation(itemKey, itemVal, fields) {
                fields.forEach((item, index) => {
                    if (item.vModel == itemKey) {
                        item.defaultValue = itemVal[itemKey]
                    }
                    if (item.labelType == 'form' && item.children && item.children.length) {
                        item.children = this.convertFormRelation(itemKey, itemVal, item.children)
                    }
                })
                fields.forEach(forItem => {
                    if (forItem.labelType == 'CaseSpecification') {
                        this['${confGlobal.formModel}'][forItem.vModel] = this.convertItemDescription(forItem,fields)
                    }
                })
                return fields
            },
            convertItemFormScores(item) {
                let formScores = undefined;
                if (item.labelType == 'Checkbox' && item.defaultValue.length) {
                    let scoresNum = []
                    item.defaultValue.forEach(dealItem => {
                        scoresNum.push(item.options.find(f => f.value == dealItem).itemScores)
                    })
                    formScores = this.mathSum(scoresNum)
                } else if (item.labelType == 'Radio' || item.labelType == 'Select') {
                    formScores = item.defaultValue && item.options && item.options.length
                        && item.options.find(f => f.value == item.defaultValue) ?
                        item.options.find(f => f.value == item.defaultValue).itemScores : 0
                } else if (item.labelType == 'Checkbox' && !item.defaultValue.length) {
                    formScores = 0
                }
                return formScores
            },
            roundNumber(number, decimals) {
                const factor = 10 ** decimals;
                return Math.round(number * factor) / factor;
            },
            convertItemDescription(forItem, fields) {
                let valUpfieldsData = this.convertFieldsDataLoop(fields)
                let description = ''
                if (forItem.installmentList && forItem.installmentList.length) {
                    forItem.installmentList.forEach((aa, ii) => {
                        let correlations = valUpfieldsData.filter(f => aa.conditions.findIndex(fi =>
                            fi.propertyCode == f.propertyCode) > -1).map(m => {
                                return {
                                    propertyCode: m.propertyCode,
                                    // selectVal: m.defaultValue,
                                    selectVal:this.${confGlobal.formModel}[m.propertyCode]
                                }
                            })
                        let newConditions = aa.conditions.map(m => {
                            return {
                                propertyCode: m.propertyCode,
                                selectVal: m.selectVal,
                            }
                        })
                        if (correlations.length == newConditions.length) {
                            correlations.sort((a, b) => {
                                return a.propertyCode - b.propertyCode
                            })
                            newConditions.sort((a, b) => {
                                return a.propertyCode - b.propertyCode
                            })
                            if (JSON.stringify(correlations) == JSON.stringify(newConditions)) {
                                description = aa.ruleResult
                                return
                            }
                        }
                    })
                }
                return description
            },
            mathSum(arr) {
                return arr.reduce(function (total, value) {
                    return total + value;
                }, 0);
            },
            convertFieldsDataLoop(array, parentIndex) {
                return array.reduce((prev, curr, index) => {
                    return prev.concat(Array.isArray(curr.children) ? this.convertFieldsDataLoop(curr.children, index) :
                        curr)
                }, [])
            },
            addMultipleForm(event){
                let fields = this.confGlobalData.fields;
                let currentIndex = fields.findIndex(f=>f.formCode==event);
                if(currentIndex>-1){
                    let addFormInfo = this.deepClone(fields.find(f=>f.formCode==event));
                    if (addFormInfo.children && addFormInfo.children.length) {
                        addFormInfo.children.forEach(item => {
                            item.propertyCode = item.propertyCode + Math.random().toString(36).substring(2, 5)
                        })
                    }
                    addFormInfo.openMultiple = undefined;
                    addFormInfo.isDeleteForm = true;
                    addFormInfo.formCode = Math.random().toString(36).substring(2, 8)
                    if(currentIndex>-1){
                        fields.splice(currentIndex+1,0,addFormInfo)
                    }
                }else{
                    fields.forEach((item,index)=>{
                        if(item.children&&item.children.length){
                            let childIndex = item.children.findIndex(f=>f.formCode==event);
                            if(childIndex>-1){
                                item.children = this.addChildrenMultiple(event,item.children)
                            }
                        }
                    })
                }
                this.$parent.updatePreviewData(fields)
            },
            addChildrenMultiple(event,children){
                let childIndex = children.findIndex(f=>f.formCode==event);
                let addFormInfo = this.deepClone(children.find(f=>f.formCode==event));
                if (addFormInfo.children && addFormInfo.children.length) {
                    addFormInfo.children.forEach(item => {
                        item.propertyCode = item.propertyCode + Math.random().toString(36).substring(2, 5)
                    })
                }
                addFormInfo.openMultiple = undefined;
                addFormInfo.isDeleteForm = true;
                addFormInfo.formCode = Math.random().toString(36).substring(2, 8)
                children.splice(childIndex+1,0,addFormInfo)
                return children
            },
            deleteFormMultiple(event){
                // console.log('event',event)
                let fields = this.confGlobalData.fields;
                let currentIndex = fields.findIndex(f=>f.formCode==event);
                if(currentIndex>-1){
                    fields.splice(currentIndex,1)
                }else{
                    fields.forEach((item,index)=>{
                        if(item.children&&item.children.length){
                            let childIndex = item.children.findIndex(f=>f.formCode==event);
                            if(childIndex>-1){
                                item.children.splice(childIndex,1)
                            }
                        }
                    })
                }
                this.$parent.updatePreviewData(fields)
            },
            deepClone(source) {
                if (!source && typeof source !== 'object') {
                    throw new Error('error arguments', 'deepClone')
                }
                const targetObj = source.constructor === Array ? [] : {}
                Object.keys(source).forEach(keys => {
                    if (source[keys] && typeof source[keys] === 'object') {
                        targetObj[keys] = this.deepClone(source[keys])
                    } else {
                        targetObj[keys] = source[keys]
                    }
                })
                return targetObj
            },
            convertCheckboxDisabled(event,list,groupOption){
                let checkboxIndex = list.findIndex(f=>f.propertyCode == event)
                if(checkboxIndex>-1){
                    list.find(f=>f.propertyCode == event).options = groupOption
                }else{
                    list.forEach(item=>{
                        if(item.children&&item.children.length){
                            item.children = this.convertCheckboxDisabled(event,item.children,groupOption)
                        }
                    })
                }
                return list
            },`
        } : null,
    }

    const methods = minxins[type]
    if (methods) {
        Object.keys(methods).forEach(key => {
            list.push(methods[key])
        })
    }

    return list
}

function buildData(conf, dataList) {
    if (conf.vModel === undefined) return
    let defaultValue
    if (typeof (conf.defaultValue) === 'string' && !conf.multiple) {
        defaultValue = `'${conf.defaultValue}'`
    } else {
        defaultValue = `${JSON.stringify(conf.defaultValue)}`
    }
    dataList.push(`${conf.vModel}: ${defaultValue},`)
}
function buildTimeNumeralData(conf,propsList){
    if (conf.vModel === undefined) return
    propsList.push(`${conf.vModel}Start: '${conf.startTimeVal||''}',`)
    propsList.push(`${conf.vModel}End: '${conf.endTimeVal||''}',`)
    propsList.push(`${conf.vModel}StartPickerOptions: {
        disabledDate:time=>{
            let endDate = '';
            if (${conf.isSetEndTime}) {
                endDate = this.${conf.vModel}End
            } else {
                endDate = new Date()
            }
            if (endDate) {
                if ('${conf.type}' == 'date') {
                    return time.getTime() > endDate.getTime();
                } else if ('${conf.type}' == 'month') {
                    return this.convertMonthDate(time) > this.convertMonthDate(endDate);
                } else if ('${conf.type}' == 'year') {
                    return time.getFullYear() > endDate.getFullYear();
                }
            } else {
                return false
            }
        }},`)
    propsList.push(`${conf.vModel}EndPickerOptions: '',`)
}

function buildPickerOptions(conf, optionsList) {
    if (conf.vModel === undefined) return
    if (conf.pickerOptions && (conf.pickerOptions.minDate || conf.pickerOptions.maxDate)) {
        optionsList.push(`${conf.vModel}PickerOptions:{
            disabledDate: time => {
                let minDate = ${conf.pickerOptions&&conf.pickerOptions.minDate?new Date(conf.pickerOptions.minDate).getTime():null}
                let maxDate = ${conf.pickerOptions&&conf.pickerOptions.maxDate?new Date(conf.pickerOptions.maxDate).getTime():null}
                if(minDate && !maxDate){
                    return new Date(time).getTime() < minDate-24*60*60*1000
                }else if(!minDate && maxDate){
                    console.log(maxDate,new Date(time).getTime())
                    return new Date(time).getTime() > maxDate
                }else if(minDate && maxDate){
                    return new Date(time).getTime() < minDate-24*60*60*1000 || new Date(time).getTime() > maxDate
                }
                return false
            }
        },`)
    }

}

function buildRules(conf, ruleList) {
    if (conf.vModel === undefined) return
    const rules = []

    if (trigger[conf.tag]) {
        if (conf.required) {
            const type = Array.isArray(conf.defaultValue) ? 'type: \'array\',' : ''
            // let message = Array.isArray(conf.defaultValue) ? `请至少选择一个${conf.vModel}` : conf.placeholder
            let message = Array.isArray(conf.defaultValue) ? `请至少选择一个选项` : conf.placeholder
            if (message === undefined) message = `${conf.propertyName}的值不能为空`
            const tagArr = ['com-Maternity', 'com-TimeNumeral', 'com-BMI', 'com-yearMonthDay']
            if (tagArr.findIndex(f => f == conf.tag) > -1) {
                message = `${conf.propertyName}的值不能为空`
            }
            rules.push(`{ required: true, ${type} message: '${message}', trigger: '${trigger[conf.tag]}' }`)
        }
        if (conf.regValue && Array.isArray(conf.regValue)) {
            conf.regValue.forEach(item => {
                if (item.pattern) {
                    rules.push(`{ pattern: ${eval(item.pattern)}, message: '${item.message}', trigger: '${trigger[conf.tag]}' }`)
                }
            })
        }
        ruleList.push(`${conf.vModel}: [${rules.join(',')}],`)
    }
}

function buildOptions(conf, optionsList) {
    if (conf.vModel === undefined) return
    if (conf.dataType === 'dynamic') {
        conf.options = []
    }
    let str = `${conf.vModel}Options: ${JSON.stringify(conf.options)},`

    optionsList.push(str)
}

function buildPopQWatchData(conf, watchList) {
    const str = `${conf.vModel}Options(newVal) {
        if (newVal.find(f => f.vModel == 'popD')) {
            if (newVal.find(f => f.vModel == 'popD').isShowDom == false) {
                newVal.find(f => f.vModel == 'prolapse').propertyName = '穹隆脱垂'
            } else if (newVal.find(f => f.vModel == 'popD').isShowDom == true) {
                newVal.find(f => f.vModel == 'prolapse').propertyName = '子宫脱垂'
            }
        }
    },
    pelvicOrgansDefVal(val) {
        let formInfo = this.${conf.vModel}Form
        if (val) {
            let gradeArr = {
                '0': 0,
                I: 1,
                II: 2,
                III: 3,
                IV: 4
            }
            let stempArr = [gradeArr[formInfo.prolapse], gradeArr[formInfo.anteriorVaginalWall], gradeArr[
                formInfo.posteriorVaginalWall]]
            stempArr.sort((a, b) => b - a)
            const result = Object.entries(gradeArr).find(([key, val]) => val === stempArr[0]);
            formInfo.pelvicOrgans = result[0]
            this.${conf.vModel}Form = formInfo
        }
    },`
    watchList.push(str)
}

function buildPopComputed(conf, computedList) {
    const str = `pelvicOrgansDefVal(){
        let formInfo = this.${conf.vModel}Form
            if (formInfo.prolapse && formInfo.anteriorVaginalWall && formInfo.posteriorVaginalWall) {
                return true
            } else {
                return false
            }
        },`
    computedList.push(str)
}

function buildProps(conf, propsList) {
    const str = `${conf.vModel}Props: ${JSON.stringify(conf.props.props)},`
    propsList.push(str)
}

function buildvalDescription(conf, propsList) {
    let str = ''
    if (conf.tag === 'com-caption') {
        str = `${conf.vModel}description:${JSON.stringify(conf.description)},`

    } else if (conf.tag === 'com-yearMonthDay') {
        // 1.日期 2 周岁 3 月龄 4 天
        str = `${conf.vModel}specialComponentType :'1',`
    }
    propsList.push(str)
}

function buildPopQModel(conf, propsList) {
    const objMap = new Map()
    const rulesMap = new Map()
    conf.options.forEach(item => {
        let itemRule = [{
            required: item.required,
            message: `${item.propertyName}不能为空`,
            trigger: 'blur'
        }]
        objMap.set(item.vModel, undefined)
        if (item.required) {
            rulesMap.set(item.vModel, itemRule)
        }
    })
    let formModel = Object.fromEntries(objMap)
    let formRules = Object.fromEntries(rulesMap)
    
    let formModelJSON = '{';
    for (let key in formModel) {
        formModelJSON += '"' + key + '":' + formModel[key] + ',';
    }
    formModelJSON = formModelJSON.slice(0, -1);
    formModelJSON += '}';
    const str = `${conf.vModel}Form:${formModelJSON},${conf.vModel}Rules:${JSON.stringify(formRules)},`
    propsList.push(str)
}
function buildMaternityModel(conf, propsList){
    const str = `${conf.vModel}OptionInfo: {undefined},${conf.vModel}embryo:{},`
    propsList.push(str)
}

function buildyearMonthDayMethod(conf) {
    const str = `${conf.vModel}ValChange(event,index){
        if(this.${confGlobal.formModel}.${conf.vModel}[index]){
            this.${conf.vModel}specialComponentType = index+1
        }else{
            this.${conf.vModel}specialComponentType = -1
        }
        if(this.${confGlobal.formModel}.${conf.vModel}.length){
            this.${confGlobal.formModel}.${conf.vModel}.forEach((item,i)=>{
                if(i==index){
                    item = event
                }else {
                    item = null
                }
            })
        }
    },`
    return str
}

function buildBeforeUpload(conf) {
    const unitNum = units[conf.sizeUnit];
    let rightSizeCode = '';
    let acceptCode = '';
    const returnList = []
    if (conf.fileSize) {
        rightSizeCode = `let isRightSize = file.size / ${unitNum} < ${conf.fileSize}
        if(!isRightSize){
        this.$message.error('文件大小超过 ${conf.fileSize}${conf.sizeUnit}')
        }`
        returnList.push('isRightSize')
    }
    if (conf.accept) {
        acceptCode = `let isAccept = new RegExp('${conf.accept}').test(file.type)
        if(!isAccept){
        this.$message.error('应该选择${conf.accept}类型的文件')
        }`
        returnList.push('isAccept')
    }
    const str = `${conf.vModel}BeforeUpload(file) {
        ${rightSizeCode}
        ${acceptCode}
        return ${returnList.join('&&')}
    },
    ${conf.vModel}Change(file,fileList){
        this.confGlobalData.fields.find(f=>f.propertyCode == '${conf.propertyCode}').defaultValue = fileList
        this.${confGlobal.formModel}['${conf.vModel}'] = fileList
    },`
    return returnList.length ? str : ''
}

function buildBMIChange(conf) {
    const str = `${conf.vModel}BMIChange(){
        let list = this.${conf.vModel}Options
        if (list.findIndex(f => f.code && !f.value) == -1) {
            let weight = Number(list.find(f => f.code == 'weight').value);
            let height = Number(list.find(f => f.code == 'height').value);
            let BMIvalue = weight / (height * height/10000)
            let info = {...this.${conf.vModel}Options[2],value:BMIvalue.toFixed(2)}
            this.${conf.vModel}Options[2] = info
            this.${confGlobal.formModel}['${conf.vModel}'] = info.value
        }
    },`
    return str
}

function buildTimeNumeralChange(conf) {
    const str = `${conf.vModel}DateChange(event,changeType){
        let differenceValue = ''
        if(event){
            if(changeType=='1'){
                if (event && ${conf.isSetEndTime} && this.${conf.vModel}End) {
                    differenceValue = this.convertDifferenceVal(event,this.${conf.vModel}End,'${conf.type}')
                } else if (event && !${conf.isSetEndTime}) {
                    differenceValue =this.convertDifferenceVal(event,null,'${conf.type}')
                } else {
                    differenceValue = ''
                }
                this.${confGlobal.formModel}.${conf.vModel} = differenceValue
            }else if(changeType=='2'){
                if (event && ${conf.isSetEndTime} && this.${conf.vModel}Start) {
                    differenceValue = this.convertDifferenceVal(this.${conf.vModel}Start,event,'${conf.type}')
                } else {
                    differenceValue = ''
                }
                this.${confGlobal.formModel}.${conf.vModel} = differenceValue
            } else if (changeType == '3' && !${conf.isSetEndTime} && !this.${conf.vModel}Start) {
                // let date = new Date();
                // let m = date.getMonth() - event + 1;
                // date.setMonth(m);
                // this.${conf.vModel}Start = date.getFullYear() + '-' + date.getMonth();
            }
        }
        this.convert${conf.vModel}PickerOptions(this.${conf.vModel}Start,this.${conf.vModel}End)
    },
    convertDifferenceVal(startDate,endDate,type) {
        let startDateTime = new Date(''+startDate)
        let endDateTime = endDate?new Date(''+endDate):new Date()
        if (!endDate) {
            let newdate = new Date()
            let year = newdate.getFullYear()  // 得到当前年份 
            let month = newdate.getMonth() + 1 // 得到当前月份（0-11月份，+1是当前月份）
            month = month > 10 ? month : '0' + month
            endDateTime = type == 'date' ? newdate : type == 'month' ? new Date(year + '-' + month + '-01')
                : type == 'year' ? new Date(year + '-01') : new Date()
        }
        let differenceVal = endDateTime.getTime() - startDateTime.getTime()
        if (type == 'date') {
            differenceVal = Number(differenceVal / 1000 / 60 / 60 / 24)
        } else if (type == 'month') {
            differenceVal = Number(differenceVal / 1000 / 60 / 60 / 24 / 30)
        } else if (type == 'year') {
            differenceVal = Number(endDateTime.getFullYear() - startDateTime.getFullYear())
        }
        return Math.trunc(differenceVal)
    },
    convertMonthDate(value) {
        let date = new Date(value)
        let year = date.getFullYear()
        let month = date.getMonth() + 1
        month = month < 10 ? ('0' + month) : month
        let curTimeStr = year + "-" + month
        let curTime = new Date(curTimeStr)
        return curTime.getTime()
    },
    convert${conf.vModel}PickerOptions(startTime,endTime){
        this.${conf.vModel}StartPickerOptions = {
            disabledDate:time=>{
                let endDate = '';
                if (${conf.isSetEndTime}) {
                    endDate = new Date(endTime)
                } else {
                    endDate = new Date()
                }
                if (endDate) {
                    if ('${conf.type}' == 'date') {
                        return time.getTime() > endDate.getTime();
                    } else if ('${conf.type}' == 'month') {
                        return this.convertMonthDate(time) > this.convertMonthDate(endDate);
                    } else if ('${conf.type}' == 'year') {
                        return time.getFullYear() > endDate.getFullYear();
                    }
                } else {
                    return false
                }
            }
        }
        this.${conf.vModel}EndPickerOptions = {
            disabledDate:time=>{
                let startDate = new Date(startTime);
                if (startTime) {
                    if ('${conf.type}' == 'date') {
                        return time.getTime() < startDate.getTime();
                    } else if ('${conf.type}' == 'month') {
                        return this.convertMonthDate(time) < this.convertMonthDate(startDate);
                    } else if ('${conf.type}' == 'year') {
                        return time.getFullYear() < startDate.getFullYear();
                    }
                } else {
                    return false
                }
            }
        }
    },`
    return str
}

function buildMaternityChange(conf) {
    const str = `${conf.vModel}NumberChange(event){
        let stempOptions = this.${conf.vModel}Options
        const {
            embryo,
            ...otherInfo
        } = stempOptions[0]
        if(!this.${conf.vModel}OptionInfo){
            this.${conf.vModel}OptionInfo = JSON.parse(JSON.stringify(otherInfo))
        }
        if(this.${conf.vModel}embryo=={}){
            this.${conf.vModel}embryo = embryo
        }
        if (event >= 2) {
            if (stempOptions.length < event) {
                let stempArr = new Array(event - stempOptions.length).toString().split(',')
                    .map((item, index) => {
                        return index
                    });
                    let newArray = []
                stempArr.forEach(item => {
                    newArray.push(JSON.parse(JSON.stringify(this.${conf.vModel}OptionInfo)))
                })
                stempOptions = stempOptions.concat(newArray)
            } else {
                stempOptions = stempOptions.slice(0,event)
            }
        } else if(event == 1) {
            stempOptions = [JSON.parse(JSON.stringify(this.${conf.vModel}OptionInfo))]
        }else {
            stempOptions = []
        }
        this.${conf.vModel}Options = stempOptions
    },
    ${conf.vModel}BirthHistoryChange(event,eventIndex){
        let newOptions = this.${conf.vModel}Options[eventIndex].birthHistory.options
        if(event.length){
            let currentIndex = newOptions.findIndex(f=>f.value==='无')
            if(event.findIndex(f=>f=='无')>-1){
                newOptions.forEach((item,index)=>{
                    if(currentIndex==index){
                        item.disable = false
                    }else{
                        item.disable = true
                    }
                })
            }else{
                newOptions.forEach((item,index)=>{
                    if(currentIndex==index){
                        item.disable = true
                    }else{
                        item.disable = false
                    }
                })
            }
        }else{
            newOptions.forEach(item=>{
                item.disable = false
            })
        }
        this.${conf.vModel}Options[eventIndex].birthHistory.options = JSON.parse(JSON.stringify(newOptions))
    },`
    return str
}

function buildCheckboxChange(conf) {
    const str = `${conf.vModel}Change(event){
        let groupOption = this.${conf.vModel}Options;
        let newArray = groupOption.filter(f=>event.findIndex(e=>f.value==e)>-1)
        let disabledArr = newArray.length&&newArray[0].mutualExclusion
        if(disabledArr&&disabledArr.length){
            disabledArr.forEach(item=>{
                groupOption.find(f=>f.value==item)['disabled'] = true
            })
        }else{
            groupOption.forEach(item=>{
                if(item.mutualExclusion){
                    let itemDisabled = false
                    event.forEach(info=>{
                        if(item.mutualExclusion.findIndex(f=>f==info)>-1){
                            itemDisabled = true
                        }
                    })
                    item.disabled = itemDisabled
                }else{
                    item.disabled = false
                }
            })
        }
        this.confGlobalData.fields = this.convertCheckboxDisabled('${conf.propertyCode}',this.confGlobalData.fields,groupOption)
    },`
    return str
}

function buildPopQInputNumberChange(conf) {
    const str = `${conf.vModel}inputNumberChange(){
        let formInfo = this.${conf.vModel}Form
        if (formInfo.popAa != '' && formInfo.popBa != '' && formInfo.popAp != '' && formInfo.popBp != '' &&
            formInfo.popC != '' && formInfo.popD != '' && formInfo.popPb != '' && formInfo.popGh != '' &&
            formInfo.popTVL != '' && formInfo.popAa != undefined && formInfo.popBa != undefined &&
            formInfo.popAp != undefined && formInfo.popBp != undefined && formInfo.popC != undefined &&
            formInfo.popD != undefined && formInfo.popPb != undefined && formInfo.popGh != undefined &&
            formInfo.popTVL != undefined) {
            let anteriorNum = formInfo.popAa > formInfo.popBa ? formInfo.popAa : formInfo.popBa
            formInfo.anteriorVaginalWall = this.convertGrade(anteriorNum)

            let posteriorNum = formInfo.popAp > formInfo.popBp ? formInfo.popAp : formInfo.popBp
            formInfo.posteriorVaginalWall = this.convertGrade(posteriorNum)

            let prolapseNum = ''
            if (this.${conf.vModel}Options.find(f => f.vModel == 'popD').isShowDom == true) {
                prolapseNum = formInfo.popC > formInfo.popD ? formInfo.popC : formInfo.popD
            } else {
                prolapseNum = formInfo.popC
            }
            formInfo.prolapse = this.convertGrade(prolapseNum)
        }
        this.${conf.vModel}Form = formInfo
    },
    convertGrade(quantizer) {
        let grade = '';
        let popQForm = this.${conf.vModel}Form
        if (quantizer <= -(popQForm.popTVL - 2)) {
            grade = '0'
        } else if (quantizer < -1) {
            grade = 'I'
        } else if (quantizer >= -1 && quantizer <= 1) {
            grade = 'II'
        } else if (quantizer > 1 && quantizer < popQForm.popTVL - 2) {
            grade = 'III'
        } else if (quantizer >= popQForm.popTVL-2) {
            grade = 'IV'
        }
        return grade
    },`
    return str
}

function buildSubmitUpload(conf) {
    const str = `submitUpload() {
    this.$refs['${conf.vModel}'].submit()
  },`
    return str
}

function buildexport(conf, type, data, computedList, rules, selectOptions, uploadVar, props, buildWatch, methods) {
    let confGlobalData = `confGlobalData:${JSON.stringify(conf)},`
    const str = `${exportDefault}{
        ${inheritAttrs[type]}
        components: {},
        props: [],
        data () {
            return {
                ${conf.formModel}: {
                    ${data}
                },
                ${confGlobalData}
                ${conf.formRules}: {
                    ${rules}
                },
                ${uploadVar}
                ${selectOptions}
                ${props}
                ${conf.formModel}score:0
            }
        },
        computed: {
            newFormObj(){
                return JSON.parse(JSON.stringify(this.${conf.formModel}));
          },
          ${computedList}
        },
        watch: {
            // 监听form表单的变化
            newFormObj:{
                immediate: true, // 监听到后，立即执行 handler方法
                handler(newVal,oldVal){
                    if(newVal&&oldVal){
                        for(var k in newVal){
                             if(newVal[k]&&oldVal[k]&&newVal[k].toString() != oldVal[k].toString()
                                ||!newVal[k]&&oldVal[k]||newVal[k]&&!oldVal[k]){
                                this.formItemChange(k,newVal)
                            }
                        }
                    }
                },
                deep:true
            },
            ${buildWatch}
        },
        created () {},
        mounted () {},
        methods: {
            ${methods}
        }
    }`
    return str
}
