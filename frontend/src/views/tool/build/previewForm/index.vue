<template>
    <el-row>
        <el-form class="preview_form" :model="previewFormModel" :rules="preViewFormRules" :ref="refKey">
            <ViewFromInfo :preview-form-data="nextPreviewFormFileds" :formModel="previewFormModel"
                @form-data-change="formItemChange">
            </ViewFromInfo>
        </el-form>
    </el-row>

</template>

<script>
import ViewFromInfo from './formInfo'
import { trigger } from '@/utils/generator/config'
import { convertFieldsDataLoop } from '@/utils/index'
import { deepClone } from '@/utils/index'
import * as math from 'mathjs';
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
                            itemC.isShowDom = this.convertIsShowDom(itemC, item.children, formList)
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
                        formObjArr[item.propertyCode] = item.defaultValue
                    }
                })
                // 计算多个分数统计，且当前分数值里有上面的分数统计参与运算
                // 扁平化处理，不区分组内组外，统一计算所有含公式的字段
                if (this.nextPreviewFormFileds && this.nextPreviewFormFileds.length > 0) {
                    let flatList = convertFieldsDataLoop(this.nextPreviewFormFileds)
                    let formulaItems = flatList.filter(f => f.formulaList && f.formulaList.length)
                    formulaItems.forEach(forItem => {
                        forItem.formScores = this.convertItemFormScores(forItem) || 0;
                        forItem.defaultValue = this.convertPropertyScoreVal(forItem, this.nextPreviewFormFileds) || 0
                        formObjArr[forItem.propertyCode] = forItem.defaultValue
                    })
                }
                // if(item.children&&item.children.length>0) {
                //     item.children.forEach( fItem => {
                //         let hasFormulaListOption = item.children.filter(f => f.formulaListOption &&
                //             f.formulaListOption.formulaList && f.formulaListOption.formulaList.length)
                //         if (hasFormulaListOption && hasFormulaListOption.length) {
                //             hasFormulaListOption.forEach((hasItemOption, hasItemOptionIndex) => {
                //                 if (hasItemOption.formulaListOption && hasItemOption.formulaListOption.formulaList.length) {
                //                     forItem.defaultValue = forItem.propertyValue = this.convertPropertyFormla(hasItemOption, item)
                //                 }
                //             })
                //         }
                //     })
                // }

                // let hasFormulaListOption = this.nextPreviewFormFileds.filter(f => f.formulaListOption &&
                //     f.formulaListOption.formulaList && f.formulaListOption.formulaList.length)
                // if (hasFormulaListOption && hasFormulaListOption.length) {
                //     hasFormulaListOption.forEach((hasItemOption, hasItemOptionIndex) => {
                //         this.nextPreviewFormFileds.forEach((forItem, forIndex, forFileds) => {
                //             if (forItem.formulaListOption && forItem.formulaListOption.formulaList.length) {
                //                 forItem.defaultValue = forItem.propertyValue = this.convertPropertyFormla(forItem, this.nextPreviewFormFileds)
                //             }
                //         })
                //     })
                // }
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
            let randomStr = new Date().getTime() + ''
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
                    if (item.labelType == 'form' && item.children && item.children.length) {
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
                if (this.$refs[this.refKey]) {
                    this.$refs[this.refKey].validateField(event.item.propertyCode)

                }
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
                            trigger: trigger[conf.tag] || 'change'
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
        convertIsShowDom(item, fields, parentFields) {
            let fieldsData = convertFieldsDataLoop(fields)
            let isShowDom = false;
            if (item.relationInfo && item.relationInfo.relationQuestion.length) {
                let relationQuestoinList = item.relationInfo.relationQuestion;
                relationQuestoinList.forEach((itemR, indexR) => {
                    let fieldsItem = fieldsData.find(f => f.propertyCode == itemR.questionId);
                    if (!fieldsItem) {
                        fieldsItem = parentFields.find(f => f.propertyCode == itemR.questionId);
                    }
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
         // 统计分数运算结果
        convertPropertyScoreVal(forItem, fields) {
            const formulaList = Array.isArray(forItem.formulaList) ? forItem.formulaList : []
            const flatFields = convertFieldsDataLoop(fields)
            if (!formulaList.length) {
                return 0
            }

            const result = this.parseFormulaAddSub(formulaList, 0, flatFields)
            const scoreVal = Number(result.value)
            return this.roundNumber(Number.isFinite(scoreVal) ? scoreVal : 0, 2)
        },
        parseFormulaAddSub(formulaList, startIndex, flatFields) {
            let current = this.parseFormulaMulDiv(formulaList, startIndex, flatFields)
            let currentIndex = current.index

            while (currentIndex < formulaList.length) {
                const token = formulaList[currentIndex]
                if (!token || token.type != 'operator' || !['+', '-'].includes(token.label)) {
                    break
                }
                const next = this.parseFormulaMulDiv(formulaList, currentIndex + 1, flatFields)
                current.value = token.label == '+'
                    ? Number(current.value) + Number(next.value)
                    : Number(current.value) - Number(next.value)
                currentIndex = next.index
            }

            return {
                value: current.value,
                index: currentIndex
            }
        },
        parseFormulaMulDiv(formulaList, startIndex, flatFields) {
            let current = this.parseFormulaPrimary(formulaList, startIndex, flatFields)
            let currentIndex = current.index

            while (currentIndex < formulaList.length) {
                const token = formulaList[currentIndex]
                if (!token || token.type != 'operator' || !['*', '/'].includes(token.label)) {
                    break
                }
                const next = this.parseFormulaPrimary(formulaList, currentIndex + 1, flatFields)
                current.value = token.label == '*'
                    ? Number(current.value) * Number(next.value)
                    : Number(next.value) === 0 ? 0 : Number(current.value) / Number(next.value)
                currentIndex = next.index
            }

            return {
                value: current.value,
                index: currentIndex
            }
        },
        parseFormulaPrimary(formulaList, startIndex, flatFields) {
            const token = formulaList[startIndex]
            if (!token) {
                return {
                    value: 0,
                    index: startIndex
                }
            }

            if (token.type == 'operator' && token.label == '(') {
                const inner = this.parseFormulaAddSub(formulaList, startIndex + 1, flatFields)
                return {
                    value: inner.value,
                    index: formulaList[inner.index] && formulaList[inner.index].label == ')'
                        ? inner.index + 1
                        : inner.index
                }
            }

            if (token.type == 'operator' && token.label == '-') {
                const next = this.parseFormulaPrimary(formulaList, startIndex + 1, flatFields)
                return {
                    value: -Number(next.value),
                    index: next.index
                }
            }

            if (token.type == 'operator' && token.label == '+') {
                return this.parseFormulaPrimary(formulaList, startIndex + 1, flatFields)
            }

            if (token.type == 'function' && token.label && token.label.indexOf('(') > -1) {
                return this.parseFormulaFunction(formulaList, startIndex, flatFields)
            }

            if (token.type == 'operator' && token.label == ')') {
                return {
                    value: 0,
                    index: startIndex
                }
            }

            return {
                value: this.getFormulaTokenValue(token, flatFields),
                index: startIndex + 1
            }
        },
        parseFormulaFunction(formulaList, startIndex, flatFields) {
            const token = formulaList[startIndex]
            const valueList = []
            let currentIndex = startIndex + 1

            while (currentIndex < formulaList.length) {
                const currentToken = formulaList[currentIndex]
                if (!currentToken) {
                    break
                }
                if (currentToken.label == ')') {
                    currentIndex++
                    break
                }
                if (currentToken.type == 'operator' && currentToken.label == '(') {
                    const inner = this.parseFormulaAddSub(formulaList, currentIndex + 1, flatFields)
                    valueList.push(inner.value)
                    currentIndex = formulaList[inner.index] && formulaList[inner.index].label == ')'
                        ? inner.index + 1
                        : inner.index
                    continue
                }
                if (currentToken.type == 'function' && currentToken.label && currentToken.label.indexOf('(') > -1) {
                    const innerFunction = this.parseFormulaFunction(formulaList, currentIndex, flatFields)
                    valueList.push(innerFunction.value)
                    currentIndex = innerFunction.index
                    continue
                }
                if (currentToken.type == 'operator') {
                    currentIndex++
                    continue
                }
                valueList.push(this.getFormulaTokenValue(currentToken, flatFields))
                currentIndex++
            }

            return {
                value: this.applyFormulaFunction(token.name, valueList),
                index: currentIndex
            }
        },
        getFormulaTokenValue(token, flatFields) {
            const target = flatFields.find(f => f.propertyCode == token.propertyCode)
            if (!target) {
                if (token.propertyCode) {
                    return 0
                }
                const labelValue = Number(token.label)
                return Number.isFinite(labelValue) ? labelValue : 0
            }
            if (target.labelType == 'CaseSpecification') {
                const descriptionValue = Number(target.description)
                return Number.isFinite(descriptionValue) ? descriptionValue : 0
            }
            const scoreValue = target.labelType == 'Statistic'
                ? target.defaultValue
                : this.convertItemFormScores(target)
            const numberValue = Number(scoreValue)
            return Number.isFinite(numberValue) ? numberValue : 0
        },
        applyFormulaFunction(name, valueList) {
            const numbers = valueList
                .map(item => Number(item))
                .filter(item => Number.isFinite(item))
            if (!numbers.length) {
                return 0
            }
            if (name == 'SUM') {
                return this.mathSum(numbers)
            }
            if (name == 'MAX') {
                return Math.max(...numbers)
            }
            if (name == 'MIN') {
                return Math.min(...numbers)
            }
            if (name == 'AVERAGE') {
                return this.mathSum(numbers) / numbers.length
            }
            if (name == 'ROUND') {
                return Math.round(numbers[0])
            }
            if (name == 'PRODUCT') {
                return numbers.reduce((total, value) => total * value, 1)
            }
            if (name == 'COUNT') {
                return numbers.length
            }
            if (name == 'MEDIAN') {
                const sorted = numbers.slice().sort((a, b) => a - b)
                const middle = Math.floor(sorted.length / 2)
                return sorted.length % 2 !== 0
                    ? sorted[middle]
                    : (sorted[middle - 1] + sorted[middle]) / 2
            }
            if (name == 'RANGE') {
                return Math.max(...numbers) - Math.min(...numbers)
            }
            return 0
        },
        convertItemFormScores(item) {
            let formScores = undefined;
            if (item.labelType == 'Checkbox' && Array.isArray(item.defaultValue) && item.defaultValue.length) {
                let scoresNum = []
                item.defaultValue.forEach(dealItem => {
                    const option = item.options && item.options.find(f => `${f.value}` == `${dealItem}`)
                    scoresNum.push(option ? Number(option.itemScores) || 0 : 0)
                })
                formScores = this.mathSum(scoresNum)
            } else if (item.labelType == 'Radio' || item.labelType == 'Select') {
                const option = item.defaultValue !== undefined && item.defaultValue !== null && item.options && item.options.length
                    ? item.options.find(f => `${f.value}` == `${item.defaultValue}`)
                    : undefined
                formScores = option ? Number(option.itemScores) || 0 : 0
            } else if (item.labelType == 'Checkbox' && (!Array.isArray(item.defaultValue) || !item.defaultValue.length)) {
                formScores = 0
            } else if (item.labelType == 'Input' && item.regValue && item.regValue.value == 'number') {
                formScores = Number.isFinite(Number(item.defaultValue)) ? Number(item.defaultValue) : 0
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
        //公式解析
        convertPropertyFormla(forItem, fields) {
            let valUpfieldsData = convertFieldsDataLoop(fields)
            let result = ''
            let valueList = []
            let formOption = forItem
            if (formOption.formulaList && formOption.formulaList.length) {
                formOption.formulaList.forEach((e, eIndex) => {
                    if (e.type == 'operator') {
                        valueList.push(e.label)
                        return
                    }
                    if (e.type != 'function') {
                        let itemIndex = valUpfieldsData.findIndex(f => f.propertyCode == e.value)
                        if (itemIndex > -1) {
                            valueList.push(Number(valUpfieldsData[itemIndex].propertyValue))
                            // {[valUpfieldsData[itemIndex].propertyCode] : valUpfieldsData[itemIndex].propertyValue}
                            // this.$set(object,[valUpfieldsData[itemIndex].propertyCode], valUpfieldsData[itemIndex].defaultValue )
                        } else {
                            valueList.push(e.value)
                        }
                    }
                })
            }
            let a = valueList.findIndex(m => m == null)
            if (a > -1) {
                // console.log('>-1')
            } else {
                let functionArr = ['SUM(', 'MAX(', 'MIN(', 'AVERAGE(', 'PRODUCT(', 'COUNT(', 'MEDIAN(', 'RANGE(']
                let itemType = functionArr.findIndex(f => f ==valueList[0])
                if(itemType > -1) {
                    console.log('itemType', itemType)

                    valueList = valueList.filter(m => typeof m === 'number')
                    if (functionArr[itemType] == 'SUM(') {
                        result = this.mathSum(valueList)
                    } else if (functionArr[itemType] == 'MAX(') {
                        result = math.max(...valueList)
                    } else if (functionArr[itemType] == 'MIN(') {
                        result = math.min(...valueList)
                    } else if (functionArr[itemType] == 'AVERAGE(') {
                        result = this.mathSum(valueList) / valueList.length
                    } else if (functionArr[itemType] == 'PRODUCT(') {
                        if (valueList.length) {
                            result = 1
                            valueList.forEach(socrInfo => {
                                result *= Number(socrInfo)
                            })
                        }
                    } else if (functionArr[itemType] == 'COUNT(') {
                        result = valueList.length
                    } else if (functionArr[itemType] == 'MEDIAN(') {
                        const sorted = valueList.slice().sort((a, b) => a - b)
                        const mid = Math.floor(sorted.length / 2)
                        result = sorted.length % 2 !== 0 ? sorted[mid] : (sorted[mid - 1] + sorted[mid]) / 2
                    } else if (functionArr[itemType] == 'RANGE(') {
                        result = valueList.length ? Math.max(...valueList) - Math.min(...valueList) : 0
                    }
                } else if (valueList.some(m => ['+', '-', '*', '/'].includes(m))) {
                    result = math.evaluate(valueList.join(''))
                } else {
                    // console.log(valueList.join(''))
                    result = math.evaluate(valueList.join(''))
                }
                // result = eval(valueList.join(''))
            }
            return result
        },
        evaluateExpression(expression, variables) {
            for (let variable in variables) {
                expression = expression.replace(new RegExp(variable, 'g'), variables[variable]);
                return eval(expression.toString());
            }
        },
        replaceSpance(value) {
            return value.replace(/\s+/g, ' '); // 多个空格则压缩
        },

    }
}
</script>
<style lang="scss" scoped>
@import "@/utils/generator/com-mixin.scss";

.preview_form {
    width: 100%;
    padding-top: 15px;

    ::v-deep .el-form-item {
        .el-form-item__label {
            display: flex;
            align-items: center;
            // width: 100%;
            text-align: left;
            margin-top: 5px;

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
