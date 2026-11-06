<template>
    <el-dialog v-bind="$attrs" width="60%" top="12vh" :close-on-click-modal="false" v-on="$listeners" @close="close"
        destroy-on-close :show-close="false">
        <div class="current_container">
            <div class="dialog_container">
                <div class="main_tabs">
                    <div class="main_tab" v-for="item in ruleCodes" :key="item.value" :label="item.label"
                        @click="handleClick(item)" :class="number == item.value ? 'blue' : '' ">
                        {{ item.label }}
                    </div>
                </div>
                <template v-if="number === '1'">
                    <!-- <el-tree :data="componentsList" node-key="propertyCode" :props="defaultProps"
                        @node-click="changeHandle" :key="currentItem.propertyCode">
                        <div slot-scope="{ node, data }">
                            <el-tooltip placement="top" effect="dark" :content="node.label"
                                :disabled="node.label.length&&node.label.length<10">
                                <span class="custom_tree_label ellipsis">{{ node.label }}</span>
                            </el-tooltip>
                        </div>
                    </el-tree> -->
                    <el-scrollbar class="property_list">
                        <div class="property_item" v-for="(item,index) in componentsList" :key="index"
                            @click="changeHandle(item)">
                            <el-tooltip placement="right" effect="dark" :content="item.label"
                                :disabled="item.label.length&&item.label.length<10">
                                <span class="custom_tree_label ellipsis">{{ item.label }}</span>
                            </el-tooltip>
                        </div>
                    </el-scrollbar>
                </template>
                <div v-if="number === '2'">
                    <el-collapse v-model="activeTag" accordion>
                        <el-collapse-item title="常用" name="1">
                            <div v-for="(info,index) in listFunction" :key="index" @click="changeHandle(info)"
                                class="list_function">
                                <p class="info_tit">{{ info.name }}</p>
                                <p class="info_remarks">{{ info.remarks }}</p>
                            </div>
                        </el-collapse-item>
                    </el-collapse>
                </div>
                <div v-if="number === '3'">
                    <el-collapse v-model="activeTag" accordion>
                        <el-collapse-item title="基础运算符" name="1">
                            <div v-for="(info,index) in listOperator" :key="index" @click="changeHandle(info)"
                                class="list_function">
                                <p class="info_tit">{{ info.name }}</p>
                                <p class="info_remarks">{{ info.remarks }}</p>
                            </div>
                        </el-collapse-item>
                    </el-collapse>
                </div>
            </div>
            <div class="right_contnt">
                <div class="codemirror_tips">在左侧选择函数或字段变量，且在英文输入法下编辑</div>
                <codemirror v-model="codemirrorVal" ref="newCm" class="codemirror-textarea" :options="cmOptions"
                    @ready="onCmReady" @input="onCodemirrorInput"></codemirror>
            </div>
        </div>
        <div slot="footer">
            <el-button type="primary" @click="setOptions">确定</el-button>
        </div>
    </el-dialog>
</template>
<script>

import 'codemirror/mode/javascript/javascript.js'
import '@/utils/generator/codemirrorSetting.js'
import { convertFieldsDataLoop } from '@/utils/index'

export default {
    props: ["currentItem", "drawingList",],

    data() {
        return {
            activeName: "first",
            componentsList: [],
            defaultProps: {
                children: "children",
                label: "propertyName",
            },
            defaultPropsFunction: {
                children: "options",
                label: "name",
            },
            codemirrorVal: "",
            textData: '',
            cmOptions: {
                mode: "text/javascript",
                theme: "mdn-like", // 主题样式
                tabSize: 4, // tabsize默认为4
                styleActiveLine: false, // 显示选中行的样式
                lineNumbers: false, // 代码行数字
                styleSelectedText: false,
                autofocus: true, //自动聚焦
                matchBrackets: true, // 匹配结束符号，比如"]、}"
                autoCloseBrackets: true, // 自动闭合符号
                lineNumberFormatter: true,//长行是否自动换行活滚动
                line: true,
                gutters: [
                    "CodeMirror-linenumbers",
                    "CodeMirror-foldgutter",
                    "CodeMirror-lint-markers",
                ],
                extraKeys: {
                    "Ctrl": "autocomplete"
                }, //自动提示配置 
                lineWrapping: true, // 自动换行
                foldGutter: true,
                highlightSelectionMatches: { showToken: /\w/, annotateScrollbar: true },
                hintOptions: {
                    // 当匹配只有一项的时候是否自动补全
                    completeSingle: false
                },
                //快捷键 可提供三种模式 sublime、emacs、vim
                keyMap: "sublime",
                showCursorWhenSelecting: true,
            },
            ruleCodes: [{
                label: '函数',
                value: '2'
            }, {
                label: '问题',
                value: '1'
            }, {
                label: '运算符',
                value: '3'
            },],
            number: '2',
            activeTag: '1',
            listFunction: [{
                name: 'SUM',
                id: '1',
                label: 'SUM()',
                type: 'function',
                remarks: '返回所有参与字段中的总和'
            },
            {
                name: 'MAX',
                id: '2',
                label: 'MAX()',
                type: 'function',
                remarks: '返回所有参与字段中的最大值'
            },
            {
                name: 'MIN',
                id: '3',
                label: 'MIN()',
                type: 'function',
                remarks: '返回所有参与字段中的最小值'
            },
            {
                name: 'AVERAGE',
                id: '4',
                label: 'AVERAGE()',
                type: 'function',
                remarks: '计算所有参与运算字段的平均值'
            },
            {
                name: 'PRODUCT',
                id: '7',
                label: 'PRODUCT()',
                type: 'function',
                remarks: '计算所有参与运算字段的乘积'
            },
                // {
                //     name: 'ROUND',
                //     id: '5',
                //     label: 'ROUND()',
                //     type: 'function',
                //     remarks: '四舍五入为最接近的整数'
                // },
                // {
                //     name: 'CONCATENATE',
                //     id: '6',
                //     label: 'CONCATENATE()',
                //     type: 'function'
                // },
            ],
            listOperator: [{
                name: '+',
                id: 'op_add',
                label: '+',
                type: 'operator',
                remarks: '加法'
            }, {
                name: '-',
                id: 'op_sub',
                label: '-',
                type: 'operator',
                remarks: '减法'
            }, {
                name: '*',
                id: 'op_mul',
                label: '*',
                type: 'operator',
                remarks: '乘法'
            }, {
                name: '/',
                id: 'op_div',
                label: '/',
                type: 'operator',
                remarks: '除法'
            }],
            checkedFormList: []
        };
    },
    computed: {},
    watch: {
        drawingList: {
            handler(newVal, oldVal) {
                this.drawingList.forEach(item => {
                    this.$set(item, 'label', item.propertyName)
                });
            }
        },
        'currentItem': {
            handler(newVal, oldVal) {
                let treeList = this.convertFormulaList(this.drawingList, newVal.propertyCode);
                console.log(this.treeList)
                this.componentsList = treeList.map(m => {
                    return {
                        ...m,
                        label: m.propertyName,
                    }
                })
                if (newVal.formulaList) {
                    this.checkedFormList = newVal.formulaList
                }
            },
            deep: true,
            immediate: true
        }
    },
    mounted() {
        this.$nextTick(() => {
            this.checkedFormList.forEach(item => {
                this.setCodemirrorValue(item)
            })
        })
    },
    methods: {
        setCodemirrorValue(data) {
            let pos1 = this.$refs.newCm.codemirror.getCursor()
            let pos2 = {
                line: pos1.line,
                ch: pos1.ch,
            }
            let len = data.label.length
            this.$refs.newCm.codemirror.getSelection()
            this.$refs.newCm.codemirror.focus()
            this.$refs.newCm.codemirror.replaceRange(data.label, pos2)
            let setCodemirror = this.$refs.newCm.codemirror.getDoc()
            let strDom = document.createElement("span");
            strDom.innerText = data.label
            if (data.type == 'function') {
                strDom.className = "cm-keyword "
                setCodemirror.markText(
                    { line: pos1.line, ch: pos1.ch },
                    { line: pos1.line, ch: pos1.ch + len },
                    { replacedWith: strDom }
                )
            } else if (data.type == 'operator') {
                strDom.className = "cm-operator"
                setCodemirror.markText(
                    { line: pos1.line, ch: pos1.ch },
                    { line: pos1.line, ch: pos1.ch + len },
                    { replacedWith: strDom }
                )
            } else {
                strDom.className = "el-tag ".concat("el-tag--light")
                setCodemirror.markText(
                    { line: pos1.line, ch: pos1.ch },
                    { line: pos1.line, ch: pos1.ch + len },
                    { replacedWith: strDom }
                )
            }
            this.$refs.newCm.codemirror.setCursor({
                line: pos1.line,
                ch: pos1.ch + len
            })
            this.$refs.newCm.codemirror.focus();
            // console.log('this.codemirrorVal',this.codemirrorVal)
        },
        convertFormulaList(comList, nowPropertyCode) {
            let flatList = convertFieldsDataLoop(comList || [])
            let index = flatList.findIndex(f => f.propertyCode == nowPropertyCode)
            if (index > -1) {
                return flatList.slice(0, index).filter(f => f.labelType == 'Checkbox' || f.labelType == 'Radio' ||
                    f.labelType == 'Select' || (f.labelType == 'Input' && f.regValue && f.regValue.value == 'number')
                    || f.labelType == 'Statistic' || f.labelType == 'CaseSpecification')
            }
            return []
        },
        close() {
            this.$emit("update:visible", false);
        },
        // 确定保存options
        setOptions() {
            if (this.codemirrorVal) {
                this.checkedFormList = this.buildFormulaListFromText(this.codemirrorVal, this.checkedFormList)
            } else {
                this.checkedFormList = []
            }
            this.currentItem['formulaList'] = this.checkedFormList.map((m, index) => {
                return {
                    type: m.type,
                    label: m.label,
                    id: m.id,
                    name: m.name,
                    propertyCode: m.propertyCode
                }
            })
            this.close();
        },
        buildFormulaListFromText(text, tokenList) {
            const sourceText = text || ''
            const remainingValueTokens = (tokenList || [])
                .filter(item => item && item.label)
                .map(item => ({ ...item }))
                .filter(item => item.type != 'operator' && item.label != ')' && item.type != 'number')
            const orderedTokens = []
            let currentIndex = 0

            while (currentIndex < sourceText.length) {
                const currentChar = sourceText[currentIndex]
                if (/\s/.test(currentChar)) {
                    currentIndex++
                    continue
                }

                const matchedTokenIndex = remainingValueTokens.findIndex(item => {
                    return sourceText.slice(currentIndex, currentIndex + item.label.length) == item.label
                })
                const matchedToken = matchedTokenIndex > -1 ? remainingValueTokens[matchedTokenIndex] : null
                if (matchedToken) {
                    orderedTokens.push({
                        ...matchedToken,
                        id: this.createFormulaTokenId(matchedToken, orderedTokens.length)
                    })
                    remainingValueTokens.splice(matchedTokenIndex, 1)
                    currentIndex += matchedToken.label.length
                    continue
                }

                if (['(', ')', '+', '-', '*', '/'].includes(currentChar)) {
                    const operatorToken = this.createManualOperatorToken(currentChar)
                    orderedTokens.push({
                        ...operatorToken,
                        id: this.createFormulaTokenId(operatorToken, orderedTokens.length)
                    })
                    currentIndex++
                    continue
                }

                const numberMatch = sourceText.slice(currentIndex).match(/^\d+(\.\d+)?/)
                if (numberMatch) {
                    const numberToken = this.createManualNumberToken(numberMatch[0])
                    orderedTokens.push({
                        ...numberToken,
                        id: this.createFormulaTokenId(numberToken, orderedTokens.length)
                    })
                    currentIndex += numberMatch[0].length
                    continue
                }

                currentIndex++
            }

            return orderedTokens
        },
        createManualOperatorToken(operator) {
            const operatorMap = {
                '+': {
                    name: '+',
                    id: 'op_add',
                    label: '+',
                    type: 'operator',
                    remarks: '加法'
                },
                '-': {
                    name: '-',
                    id: 'op_sub',
                    label: '-',
                    type: 'operator',
                    remarks: '减法'
                },
                '*': {
                    name: '*',
                    id: 'op_mul',
                    label: '*',
                    type: 'operator',
                    remarks: '乘法'
                },
                '/': {
                    name: '/',
                    id: 'op_div',
                    label: '/',
                    type: 'operator',
                    remarks: '除法'
                },
                '(': {
                    name: '(',
                    id: 'op_left_paren',
                    label: '(',
                    type: 'operator',
                    remarks: '左括号'
                },
                ')': {
                    name: ')',
                    id: 'op_right_paren',
                    label: ')',
                    type: 'operator',
                    remarks: '右括号'
                }
            }
            return operatorMap[operator]
        },
        createManualNumberToken(value) {
            return {
                name: value,
                id: `num_${value}`,
                label: value,
                type: 'number',
                remarks: '常数'
            }
        },
        createFormulaTokenId(token, index) {
            if (token.type == 'function') {
                return `${token.name || token.label}${index}`
            }
            if (token.type == 'operator') {
                return `${token.label}${index}`
            }
            if (token.type == 'number') {
                return `number_${token.label}_${index}`
            }
            return `${token.propertyCode || token.label}${index}`
        },
        handleClick(item) {
            this.number = item.value
        },
        onCmReady(cm) {
            // 设置代码编辑框宽和高
            this.$refs.newCm.codemirror.setSize("-webkit-fill-available", "100%")
        },
        tagClose(event) {
            if (event.type == 'function') {
                this.checkedFormList.splice(this.checkedFormList.indexOf(event), 1);
                let index = this.checkedFormList.findIndex(f => f.name == event.name)
                this.checkedFormList.splice(index, 1);
            } else {
                this.checkedFormList.splice(this.checkedFormList.indexOf(event), 1);
            }
        },
        onCodemirrorInput(event) {
            // console.log('input event', event)
            if (!event) {
                this.checkedFormList = []
            }
        },
        changeHandle(data, node) {
            if (data.type == 'function') {
                this.checkedFormList.push({
                    ...data,
                    id: this.checkedFormList.length + data.name,
                    label: data.label.slice(0, -1)
                })
                this.checkedFormList.push({
                    ...data,
                    id: this.checkedFormList.length + data.name,
                    label: ')'
                })
            } else if (data.type == 'operator') {
                this.checkedFormList.push({
                    ...data,
                    id: this.checkedFormList.length + data.name
                })
            } else {
                const lastItem = this.checkedFormList[this.checkedFormList.length - 1]
                if (lastItem && lastItem.label === ')') {
                    this.checkedFormList.splice(-1, 0, data)
                } else {
                    this.checkedFormList.push(data)
                }
            }
            this.checkedFormList.forEach((item, index) => {
                item.id = item.type == 'function'
                    ? item.name + index
                    : item.type == 'operator'
                        ? item.label + index
                        : item.propertyCode + index
            })


            let pos1 = this.$refs.newCm.codemirror.getCursor()
            let pos2 = {
                line: pos1.line,
                ch: pos1.ch,
            }
            let len = data.label.length
            this.$refs.newCm.codemirror.getSelection()
            this.$refs.newCm.codemirror.focus()
            if (data.type == 'function') {
                this.$refs.newCm.codemirror.replaceSelection(data.label, 'start');
                len = len - 1
            } else if (data.type == 'operator') {
                this.$refs.newCm.codemirror.replaceSelection(data.label, 'end');
            } else {
                this.$refs.newCm.codemirror.replaceRange(data.label, pos2)
                let setCodemirror = this.$refs.newCm.codemirror.getDoc()
                let strDom = document.createElement("span");
                strDom.innerText = data.label
                strDom.className = "el-tag ".concat("el-tag--light")
                setCodemirror.markText(
                    { line: pos1.line, ch: pos1.ch },
                    { line: pos1.line, ch: pos1.ch + len },
                    { replacedWith: strDom }
                )
            }
            this.$refs.newCm.codemirror.setCursor({
                line: pos1.line,
                ch: pos1.ch + len
            })
            this.$refs.newCm.codemirror.focus();
        },

    },
};
</script>
<style lang="scss" scoped>
.codemirror_tips {
    font-size: 14px;
    color: rgb(176, 176, 185);
    text-indent: 30px;
    line-height: 30px;
}
.codemirror-textarea {
    font-family: Arial, monospace;
    font-size: 20px;
    height: calc(100% - 35px);
    ::v-deep .CodeMirror {
        height: 100%;
        .el-tag {
            margin: 0 3px;
        }
        .cm-keyword,
        .cm-operator,
        .cm-number {
            font-size: 20px;
        }
    }
}

.CodeMirror * {
    font-family: Arial, monospace;
    font-size: 32px;
}

.dialog_container {
    width: 24%;
    border-right: 1px solid #dde5ed;
    overflow-y: auto;
    flex-shrink: 0;

    .property_list {
        height: calc(100% - 40px);
        .property_item {
            padding: 4px 10px;
            cursor: pointer;
            &:first-child {
                margin-top: 10px;
            }
            &:hover {
                background-color: #e8f4ff;
                border-color: #d1e9ff;
            }
            .custom_tree_label {
                width: 100%;
                display: inline-block;
            }
        }
    }

    .main_tabs {
        display: flex;
        flex: 0 0 33.3333%;

        .main_tab {
            width: 100%;
            height: 30px;
            border: 1px solid #4797c7;
            text-align: center;
            line-height: 30px;
        }
    }

    ::v-deep .el-collapse-item__header {
        text-indent: 10px;
    }

    .blue {
        background-color: #2e5c9f;
        color: #fff;
    }

    .list_function {
        padding: 5px 10px;
        cursor: pointer;

        &:hover {
            background: #e0effd;
        }

        .info_tit {
            font-weight: bolder;
            color: #333;
        }

        .info_remarks {
            color: #666;
        }
    }
}

::v-deep .el-dialog__body {
    height: 60vh;
    overflow-y: auto;
}

.current_container {
    width: 100%;
    height: 100%;
    display: flex;
    border: 1px solid #dde5ed;

    .el-tabs {
        width: 24%;
        height: 100%;
        overflow-y: auto;
    }
    .right_contnt {
        width: 76%;
    }
}

.checked_formula {
    width: 100%;
    height: 100%;

    .el-tag {
        margin: 3px;
    }
}
</style>
