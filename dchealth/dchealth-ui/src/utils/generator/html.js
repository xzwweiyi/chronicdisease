/* eslint-disable max-len */
import {
    trigger
} from './config'

let confGlobal
let someSpanIsNot24

export function dialogWrapper(str) {
    return `<el-dialog v-bind="$attrs" class="custom_dialog" top="8vh" v-on="$listeners" @open="onOpen" @close="onClose" title="">
    ${str}
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确定</el-button>
    </div>
  </el-dialog>`
}

export function vueTemplate(str) {
    return `<template>
    <div>
      ${str}
    </div>
  </template>`
}

export function vueScript(str) {
    return `<script>
    ${str}
  </script>`
}

export function cssStyle(cssStr) {
    return `<style>
    ${cssStr}
  </style>`
}

function buildFormTemplate(conf, child, type) {
    let labelPosition = ''
    if (conf.labelPosition !== 'right') {
        labelPosition = `label-position="${conf.labelPosition}"`
    }
    const disabled = conf.disabled ? `:disabled="${conf.disabled}"` : ''
    let str = `<el-form ref="${conf.formRef}" :model="${conf.formModel}" :rules="${conf.formRules}"
         ${disabled} label-width="${conf.labelWidth}px" ${labelPosition} class="preview_form_content">
      ${child}
      ${buildFromBtns(conf, type)}
    </el-form>`
    // if (someSpanIsNot24) {
    str = `<el-row>
        ${str}
      </el-row>`
    // }
    return str
}

function buildFromBtns(conf, type) {
    let str = ''
    if (conf.formBtns && type === 'file') {
        str = `<div v-if="${conf.formModel}score">答题总分：{{${conf.formModel}score}}</div>`
        // if (someSpanIsNot24) {
        str = `<el-col :span="24">
            ${str}
            </el-col>`
        // }
    }
    return str
}

// span不为24的用el-col包裹
function colWrapper(element, str) {
    // if (someSpanIsNot24 || element.span !== 24) {
    return `<el-col :span="${element.span}" class="drawing-item">
            ${str}
        </el-col>`
    // }
    return str
}

function reverseComputedList(computedList, element) {
    let list = [];
    computedList.forEach(item => {
        if (item.labelType == 'form' && item.children && item.children.length) {
            let index = item.children.findIndex(f => f.propertyCode == element.propertyCode);
            if (index > -1) {
                list = item.children.slice(0, index)
            } else {
                list = reverseComputedList(item.children, element)
            }
        }
    })
    return list
}

function convertFieldsDataLoop(array, parentIndex) {
    return array.reduce((prev, curr, index) => {
        return prev.concat(Array.isArray(curr.children) ? convertFieldsDataLoop(curr.children, index) :
            curr)
    }, [])
}
function convertIsShowDom(item, fields) {
    let fieldsData = convertFieldsDataLoop(fields)
    let isShowDom = false;
    if (item.relationInfo && item.relationInfo.relationQuestion.length) {
        let relationQuestoinList = item.relationInfo.relationQuestion;
        relationQuestoinList.forEach((itemR, indexR) => {
            let fieldsItem = fieldsData.find(f => f.propertyCode == itemR.questionId);
            let aggregate = [];
            let valueList = fieldsItem && fieldsItem.defaultValue || []
            if (fieldsItem && (fieldsItem.labelType == 'Radio' || fieldsItem.labelType == 'Checkbox' ||
                fieldsItem.labelType == 'Select')) {
                if (!Array.isArray(valueList)) {
                    valueList = [valueList]
                }
                if (itemR.checkedOptions && itemR.checkedOptions.length) {
                    itemR.checkedOptions.forEach((checkItem, checkIndex) => {
                        if (valueList.indexOf(checkItem) > -1) {
                            aggregate.push(checkIndex)
                        }
                    })
                }
            } else if (fieldsItem && fieldsItem.labelType == 'switch') {
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
}
const layouts = {
    colFormItem(element) {
        let labelWidth = ''
        if (element.labelWidth && element.labelWidth !== confGlobal.labelWidth) {
            labelWidth = `label-width="${element.labelWidth}px"`
        }
        let label = element.propertyName;
        if (element.assemblyType == 'tool' || element.labelType == 'Caption' || label == ' ' || element.isHideName) {
            label = ''
        }

        // ===========   判断问题
        if (element.isShowDom != false) {
            element.isShowDom = true;
        }

        element.isShowDom = convertIsShowDom(element, confGlobal.fields)
        if (!element.isShowDom) {
            if (Array.isArray(element.defaultValue)) {
                element.defaultValue = []
            } else {
                element.defaultValue = undefined
            }
        }
        const required = !trigger[element.tag] && element.required ? 'required' : ''
        const tagDom = tags[element.tag] ? tags[element.tag](element) : null
        let str = `<el-form-item ${labelWidth} label='` + label + `' prop="${element.vModel}" ${required} 
                class="${element.isFlex ? '' : 'display-flex'}" v-show="${element.isShowDom}">
                <p class="subTitle_style" v-if="${element.isShowSubTitle}">${element.subTitle}</p>
                <el-tooltip placement="top" v-if="${element.isExplanatoryNote}" effect="light">
                    <div slot="content">${element.explanatoryNote}</div>
                    <el-button plain type="text" class="form_explanatory_note" icon="el-icon-info"></el-button>
                </el-tooltip>
                <div class='display-flex render_content'>
                    ${tagDom}
                </div>
            </el-form-item>`
        str = colWrapper(element, str)
        return str
    },
    rowFormItem(element) {
        const type = element.type === 'default' ? '' : `type="${element.type}"`
        const justify = element.type === 'default' ? '' : `justify="${element.justify}"`
        const align = element.type === 'default' ? '' : `align="${element.align}"`

        const children = element.children && element.children.length ? element.children.map(el => layouts[el.layout](el)) : ''
        // ===========   判断问题
        if (element.isShowDom != false) {
            element.isShowDom = true;
        }
        element.isShowDom = convertIsShowDom(element, confGlobal.fields)
        if (!element.isShowDom) {
            if (Array.isArray(element.defaultValue)) {
                element.defaultValue = []
            } else {
                element.defaultValue = undefined
            }
        }

        let str = `<el-row ${type} ${justify} ${align} v-show="${element.isShowDom}" class="form_row_style">
            <div class="open_multiple">
                <el-button type="text" v-if="${element.openMultiple == '1'}" @click="addMultipleForm('${element.formCode}')" 
                    >新增</el-button>
                <el-button type="text" class="el-button-text_delete" v-if="${element.isDeleteForm}" 
                @click="deleteFormMultiple('${element.formCode}')">删除</el-button>
            </div>
            <el-tooltip placement="top" v-if="${element.isExplanatoryNote}" effect="light">
                <div slot="content">${element.explanatoryNote}</div>
                <el-button plain type="text" class="form_explanatory_note" icon="el-icon-info"></el-button>
            </el-tooltip>
            <p class="subTitle_style" v-if="${!element.isHideName}">${element.formName}</p>
            <p class="subTitle_style" v-if="${element.isShowSubTitle}">${element.subTitle}</p>
            ${element.openImage != '1' && children ? children.join('\n') : ''}
            ${element.openImage == '1' ?
                `<el-upload style="margin-bottom:10px;" list-type="picture-card" action="${element.imageAction}" accept="image/*">
                <i slot="default" class="el-icon-plus"></i>
            </el-upload>`
                : ''}
        </el-row>`
        str = colWrapper(element, str)
        return str
    },
}

const tags = {
    'el-button': el => {
        const {
            tag,
            disabled
        } = attrBuilder(el)
        const type = el.type ? `type="${el.type}"` : ''
        const icon = el.icon ? `icon="${el.icon}"` : ''
        const size = el.size ? `size="${el.size}"` : ''
        let child = buildElButtonChild(el)

        if (child) child = `\n${child}\n` // 换行
        return `<${el.tag} ${type} ${icon} ${disabled}>${child}</${el.tag}>`
    },
    'el-input': el => {
        const {
            disabled,
            vModel,
            clearable,
            placeholder,
            width
        } = attrBuilder(el)
        const maxlength = el.maxlength ? `:maxlength="${el.maxlength}"` : ''
        const showWordLimit = el['show-word-limit'] ? 'show-word-limit' : ''
        const readonly = el.readonly ? 'readonly' : ''
        const prefixIcon = el['prefix-icon'] ? `prefix-icon='${el['prefix-icon']}'` : ''
        const suffixIcon = el['suffix-icon'] ? `suffix-icon='${el['suffix-icon']}'` : ''
        const showPassword = el['show-password'] ? 'show-password' : ''
        const type = el.type ? `type="${el.type}"` : ''
        const autosize = el.autosize && el.autosize.minRows ?
            `:autosize="{minRows: ${el.autosize.minRows}, maxRows: ${el.autosize.maxRows}}"` : ''
        const min = el.min ? `:min='${el.min}'` : ''
        const max = el.max ? `:max='${el.max}'` : ''
        let child = buildElInputChild(el)

        if (child) child = `\n${child}\n` // 换行
        return `<${el.tag} ${vModel} ${type} ${placeholder} ${maxlength} ${showWordLimit} 
        ${readonly} ${disabled} ${clearable} ${prefixIcon} ${suffixIcon} ${showPassword} 
        ${autosize} ${width} ${min} ${max}>${child}</${el.tag}>
        <span class="unit_info" v-if="${el.unit != undefined}">${el.unit}</span>`
    },
    'el-input-number': el => {
        const {
            disabled,
            vModel,
            placeholder
        } = attrBuilder(el)
        const controlsPosition = el['controls-position'] ? `controls-position=${el['controls-position']}` : ''
        const min = el.min ? `:min='${el.min}'` : ''
        const max = el.max ? `:max='${el.max}'` : ''
        const step = el.step ? `:step='${el.step}'` : ''
        const stepStrictly = el['step-strictly'] ? 'step-strictly' : ''
        const precision = el.precision ? `:precision='${el.precision}'` : ''

        return `<${el.tag} ${vModel} ${placeholder} ${step} ${stepStrictly} ${precision} ${controlsPosition}
         ${min} ${max} ${disabled} :controls=${el.controls}></${el.tag}>
         <span class="unit_info" v-if="${el.unit != undefined}">${el.unit}</span>`
    },
    'el-select': el => {
        const {
            disabled,
            vModel,
            clearable,
            placeholder,
            width
        } = attrBuilder(el)
        const filterable = el.filterable ? 'filterable' : ''
        const multiple = el.multiple ? 'multiple' : ''
        let child = buildElSelectChild(el)

        if (child) child = `\n${child}\n` // 换行
        return `<${el.tag} ${vModel} ${placeholder} ${disabled} ${multiple} ${filterable} ${clearable} ${width}>${child}</${el.tag}>`
    },
    'el-radio-group': el => {
        const {
            disabled,
            vModel
        } = attrBuilder(el)
        const size = `size="${el.size}"`
        let child = buildElRadioGroupChild(el)

        if (child) child = `\n${child}\n` // 换行
        return `<${el.tag} ${vModel} ${disabled}>${child}</${el.tag}>`
    },
    'el-checkbox-group': el => {
        const {
            disabled,
            vModel
        } = attrBuilder(el)
        const size = `size="${el.size}"`
        const min = el.min ? `:min="${el.min}"` : ''
        const max = el.max ? `:max="${el.max}"` : ''
        let child = buildElCheckboxGroupChild(el)

        if (child) child = `\n${child}\n` // 换行
        return `<${el.tag} ${vModel} ${min} ${max} ${disabled} @change="${el.vModel}Change">${child}</${el.tag}>`
    },
    'el-switch': el => {
        const {
            disabled,
            vModel
        } = attrBuilder(el)
        const activeText = el['active-text'] ? `active-text="${el['active-text']}"` : ''
        const inactiveText = el['inactive-text'] ? `inactive-text="${el['inactive-text']}"` : ''
        const activeColor = el['active-color'] ? `active-color="${el['active-color']}"` : ''
        const inactiveColor = el['inactive-color'] ? `inactive-color="${el['inactive-color']}"` : ''
        const activeValue = el['active-value'] !== true ? `:active-value='${JSON.stringify(el['active-value'])}'` : ''
        const inactiveValue = el['inactive-value'] !== false ? `:inactive-value='${JSON.stringify(el['inactive-value'])}'` : ''

        return `<${el.tag} ${vModel} ${activeText} ${inactiveText} ${activeColor} ${inactiveColor}
         ${activeValue} ${inactiveValue} ${disabled}></${el.tag}>`
    },
    'el-cascader': el => {
        const {
            disabled,
            vModel,
            clearable,
            placeholder,
            width
        } = attrBuilder(el)
        const options = el.options ? `:options="${el.vModel}Options"` : ''
        const props = el.props ? `:props="${el.vModel}Props"` : ''
        const showAllLevels = el['show-all-levels'] ? '' : ':show-all-levels="false"'
        const filterable = el.filterable ? 'filterable' : ''
        const separator = el.separator === '/' ? '' : `separator="${el.separator}"`

        return `<${el.tag} ${vModel} ${options} ${props} ${width} ${showAllLevels} ${placeholder}
         ${separator} ${filterable} ${clearable} ${disabled}></${el.tag}>`
    },
    'el-slider': el => {
        const {
            disabled,
            vModel
        } = attrBuilder(el)
        const min = el.min ? `:min='${el.min}'` : ''
        const max = el.max ? `:max='${el.max}'` : ''
        const step = el.step ? `:step='${el.step}'` : ''
        const range = el.range ? 'range' : ''
        const showStops = el['show-stops'] ? `:show-stops="${el['show-stops']}"` : ''

        return `<${el.tag} ${min} ${max} ${step} ${vModel} ${range} ${showStops} ${disabled}></${el.tag}>`
    },
    'el-time-picker': el => {
        const {
            disabled,
            vModel,
            clearable,
            placeholder,
            width
        } = attrBuilder(el)
        const startPlaceholder = el['start-placeholder'] ? `start-placeholder="${el['start-placeholder']}"` : ''
        const endPlaceholder = el['end-placeholder'] ? `end-placeholder="${el['end-placeholder']}"` : ''
        const rangeSeparator = el['range-separator'] ? `range-separator="${el['range-separator']}"` : ''
        const isRange = el['is-range'] ? 'is-range' : ''
        const format = el.format ? `format="${el.format}"` : ''
        const valueFormat = el['value-format'] ? `value-format="${el['value-format']}"` : ''
        const pickerOptions = el['picker-options'] ? `:picker-options='${JSON.stringify(el['picker-options'])}'` : ''

        return `<${el.tag} ${vModel} ${isRange} ${format} ${valueFormat} ${pickerOptions} ${width}
         ${placeholder} ${startPlaceholder} ${endPlaceholder} ${rangeSeparator} ${clearable} ${disabled}></${el.tag}>`
    },
    'el-date-picker': el => {
        const {
            disabled,
            vModel,
            clearable,
            placeholder,
            width
        } = attrBuilder(el)
        const startPlaceholder = el['start-placeholder'] ? `start-placeholder="${el['start-placeholder']}"` : ''
        const endPlaceholder = el['end-placeholder'] ? `end-placeholder="${el['end-placeholder']}"` : ''
        const rangeSeparator = el['range-separator'] ? `range-separator="${el['range-separator']}"` : ''
        const format = el.format ? `format="${el.format}"` : ''
        const valueFormat = el['value-format'] ? `value-format="${el['value-format']}"` : ''
        const type = el.type === 'date' ? '' : `type="${el.type}"`
        const readonly = el.readonly ? 'readonly' : ''
        const pickerOptions = el.pickerOptions && (el.pickerOptions.minDate || el.pickerOptions.maxDate) ?
            `:picker-options=${el.vModel}PickerOptions` : ''

        return `<${el.tag} ${type} ${vModel} ${format} ${valueFormat} ${width} ${placeholder} ${pickerOptions}
        ${startPlaceholder} ${endPlaceholder} ${rangeSeparator} ${clearable} ${readonly} ${disabled}></${el.tag}>`
    },
    'el-rate': el => {
        const {
            disabled,
            vModel
        } = attrBuilder(el)
        const max = el.max ? `:max='${el.max}'` : ''
        const allowHalf = el['allow-half'] ? 'allow-half' : ''
        const showText = el['show-text'] ? 'show-text' : ''
        const showScore = el['show-score'] ? 'show-score' : ''

        return `<${el.tag} ${max} ${vModel} ${allowHalf} ${showText} ${showScore} ${disabled}></${el.tag}>`
    },
    'el-color-picker': el => {
        const {
            disabled,
            vModel
        } = attrBuilder(el)
        const size = `size="${el.size}"`
        const showAlpha = el['show-alpha'] ? 'show-alpha' : ''
        const colorFormat = el['color-format'] ? `color-format="${el['color-format']}"` : ''

        return `<${el.tag} ${vModel} ${size} ${showAlpha} ${colorFormat} ${disabled}></${el.tag}>`
    },
    'el-upload': el => {
        const disabled = el.disabled ? ':disabled=\'true\'' : ''
        const action = el.action ? `:action="${el.vModel}Action"` : ''
        const multiple = el.multiple ? 'multiple' : ''
        const listType = el['list-type'] !== 'text' ? `list-type="${el['list-type']}"` : ''
        const accept = el.accept ? `accept="${el.accept}"` : ''
        const name = el.name !== 'file' ? `name="${el.name}"` : ''
        const autoUpload = el['auto-upload'] === false ? ':auto-upload="false"' : ''
        const beforeUpload = `:before-upload="${el.vModel}BeforeUpload"`
        const onChange = `:on-change="${el.vModel}Change"`
        const onRemove = `:on-remove="${el.vModel}Change"`
        const fileList = `:file-list="${confGlobal.formModel}.${el.vModel}||[]"`
        const ref = `ref="${el.vModel}"`
        let child = buildElUploadChild(el)

        if (child) child = `\n${child}\n` // 换行
        return `<${el.tag} ${ref} ${fileList} ${action} ${onChange} ${onRemove} ${autoUpload} ${multiple} ${beforeUpload}
         ${listType} ${accept} ${name} ${disabled}>${child}</${el.tag}>`
    },
    'matrix-void': el => {
        const child = el.child;
        let matrixLabel = el.label ? `<div>${el.label}</div>` : ''
        let text = `<div> ${matrixLabel}</div>`
        return text
    },
    tinymce: el => {
        const {
            vModel,
            placeholder
        } = attrBuilder(el)
        const height = el.height ? `:height="${el.height}"` : ''
        const branding = el.branding ? `:branding="${el.branding}"` : ''
        return `<${el.tag} ${vModel} ${placeholder} ${height} ${branding}></${el.tag}>`
    },
    'el-divider': el => {
        const direction = el.direction ? `direction="${el.direction}"` : '';

        return `<${el.tag} ${direction} class="${el.class}"></${el.tag}>`
    },
    paging: el => {
        const page = el.page ? `:page="${el.page}"` : ''
        const total = el.total ? `:total="${el.total}"` : ''
        return `<${el.tag}  ${page} ${total}></${el.tag}>`
    },
    'com-table': el => {
        const {
            vModel
        } = attrBuilder(el)

        const options = el.options ? `:options='${el.vModel}Options'` : '';
        const headers = el.tableHeaders ? `:headers='${el.vModel}Headers'` : '';
        const align = el.tableAlignType ? `tableAlignType=${el.tableAlignType}` : '';
        let a = `<${el.tag} ${options} ${headers} ${align} ${vModel}></${el.tag}>`
        return a
    },
    'el-table': el => {
        const data = el.data ? `:data='${el.vModel}Data'` : '';
        let headerData = [];
        headerData = Object.keys(el.data[0]);
        let child = buildElTableChild(el, headerData)

        if (child) child = `\n${child}\n` // 换行
        let table = `<${el.tag} ${data} > ${child} </${el.tag}>`
        return table
    },
    'com-BMI': el => {
        // console.log('com-BMI', el)
        let domStr = `<div class="BMI_component">
            <div v-for="(item,index) in ${el.vModel}Options" :key="index" class="BMI_component_item">
                <p class="BMI_lable">{{item.label}}</p>
                <el-input-number clearable v-model="item.value" class="BMI_value" :controls="false" 
                    v-if="item.code" @change="${el.vModel}BMIChange" />
                <p v-else class="BMI_value" :key="item.value">{{ item.value }}</p>
                <p class="BMI_unit">{{ item.unit }}</p>
            </div>
        </div>`
        return domStr
    },
    'com-TimeNumeral': el => {
        const {
            placeholder,
            vModel,
        } = attrBuilder(el)

        return `<div class="time_numeral_component">
            <div class="time_numeral_datePicker">
                <span class="datePicker_label">${el.startDateLabel}</span>
                <el-date-picker v-model="${el.vModel}Start" type="${el.type}" ${placeholder}
                    :picker-options="${el.vModel}StartPickerOptions" @change=${el.vModel}DateChange($event,1)>
                </el-date-picker>
            </div>
            <template v-if="${el.isSetEndTime}">
                <div class="time_numeral_datePicker">
                    <span class="datePicker_label">${el.endDateLabel}</span>
                    <el-date-picker v-model="${el.vModel}End" type="${el.type}" ${placeholder}
                        :picker-options="${el.vModel}EndPickerOptions" @change=${el.vModel}DateChange($event,2)>
                    </el-date-picker>
                </div>
            </template>
            <div class="difference_container">
                <span class="dif_str">${el.differenceLabel}</span>
                <span class="dif_val">{{ ${confGlobal.formModel}.${el.vModel} }}</span>
                <span class="dif_str">${el.differenceUnit}</span>
            </div>
        </div>`
    },
    'com-Maternity': el => {
        let child = buildComMaternityChild(el)

        if (child) child = `\n${child}\n` // 换行
        let str = `<div class="maternity_component">
            <div class="maternity_baseInfo">
                <span>孕</span>
                <el-input-number :controls="false" v-model="${confGlobal.formModel}.${el.vModel}[0]" 
                :min="${confGlobal.formModel}.${el.vModel}[1]" />
                <span>次，</span>
                <span>产</span>
                <el-input-number :controls="false" v-model="${confGlobal.formModel}.${el.vModel}[1]" 
                :max="${confGlobal.formModel}.${el.vModel}[0]<=10?${confGlobal.formModel}.${el.vModel}[0]:10" 
                :min="0" @change="${el.vModel}NumberChange" />
                <span>次</span>
            </div>
            ${child}
        </div>`
        return str
    },
    'com-caption': el => {
        return `<el-alert :closable="${el.closable}" type="${el.type}" effect="${el.effect}">
        <template slot="title" >
            <div v-html="${el.vModel}description"></div>
        </template>
    </el-alert>`
    },
}

function buildElTableChild(el, header) {
    const children = [];
    if (header && header.length) {
        header.forEach(item => {
            children.push(`<el-table-column label="${item}" prop="${item}"
            align="center" min-width="220">
                <template slot-scope="scope">
                    <el-input clearable v-if="scope.row[scope.column.property].type=='text'" type="textarea"
                        v-model="scope.row[scope.column.property].value" autosize></el-input>
                    <el-date-picker v-if="scope.row[scope.column.property].type!='text'" 
                        v-model="scope.row[scope.column.property].value" 
                        :type="scope.row[scope.column.property].type" 
                        :format="scope.row[scope.column.property].type=='week'?'yyyy 第 WW 周'
                        :scope.row[scope.column.property].type=='month'?'yyyy-MM'
                        :scope.row[scope.column.property].type=='year'?'yyyy'
                        :scope.row[scope.column.property].type=='datetime'?'yyyy-MM-DD HH:mm':'yyyy-MM-DD'"
                        placeholder="选择日期">
                    </el-date-picker>
                </template>
            </el-table-column>`)
        })
    }
    return children.join('\n')

}

function attrBuilder(el) {
    return {
        vModel: `v-model="${confGlobal.formModel}.${el.vModel}"`,
        clearable: el.clearable ? 'clearable' : '',
        placeholder: el.placeholder ? `placeholder="${el.placeholder}"` : '',
        width: el.style && el.style.width ? ':style="{width: \'100%\'}"' : '',
        disabled: el.disabled ? ':disabled=\'true\'' : ''
    }
}

function buildComMaternityChild(el) {
    const children = [];
    let options = el.options
    if (options && options.length) {
        children.push(`
        <el-form inline label-width="80px" v-for="(item,index) in ${el.vModel}Options" :key="index" 
        v-if="${confGlobal.formModel}.${el.vModel}[1]" class="maternity_form_content">
            <h4 class="maternity_number">生产第{{ index+1 }}次</h4>
            <el-form-item :label="item.childbirth.propertyName" class="maternity_item">
                <el-radio-group v-model="item.childbirth.defaultValue">
                    <el-radio v-for="(optionItem,optionIndex) in item.childbirth.options" :key="optionIndex"
                        :label="optionItem.value">{{ optionItem.label }}</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item :label="item.birthHistory.propertyName" class="maternity_item">
                <el-checkbox-group v-model="item.birthHistory.defaultValue" @change="${el.vModel}BirthHistoryChange($event,index)">
                    <el-checkbox v-for="(optionItem,optionIndex) in item.birthHistory.options" :key="optionIndex"
                        :label="optionItem.value" :disabled="optionItem.disable">{{ optionItem.label }}</el-checkbox>
                </el-checkbox-group>
            </el-form-item>
        </el-form>
        <el-form-item :label="${el.vModel}embryo&&${el.vModel}embryo.propertyName" 
            class="maternity_item display-flex" 
            v-if="${confGlobal.formModel}.${el.vModel}[1]" label-width="110px">
            <el-input-number v-model="${el.vModel}embryo&&${el.vModel}embryo.value"
                :min="${el.vModel}embryo&&${el.vModel}embryo.min" 
                :max="${el.vModel}embryo&&${el.vModel}embryo.max" :controls="false" />
            <span v-if="${el.vModel}embryo&&${el.vModel}embryo.unit">
                {{ ${el.vModel}embryo&&${el.vModel}embryo.unit }}</span>
        </el-form-item>`)
    }
    return children.join('\n')
}

// el-buttin 子级
function buildElButtonChild(conf) {
    const children = []
    if (conf.default) {
        children.push(conf.default)
    }
    return children.join('\n')
}

// el-input innerHTML
function buildElInputChild(conf) {
    const children = []
    if (conf.prepend) {
        children.push(`<template slot="prepend">${conf.prepend}</template>`)
    }
    if (conf.append) {
        children.push(`<template slot="append">${conf.append}</template>`)
    }
    return children.join('\n')
}
// 像这种渲染的也可以吧 不对，只要传了参，就没有子dom
function buildElSelectChild(conf) {
    const children = []
    if (conf.options && conf.options.length) {
        children.push(`<el-option v-for="(item, index) in ${conf.vModel}Options" :key="index" 
        :label="item.label" :value="item.value" :disabled="item.disabled">
        {{item.label}}{{item.itemScores?'（' + item.itemScores + '分）':''}}</el-option>`)
    }
    return children.join('\n')
}

function buildElRadioGroupChild(conf) {
    const children = []
    if (conf.options && conf.options.length) {
        conf.options.forEach(item => {
            item.disposeExplain = item.disposeExplain ?
                item.disposeExplain.replace(/\n|\r\n/g, '<br>').replace(/ /g, ' &nbsp') : undefined
        })
        const tag = conf.optionType === 'button' ? 'el-radio-button' : 'el-radio'
        const border = conf.border ? 'border' : '';
        let radioDom = `<div class="group_custom_item" v-for="(item, index) in ${conf.vModel}Options" :key="index">
            <${tag} :label="item.value" :disabled="item.disabled" ${border}>
            {{item.label}}{{item.itemScores?'（' + item.itemScores + '分）':''}}</${tag}>
            <template v-if="item.isShowInputValue&&${confGlobal.formModel}.${conf.vModel}==item.value">
                <p class="radio_text" v-if="item.inputAttr.inputPrefixVal">{{item.inputAttr.inputPrefixVal}}</p>
                <el-input class="option_input" v-if="item.inputAttr.inputType!='number'" 
                v-model="item.inputAttr.inputDefaultVal" 
                :maxlength="item.inputAttr&&item.inputAttr.maxlength||undefined"
                :minlength="item.inputAttr&&item.inputAttr.minlength||undefined"
                :placeholder="item.inputAttr&&item.inputAttr.placeholder"
                />
                <el-input-number v-if="item.inputAttr.inputType=='number'" 
                :controls="false"
                v-model="item.inputAttr.inputDefaultVal" 
                :max="item.inputAttr&&item.inputAttr.max||undefined"
                :min="item.inputAttr&&item.inputAttr.min||undefined"
                :placeholder="item.inputAttr&&item.inputAttr.placeholder"
                :step="item.inputAttr.step" :precision="item.inputAttr.precision"
                />
                <p class="radio_text" v-if="item.inputAttr.inputSuffixVal">{{item.inputAttr.inputSuffixVal}}</p>
            </template>
            <p class="dispose_explain" v-if="item.disposeExplain" 
            v-html="item.disposeExplain"></p>
        </div>`
        children.push(radioDom)
    }
    return children.join('\n')
}

function buildElCheckboxGroupChild(conf) {
    const children = []
    if (conf.options && conf.options.length) {
        conf.options.forEach(item => {
            item.disposeExplain = item.disposeExplain ?
                item.disposeExplain.replace(/\n|\r\n/g, '<br>') : undefined
        })
        const tag = conf.optionType === 'button' ? 'el-checkbox-button' : 'el-checkbox'
        const border = conf.border ? 'border' : ''
        const checkboxStr = `<div class="group_custom_item" v-for="(item, index) in ${conf.vModel}Options" 
            :key="index">
            <${tag} :label="item.value" :disabled="item.disabled" ${border}>
            {{item.label}}{{item.itemScores?'（' + item.itemScores + '分）':''}}</${tag}>
            <template v-if="item.isShowInputValue&&${confGlobal.formModel}.${conf.vModel}.find(f=>f==item.value)">
                <p class="radio_text" v-if="item.inputAttr.inputPrefixVal">{{item.inputAttr.inputPrefixVal}}</p>
                <el-input class="option_input" v-if="item.inputAttr.inputType!='number'" 
                v-model="item.inputAttr.inputDefaultVal" 
                :maxlength="item.inputAttr&&item.inputAttr.maxlength||undefined"
                :minlength="item.inputAttr&&item.inputAttr.minlength||undefined"
                :placeholder="item.inputAttr&&item.inputAttr.placeholder"
                 />
                <el-input-number v-if="item.inputAttr.inputType=='number'" 
                :controls="false"
                v-model="item.inputAttr.inputDefaultVal" 
                :max="item.inputAttr&&item.inputAttr.max||undefined"
                :min="item.inputAttr&&item.inputAttr.min||undefined"
                :placeholder="item.inputAttr&&item.inputAttr.placeholder"
                :step="item.inputAttr.step" :precision="item.inputAttr.precision"
                 />
                <p class="radio_text" v-if="item.inputAttr.inputSuffixVal">{{item.inputAttr.inputSuffixVal}}</p>
            </template>
            <p class="dispose_explain" v-if="item.disposeExplain" 
                v-html="item.disposeExplain"></p>
        </div>`
        // console.log('checkboxStr',checkboxStr,conf.vModel+'Options')
        children.push(checkboxStr)
    }
    return children.join('\n')
}

function buildElUploadChild(conf) {
    const list = []
    if (conf['list-type'] === 'picture-card') list.push('<i class="el-icon-plus"></i>')
    else list.push(`<el-button type="primary" icon="el-icon-upload">${conf.buttonText}</el-button>`)
    if (conf.showTip) list.push(`<div slot="tip" class="el-upload__tip">只能上传不超过 ${conf.fileSize}${conf.sizeUnit} 的${conf.accept}文件</div>`)
    return list.join('\n')
}

export function makeUpHtml(conf, type) {
    const htmlList = []
    confGlobal = conf
    someSpanIsNot24 = conf.fields.some(item => item.span !== 24)
    conf.fields.forEach(el => {
        htmlList.push(layouts[el.layout](el))
    })
    const htmlStr = htmlList.join('\n')
    let temp = buildFormTemplate(conf, htmlStr, type)
    if (type === 'dialog') {
        temp = dialogWrapper(temp)
    }
    confGlobal = null
    return temp
}

export function makeUpPreviewHtml(conf, type) {
    const htmlList = []
    confGlobal = conf
    someSpanIsNot24 = conf.fields.some(item => item.span !== 24)
    conf.fields.forEach(el => {
        htmlList.push(layouts[el.layout](el))
    })
    const htmlStr = htmlList.join('\n')
    let temp = buildFormTemplate(conf, htmlStr, type)
    confGlobal = null
    return temp
}
