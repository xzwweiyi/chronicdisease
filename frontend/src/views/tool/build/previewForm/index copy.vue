<template>
    <el-row>
        <el-form class="preview_form" :model="previewFormModel" :rules="preViewFormRules" :ref="refKey">
            <ViewFromInfo :preview-form-data="nextPreviewFormFileds" :formModel="previewFormModel" @form-data-change="formItemChange">
            </ViewFromInfo>
        </el-form>
    </el-row>

</template>

<script>
import ViewFromInfo from './formInfo'
import { trigger } from '@/utils/generator/config'
import { convertFieldsDataLoop } from '@/utils/index'
import { deepClone } from '@/utils/index'
export default {
    name: 'preview-form',
    props: ['previewFormData'],
    components: { ViewFromInfo },
    data() {
        return {
            preViewFormRules: undefined,
            customTagList: ['com-BMI', 'com-TimeNumeral', 'com-Maternity', 'com-popQ', 'com-DValue', 'com-complication',
                'com-yearMonthDay', 'com-questionnaire', 'com-urineFlowRate', 'com-IUGAICS', 'com-preOperPopQ'],
            arrayValTagList: ['el-checkbox-group', 'el-cascader'],
            previewFormModel: {},
            nextPreviewFormFileds: [],
            refKey: 'refKeys',
        }
    },
    watch: {
        previewFormData: {
            immediate: true,
            deep: true,
            handler(newVal, oldVal) {
                if (newVal) {
                    this.refKey = 'refKeys' + Math.random().toString(36).substring(2, 5)
                    this.nextPreviewFormFileds = deepClone(newVal)
                    this.convertFormItemInfo()
                    this.previewFormModel = this.convertPreviewFormModel()
                }
            }
        },
    },
    computed: {},
    mounted() { },
    destroyed() { },
    methods: {
        convertPreviewFormModel() {
            let formObjArr = {}
            if (this.nextPreviewFormFileds && this.nextPreviewFormFileds.length) {
                let formList = convertFieldsDataLoop(this.nextPreviewFormFileds)
                this.nextPreviewFormFileds.forEach((item, index) => {
                    if (item.labelType == 'form' && item.children && item.children.length) {
                        item.isShowDom = this.convertIsShowDom(item, formList)
                        item.children.forEach((itemC, indexC) => {
                            itemC.isShowDom = this.convertIsShowDom(itemC, formList)
                            if (!itemC.isShowDom) {
                                if (Array.isArray(itemC.defaultValue)) {
                                    itemC.defaultValue = []
                                    itemC.options.forEach(optItem => {
                                        optItem.disabled = false
                                    })
                                } else {
                                    itemC.defaultValue = undefined
                                }
                            }
                            if (itemC.labelType == 'CaseSpecification') {
                                itemC.description = this.convertItemDescription(itemC, this.nextPreviewFormFileds)
                            }
                            itemC.formScores = this.convertItemFormScores(itemC) || 0;
                            if (itemC.labelType == 'Statistic' && itemC.formulaList && itemC.formulaList.length) {
                                itemC.defaultValue = this.convertPropertyScoreVal(itemC, this.nextPreviewFormFileds)
                            }
                            formObjArr[itemC.propertyCode] = itemC.defaultValue
                        })
                    } else if (item.labelType !== 'form') {
                        item.isShowDom = this.convertIsShowDom(item, formList)
                        if (!item.isShowDom) {
                            if (Array.isArray(item.defaultValue)) {
                                item.defaultValue = []
                                item.options.forEach(optItem => {
                                    optItem.disabled = false
                                })
                            } else {
                                item.defaultValue = undefined
                            }
                        }
                        if (item.labelType == 'CaseSpecification') {
                            item.description = this.convertItemDescription(item, this.nextPreviewFormFileds)
                        }
                        formObjArr[item.propertyCode] =  item.defaultValue
                    }
                })
                // 计算多个分数统计，且当前分数值里有上面的分数统计参与运算
                let hasFormulaListArr = this.nextPreviewFormFileds.filter(f => f.formulaList && f.formulaList.length)
                if (hasFormulaListArr && hasFormulaListArr.length) {
                    hasFormulaListArr.forEach((hasItem, index) => {
                        this.nextPreviewFormFileds.forEach((forItem, forIndex, forFileds) => {
                            forItem.formScores = this.convertItemFormScores(forItem) || 0;
                            if (forItem.formulaList && forItem.formulaList.length) {
                                forItem.defaultValue = this.convertPropertyScoreVal(forItem, this.nextPreviewFormFileds)
                            }
                        })
                    })
                }
            }
            // console.log('formObjArr', formObjArr)
            return formObjArr
        },
        formItemChange(event) {
            // console.log('formItemChange event', event)

            let defaultVal = event.value
            let propertyVal = event.value
            let unitValStr = ''
            if (this.customTagList.findIndex(f => f == event.item.tag) > -1 && defaultVal) {
                defaultVal = JSON.parse(event.value)
            }
            if (this.arrayValTagList.findIndex(f => f == event.item.tag) > -1 && defaultVal) {
                propertyVal = event.value.toString()
            }
            if ((event.item.tag == 'el-input' || event.item.tag == 'el-input-number') && event.item.unitVal) {
                let jsonStr = JSON.parse(event.value)
                defaultVal = Number(jsonStr.content)
                unitValStr = jsonStr.unit
            }
            if (event.item.tag == 'el-checkbox' && !Array.isArray(defaultVal)) {
                defaultVal = defaultVal.split(',')
            }
            let medicalInfoArray = this.nextPreviewFormFileds
            let currentIndex = this.nextPreviewFormFileds.findIndex(f => f.propertyCode == event.item.propertyCode)
            let randomStr = new Date().getTime()+''
            if (currentIndex > -1) {
                medicalInfoArray[currentIndex].propertyValue = propertyVal
                medicalInfoArray[currentIndex].defaultValue = defaultVal
		medicalInfoArray[currentIndex].renderKey = randomStr
                let labelList = ['Checkbox', 'Radio', 'Select', 'Input', 'Statistic', 'CaseSpecification']
                if (labelList.findIndex(f => f == medicalInfoArray[currentIndex].labelType) > -1) {
                    medicalInfoArray[currentIndex].formScores = this.convertItemFormScores(medicalInfoArray[currentIndex]) || 0;
                }
            } else {
                medicalInfoArray.forEach(item => {
                    if (item.labelType == 'form' && item.children&&item.children.length) {
                        let childIndex = item.children.findIndex(f => f.propertyCode == event.item.propertyCode)
                        if (childIndex > -1) {
                            item.children[childIndex].propertyValue = propertyVal;
                            item.children[childIndex].defaultValue = defaultVal
                            item.children[childIndex].renderKey = randomStr
                        }
                    }
                })
            }
            this.nextPreviewFormFileds = medicalInfoArray
            this.previewFormModel = this.convertPreviewFormModel()
            this.$nextTick(() => {
                this.$refs[this.refKey].validateField('' + event.item.propertyCode)
            })
        },
        convertFormItemInfo() {
            let ruleList = {}
            let fileds = [];
            if (this.nextPreviewFormFileds && this.nextPreviewFormFileds.length) {
                fileds = deepClone(this.nextPreviewFormFileds)
                let formList = convertFieldsDataLoop(fileds)
                formList.forEach(conf => {
                    // debugger
                    let itemRule = []
                    if (conf.required) {
                        let message = Array.isArray(conf.defaultValue) ? `请至少选择一个选项` : conf.placeholder
                        if (message === undefined) message = `${conf.propertyName}的值不能为空`
                        const tagArr = ['com-Maternity', 'com-TimeNumeral', 'com-BMI', 'com-yearMonthDay']
                        if (tagArr.findIndex(f => f == conf.tag) > -1) {
                            message = `${conf.propertyName}的值不能为空`
                        }
                        let ruleInfo = {
                            required: true,
                            message,
                            trigger: trigger[conf.tag]||'change'
                            // trigger: 'change'
                        }
                        if (Array.isArray(conf.defaultValue)) {
                            ruleInfo.type = 'array'
                        }
                        itemRule.push(ruleInfo)
                    }
                    if (conf.regValue && Array.isArray(conf.regValue)) {
                        conf.regValue.forEach(item => {
                            if (item.pattern) {
                                itemRule.push({
                                    pattern: item.pattern,
                                    message: item.message,
                                    trigger: trigger[conf.tag]
                                })
                            }
                        })
                    }
                    if (itemRule.length) {
                        ruleList[conf.propertyCode] = itemRule
                    }
                    // console.log(conf.propertyCode, conf.propertyName, itemRule)
                })
                this.preViewFormRules = ruleList
            }

        },
        // 是否显示
        convertIsShowDom(item, fields) {
            let fieldsData = convertFieldsDataLoop(fields)
            let isShowDom = false;
            if (item.relationInfo && item.relationInfo.relationQuestion.length) {
                let relationQuestoinList = item.relationInfo.relationQuestion;
                relationQuestoinList.forEach((itemR, indexR) => {
                    let fieldsItem = fieldsData.find(f => f.propertyCode == itemR.questionId);
                    let aggregate = [];
                    let valueList = fieldsItem && fieldsItem.defaultValue || []
                    if (fieldsItem && (fieldsItem.tag == "el-radio-group" || fieldsItem.tag ==
                        'el-checkbox-group' || fieldsItem.tag == 'el-select')) {
                        if (!Array.isArray(valueList)) {
                            valueList = [valueList]
                        }
                        if (itemR.checkedOptions && itemR.checkedOptions.length) {
                            itemR.checkedOptions.forEach((checkItem, checkIndex) => {
                                let checkItemString = checkItem.toString()
                                if (valueList.indexOf(checkItemString) > -1 || valueList.findIndex(f => f == checkItem) > -1) {
                                    aggregate.push(checkIndex)
                                }
                            })
                        }
                    } else if (fieldsItem && fieldsItem.tag == 'el-switch') {
                        if (itemR.checkedOptions && itemR.checkedOptions.length) {
                            itemR.checkedOptions.forEach((checkItem, checkIndex) => {
                                if (valueList.indexOf(checkItem) > -1) {
                                    aggregate.push(checkIndex)
                                }
                            })
                        }
                    } else if (fieldsItem && fieldsItem.defaultValue) {
                        aggregate.push(true)
                    }
                    if (aggregate.length) {
                        isShowDom = true;
                    } else if (fieldsItem && (fieldsItem.tag == 'el-upload') &&
                        fieldsItem.defaultValue && fieldsItem.defaultValue.length) {
                        isShowDom = true;
                    } else {
                        isShowDom = false;
                    }
                })
            } else {
                isShowDom = true;
            }
            return isShowDom
        },
        convertItemDescription(forItem, fields) {
            let valUpfieldsData = convertFieldsDataLoop(fields)
            let description = ''
            if (forItem.installmentList && forItem.installmentList.length) {
                forItem.installmentList.forEach((aa, ii) => {
                    let correlations = valUpfieldsData.filter(f => aa.conditions.findIndex(fi =>
                        fi.propertyCode == f.propertyCode) > -1).map(m => {
                            return {
                                propertyCode: m.propertyCode,
                                selectVal: m.defaultValue,
                                // selectVal:this.${confGlobal.formModel}[m.propertyCode]
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
                            return a.propertyCode.toLowerCase().localeCompare(b.propertyCode.toLowerCase());
                        })
                        newConditions.sort((a, b) => {
                            return a.propertyCode.toLowerCase().localeCompare(b.propertyCode.toLowerCase());
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
        convertPropertyScoreVal(forItem, fields) {
            let valUpfieldsData = convertFieldsDataLoop(fields)
            let scoreVal = 0;
            let valueList = []
            let funStartI = forItem.formulaList.findIndex(f => f.label.indexOf('(') > -1 && f.type == "function")
            let funEndI = forItem.formulaList.findIndex(f => f.label.lastIndexOf(')') > -1 && f.type == "function")
            forItem.formulaList.forEach((e, eIndex) => {
                if (e.type != 'function' && e.type != 'operator' && eIndex > funStartI && eIndex < funEndI) {
                    let itemIndex = valUpfieldsData.findIndex(f => f.propertyCode == e.propertyCode &&
                        (Array.isArray(f.defaultValue) && f.defaultValue.length
                            || !Array.isArray(f.defaultValue) && f.defaultValue ||
                            f.labelType == 'CaseSpecification' && f.description))
                    if (itemIndex > -1) {
                        valueList.push(valUpfieldsData[itemIndex].formScores)
                    }
                }
            })
            valueList = valueList.map(m => Number(m))
            if (forItem.formulaList[0].name == 'SUM') {
                scoreVal = this.mathSum(valueList)
            } else if (forItem.formulaList[0].name == 'MAX') {
                scoreVal = Math.max(...valueList)
            } else if (forItem.formulaList[0].name == 'MIN') {
                scoreVal = Math.min(...valueList)
            } else if (forItem.formulaList[0].name == 'AVERAGE') {
                scoreVal = this.mathSum(valueList) / valueList.length
            } else if (forItem.formulaList[0].name == 'ROUND') {
                scoreVal = Math.round(...valueList)
            } else if (forItem.formulaList[0].name == 'PRODUCT') {
                if (valueList.length) {
                    scoreVal = 1
                    valueList.forEach(socrInfo => {
                        scoreVal *= Number(socrInfo)
                    })
                }
            }
            if (funEndI < forItem.formulaList.length - 1) {
                let otherFormula = forItem.formulaList.slice(funEndI + 1)
                otherFormula.forEach(other => {
                    const numbers = other.label.split(/[*]|[+]|[-]|[\/]/).filter(e => e);
                    const operators = other.label.split(/[^-|+|\/|\*]/).filter(e => e);
                    if (operators.length) {
                        operators.forEach((oper, operI) => {
                            if (numbers[operI] && Number(numbers[operI]) != NaN) {
                                switch (true) {
                                    case oper == '+':
                                        scoreVal = scoreVal + Number(numbers[operI]);
                                        break;
                                    case oper == '-':
                                        scoreVal = scoreVal - numbers[operI];
                                        break;
                                    case oper == '*':
                                        scoreVal = scoreVal * numbers[operI];
                                        break;
                                    case oper == '/':
                                        scoreVal = scoreVal / numbers[operI];
                                        break;
                                }
                            }
                        })
                    }
                })
            }
            return this.roundNumber(scoreVal, 2)
            // return scoreVal
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
            } else if (item.labelType == 'Input' && item.regValue && item.regValue.value == 'number') {
                formScores = Number(item.defaultValue) != NaN ? Number(item.defaultValue) : 0
            } else if (item.labelType == 'Statistic') {
                formScores = item.defaultValue || 0
            } else if (item.labelType == 'CaseSpecification') {
                formScores = item.description
            }
            return formScores
        },
        mathSum(arr) {
            return arr.reduce(function (total, value) {
                return total + value;
            }, 0);
        },
        roundNumber(number, decimals) {
            const factor = 10 ** decimals;
            return Math.round(number * factor) / factor;
        },

    }
}
</script>
<style lang="scss" scoped>
@import "@/utils/generator/com-mixin.scss";
.preview_form {
    width: 100%;
    ::v-deep .el-form-item {
        .el-form-item__label {
            display: flex;
            align-items: center;
            // width: 100%;
            text-align: left;
            margin-top:5px;

            &::before {
                line-height: 2;
            }
        }
        &.form_item_row {
            .el-form-item__label {
                width: 30%;
            }
            .el-form-item__content {
                width: 100%;
                .el-slider {
                    width: 96%;
                }
                .caption_content {
                    min-height: 30px;
                }
            }
        }
    }
}
</style>
