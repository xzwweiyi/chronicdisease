<template>
    <div>
        <el-dialog v-bind="$attrs" width="80%" :close-on-click-modal="false" v-on="$listeners" @close="close"
            :show-close="false" center top="7vh">
            <div class="current_container" v-if="currentItem.tag=='el-cascader'">
                <div class="grid-content" v-for="(levelItem,levelIndex) in levelList" :key="levelIndex">
                    <el-tag effect="plain" type="info">{{levelItem.label}}</el-tag>
                    <draggable class="drawing-itemInfo" :list="levelItem.options" :animation="340"
                        group="componentsGroup" :disabled="disabledDrag">
                        <div class="optionsItem" v-for="(item,index) in levelItem.options" :key="index" :class="
                            (firstIndex == index&&levelIndex ==0)||(secondIndex==index&&levelIndex==1)
                            ||(thirdIndex==index&&levelIndex==2)||(thirdIndex==index&&levelIndex==2)
                            ||(fourIndex==index&&levelIndex==3)?'activeOption':''">
                            <div class="options_item_info" @click="checkedItem(item,levelIndex+1,index)">
                                <div class="display-flex">
                                    <span class="items_name">选项名：</span>
                                    <el-input @blur="disabledDrag=false" @focus="disabledDrag=true" clearable
                                        v-model="item.label" placeholder="请输入选项名"></el-input>
                                </div>
                            </div>
                            <el-tooltip content="删除选项" placement="right" effect="light">
                                <i v-if="!(levelIndex==0&&levelItem.options.length<=1)"
                                    @click="deleteOptionItem(item,index,levelIndex+1)"
                                    class="delete_btn el-icon-remove-outline" circle></i>
                            </el-tooltip>

                        </div>
                    </draggable>
                    <el-button type="text" icon="el-icon-plus" v-if="(levelIndex==1&&firstIndex>-1)
                        ||(levelIndex==2&&secondIndex>-1)||(levelIndex==3&&thirdIndex>-1)||levelIndex==0"
                        @click="addOptions(currentItem,levelIndex+1)">
                        添加选项
                    </el-button>
                </div>
            </div>
            <div class="current_table_container" v-else-if="currentItem.tag=='el-table'">
                <el-table :data="currentItem.data" height="64vh" highlight-current-row
                    :key="this.currentItem.renderKey">
                    <el-table-column v-for="(item,index) in tableHdeaders" :key="index" :label="item" :prop="item"
                        align="center" min-width="220">
                        <template slot="header" slot-scope="scope">
                            <div class="table_headers">
                                <div class="header_icons">
                                    <el-select v-model="currentItem.data[0][item].type"
                                        @change="tableColumnTypeChange($event,scope.column.property)">
                                        <el-option label="类型" value="default"></el-option>
                                        <el-option label="日期时间" value="datetime"></el-option>
                                        <el-option label="日期" value="date"></el-option>
                                        <el-option label="周" value="week"></el-option>
                                        <el-option label="月" value="month"></el-option>
                                        <el-option label="年" value="year"></el-option>
                                        <el-option label="文字" value="text"></el-option>
                                    </el-select>
                                </div>
                                <el-input clearable v-model="scope.column.property"></el-input>
                                <el-button type="text" icon="el-icon-delete" plain class="deleteBtn"
                                    @click="deleteTableColumn(item)" v-if="tableHdeaders.length>1"></el-button>
                            </div>
                        </template>
                        <template slot-scope="scope">
                            <div class="column_content" v-if="scope.row[scope.column.property].type">
                                <el-input v-if="scope.row[scope.column.property].type=='text'" autosize type="textarea"
                                    v-model="scope.row[scope.column.property].value"></el-input>
                                <el-date-picker v-else-if="scope.row[scope.column.property].type!='text'"
                                    v-model="scope.row[scope.column.property].value" :format="
                                    scope.row[scope.column.property].type=='week'?'yyyy 第 WW 周'
                                    :scope.row[scope.column.property].type=='month'?'yyyy-MM'
                                    :scope.row[scope.column.property].type=='year'?'yyyy'
                                    :scope.row[scope.column.property].type=='datetime'?'yyyy-MM-DD HH:mm':'yyyy-MM-DD'"
                                    :type="scope.row[scope.column.property].type" placeholder="选择日期">
                                </el-date-picker>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column fixed="right" label="操作" width="120" align="center">
                        <template slot-scope="scope">
                            <el-button @click.native.prevent="upTableRow(scope.$index)" v-if="scope.$index"
                                icon="el-icon-top" type="text">
                            </el-button>
                            <el-button @click.native.prevent="downTableRow(scope.$index)"
                                v-if="scope.$index<currentItem.data.length-1" icon="el-icon-bottom" type="text">
                            </el-button>
                            <el-button @click.native.prevent="deleteTableRow(scope.$index)"
                                v-if="currentItem.data.length>1" type="text" class="deleteBtn">
                                删除行
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>

            </div>
            <div class="current_other_container" v-else>
                <draggable class="drawing-itemInfo" :list="optionsList" :animation="340" draggable=".other_tag_item"
                    :sort="true" :disabled="disabledDrag">
                    <div class="other_tag_item" v-for="(item,index) in optionsList" :key="index">
                        <div class="options_item_info" @click="checkedOtherTagItem(item,index)">
                            <div class="display-flex">
                                <span class="items_name margin_mini_left">选项名：</span>
                                <el-input @blur="disabledDrag=false" @focus="disabledDrag=true" clearable
                                    v-model="item.label" placeholder="请输入选项名"></el-input>
                            </div>
                        </div>
                        <div v-if="currentItem.labelType=='Radio'||currentItem.labelType=='Checkbox'">
                            <span class="margin_mini_left">是否显示输入框</span>
                            <el-switch class="margin_mini_left" v-model="item.isShowInputValue"
                                @change="inputVisibleStatusChange(item)" />
                            <el-button class="margin_mini_left" type="text" @click="showSetInputInfoDialog(item)"
                                v-if="item.isShowInputValue">设置输入框</el-button>
                        </div>
                        <el-button class="margin_mini_left" type="text" @click="showMutualExclusionDialog(item)"
                            v-if="currentItem.labelType=='Checkbox'">设置互斥选项</el-button>
                        <el-button class="margin_mini_left" type="text" @click="disposeExplainDialog(item)"
                            v-if="currentItem.labelType=='Radio'||currentItem.labelType=='Checkbox'">设置选项说明</el-button>

                        <el-tooltip content="删除选项" placement="right" effect="light">
                            <i @click="deleteOthetTagOptionItem(index)" v-if="optionsList&&optionsList.length>1"
                                class="delete_btn el-icon-remove-outline margin_mini_left" circle></i>
                        </el-tooltip>
                    </div>
                </draggable>
            </div>
            <div slot="footer" class="footer_content">
                <div v-if="currentItem.tag=='el-table'">
                    <el-button type="text" icon="el-icon-plus" @click="addTablecolumn()">添加列</el-button>
                    <el-button type="text" icon="el-icon-plus" @click="addTableRow()">添加行</el-button>
                </div>
                <el-button v-else-if="currentItem.tag!='el-table'&&currentItem.tag!='el-cascader'" type="text"
                    icon="el-icon-plus" @click="addOthetTagOptions">添加选项
                </el-button>
                <i style="color:#fff;">flex占位</i>
                <div>
                    <el-button type="primary" plain @click="cancleSetOption">取消</el-button>
                    <el-button type="primary" @click="setOptions">确定</el-button>
                </div>
            </div>
        </el-dialog>
        <el-dialog :close-on-click-modal="false" :visible.sync="inputSetVisible" :show-close="false" center
            title="输入框信息设置" top="7vh">
            <el-form ref="inputInfo" :model="currentOptionInputInfo" v-if="inputSetVisible" class="current_inputInfo"
                label-width="180px" :rules="inputInfoRule" @submit.native.prevent>
                <el-form-item prop="isRequired" label="输入框是否必填:">
                    <el-switch v-model="currentOptionInputInfo.isRequired"></el-switch>
                </el-form-item>
                <el-form-item prop="inputType" label="输入框类型:">
                    <el-select class="options_typeList" v-model="currentOptionInputInfo.inputType" placeholder="类型选择">
                        <el-option v-for="optionItem in optionBlankTypeOption" :key="optionItem.value"
                            :label="optionItem.label" :value="optionItem.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="inputPrefixVal" label="输入框前的文字:">
                    <el-input v-model="currentOptionInputInfo.inputPrefixVal" placeholder="请输入输入框前的文字" />
                </el-form-item>
                <el-form-item prop="inputSuffixVal" label="输入框后的文字:">
                    <el-input v-model="currentOptionInputInfo.inputSuffixVal" placeholder="请输入输入框后的文字" />
                </el-form-item>
                <el-form-item prop="max" label="输入框最大值:" v-if="currentOptionInputInfo.inputType=='number'">
                    <el-input-number v-model="currentOptionInputInfo.max" :min="currentOptionInputInfo.min||1"
                        :controls="false" :step="1" />
                </el-form-item>
                <el-form-item prop="min" label="输入框最小值:" v-if="currentOptionInputInfo.inputType=='number'">
                    <el-input-number v-model="currentOptionInputInfo.min" :max="currentOptionInputInfo.max"
                        :controls="false" :step="1" />
                </el-form-item>
                <el-form-item prop="precision" label="小数位数:" v-if="currentOptionInputInfo.inputType=='number'">
                    <el-input-number v-model="currentOptionInputInfo.precision" :controls="false" :min="0" />
                </el-form-item>

                <el-form-item prop="maxLength" label="输入框最大长度:" v-if="currentOptionInputInfo.inputType!='number'">
                    <el-input-number v-model="currentOptionInputInfo.maxLength"
                        :min="currentOptionInputInfo.minLength||1" :controls="false" :step="1" />
                </el-form-item>
                <el-form-item prop="minLength" label="输入框最小长度:" v-if="currentOptionInputInfo.inputType!='number'">
                    <el-input-number v-model="currentOptionInputInfo.minLength" :max="currentOptionInputInfo.maxLength"
                        :controls="false" :step="1" :min="1" />
                </el-form-item>
                <el-form-item prop="inputDefaultVal" label="输入框默认文字:">
                    <el-input v-model="currentOptionInputInfo.inputDefaultVal" placeholder="请输入默认值" />
                </el-form-item>
                <el-form-item prop="placeholder" label="输入框占位文字:">
                    <el-input v-model="currentOptionInputInfo.placeholder" />
                </el-form-item>
                <el-form-item prop="typeMessage" label="输入内容格式错误提示:">
                    <el-input v-model="currentOptionInputInfo.typeMessage" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="setInput">确定</el-button>
                    <el-button type="primary" @click="cancleSetInfo">重置</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <el-dialog :close-on-click-modal="false" :visible.sync="mutualExclusionVisible" :show-close="false" center
            title="输入框信息设置" class="mutual_exclusion_dialog">
            <h3 class="dialog_title">可选选项:</h3>
            <el-checkbox-group v-model="checkedMutualExclusion" @change="mutualExclusionChange">
                <el-checkbox v-for="item in currentOptions" :key="item.value" :label="item.value">{{ item.label }}
                </el-checkbox>
            </el-checkbox-group>
            <div slot="footer">
                <el-button type="primary" @click="setMutualExclusion">确定</el-button>
                <el-button type="primary" @click="cancleSetMutualInfo">取消</el-button>
            </div>
        </el-dialog>
        <el-dialog :close-on-click-modal="false" :visible.sync="isShowDisposeExplain" :show-close="false" center
            title="输入选项说明" class="mutual_exclusion_dialog">
            <el-input v-model="disposeExplainTips" type="textarea" :autosize="{ minRows: 4, maxRows: 8}"></el-input>
            <div slot="footer">
                <el-button type="primary" @click="setDisposeExplain">确定</el-button>
                <el-button type="primary" @click="cancleDisposeExplain">取消</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
    import draggable from 'vuedraggable'
    let getLevelData = () => {
        return [{
            label: '一级选项',
            activeId: '',
            activeIndex: 0,
            options: [],
        }, {
            label: '二级选项',
            activeId: '',
            activeIndex: 0,
            options: [],
        }, {
            label: '三级选项',
            activeId: '',
            activeIndex: 0,
            options: [],
        }, {
            label: '四级选项',
            activeId: '',
            activeIndex: 0,
            options: [],
        }]
    }

    export default {
        components: {
            draggable
        },
        inheritAttrs: false,
        props: ['currentItem'],
        data() {
            return {
                levelList: getLevelData(),
                firstIndex: -1,
                secondIndex: -1,
                thirdIndex: -1,
                fourIndex: -1,
                tableHeaderTool: '操作',
                tableHdeaders: [],
                tableColumnType: 'default',
                optionBlankTypeOption: [{
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
                        value: 'mobile',
                        label: '手机号',
                        message: "手机号格式错误",
                        pattern: "/^1(3|4|5|7|8|9)\\d{ 9}$/"
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
                    },
                ],
                inputSetVisible: false,
                currentOptionInputInfo: undefined,
                currentOptionInfo: undefined,
                inputInfoRule: {
                    maxLength: [{
                        type: 'number',
                        message: '必须为数字值',
                        trigger: ['blur', 'change']
                    }],
                    minLength: [{
                        type: 'number',
                        message: '必须为数字值',
                        trigger: ['blur', 'change']
                    }],
                },
                disabledDrag: false,
                optionsList: [],
                cascaderList: [],
                mutualExclusionVisible: false,
                checkedMutualExclusion: [],
                editCurrentOptionsItem: undefined,
                currentOptions: [],
                isShowDisposeExplain:false,
                disposeExplainTips:'',
            }
        },
        computed: {},
        watch: {
            currentItem: {
                handler(newVal, oldVal) {
                    if (newVal.propertyCode != oldVal.propertyCode) {
                        if (newVal.tag == 'el-cascader') {
                            if (oldVal && newVal.formId != oldVal.formId) {
                                this.firstIndex = -1;
                                this.secondIndex = -1;
                                this.thirdIndex = -1;
                                this.fourIndex = -1;
                            }
                            this.cascaderList = [...newVal.options]
                            this.setLevelInfo(this.cascaderList)
                        } else if (newVal.tag == 'el-table') {
                            this.tableHdeaders = Object.keys(this.currentItem.data[0])
                        } else if (newVal.options) {
                            this.optionsList = [...newVal.options]
                        }
                    }
                },
                deep: true
            },
            ['currentOptionInputInfo.maxLength'](newVal) {
                if (newVal == '') {
                    this.currentOptionInputInfo.maxLength = undefined
                } else if (newVal == this.currentOptionInputInfo.minLength) {
                    this.currentOptionInputInfo.maxLength = newVal + 1
                }
            },
            ['currentOptionInputInfo.minLength'](newVal) {
                if (newVal == '') {
                    this.currentOptionInputInfo.minLength = undefined
                } else if (newVal == this.currentOptionInputInfo.maxLength) {
                    this.currentOptionInputInfo.minLength = newVal - 1
                }
            },
        },
        created() {},
        mounted() {},
        methods: {
            // ===================== 选项后输入框相关内容方法 start =================
            showSetInputInfoDialog(optionItem) {
                this.inputSetVisible = true;
                this.currentOptionInfo = optionItem
                this.currentOptionInputInfo = optionItem.inputAttr ? optionItem.inputAttr :
                    this.resetCurrentInputInfo()
            },
            resetCurrentInputInfo() {
                return {
                    inputType: '',
                    maxLength: undefined,
                    minLength: undefined,
                    inputPrefixVal: undefined,
                    inputSuffixVal: undefined,
                    placeholder: '请输入',
                    typeMessage: '格式错误',
                    isRequired: false,
                    inputDefaultVal: ''
                }
            },
            setInput() {
                this.$refs['inputInfo'].validate((valid) => {
                    if (valid) {
                        this.inputSetVisible = false;
                        this.optionsList.find(f => f.value == this.currentOptionInfo.value)[
                            'inputAttr'] = this.currentOptionInputInfo;
                    } else {
                        console.log('error submit!!', valid);
                        return false;
                    }
                });
            },
            cancleSetInfo() {
                this.currentOptionInputInfo = this.resetCurrentInputInfo();
            },
            inputVisibleStatusChange(event) {
                if (event.isShowInputValue) {
                    this.optionsList.find(f => f.value == event.value)['inputAttr'] = this
                        .resetCurrentInputInfo();
                } else {
                    this.optionsList.find(f => f.value == event.value)['inputAttr'] = undefined;
                }
            },
            // ===================== 选项后输入框相关内容方法 start =================
            close() {
                this.$emit('update:visible', false)
            },
            // 当前选中的选项
            checkedItem(event, level, index) {
                if (level == 1) {
                    this.firstIndex = index;
                    this.secondIndex = -1;
                    this.thirdIndex = -1;
                    this.fourIndex = -1;
                } else if (level == 2) {
                    this.secondIndex = index;
                    this.thirdIndex = -1;
                    this.fourIndex = -1;
                } else if (level == 3) {
                    this.thirdIndex = index;
                    this.fourIndex = -1;
                } else if (level == 4) {
                    this.fourIndex = index;
                }
                this.setLevelInfo(this.cascaderList)
            },
            setLevelInfo(cascaderOptions) {
                this.levelList.forEach((item, index) => {
                    let options = []
                    if (index == 0) {
                        options = cascaderOptions
                    } else if (index == 1) {
                        if (this.firstIndex > -1) {
                            options = cascaderOptions[this.firstIndex].children
                        }
                    } else if (index == 2) {
                        if (this.firstIndex > -1 && this.secondIndex > -1) {
                            options = cascaderOptions[this.firstIndex].children[this.secondIndex].children
                        }
                    } else if (index == 3) {
                        if (this.firstIndex > -1 && this.secondIndex > -1 && this.thirdIndex > -1) {
                            options = cascaderOptions[this.firstIndex].children[this.secondIndex].children[this
                                .thirdIndex].children
                        }
                    }
                    item.options = options
                })
                // console.log('cascaderOptions', cascaderOptions, this.levelList)
            },
            // 
            addOptions(list, level) {
                // if (this.isEmptyOption(this.optionsList)) {
                //     this.$message.warning('选项中有还未填写的信息')
                //     return
                // }
                let currentId = Math.random().toString(36).substring(2, 5) + level
                let cascaderOptions = this.cascaderList
                let info = {
                    value: currentId,
                    label: '',
                    level: level,
                }
                if (level == 1) {
                    cascaderOptions.push(info)
                } else {
                    if (level == 2) {
                        if (cascaderOptions[this.firstIndex].children) {
                            cascaderOptions[this.firstIndex].children.push(info)
                        } else {
                            cascaderOptions[this.firstIndex].children = [info]
                        }
                    } else {
                        if (level == 3) {
                            if (cascaderOptions[this.firstIndex].children[this.secondIndex].children) {
                                cascaderOptions[this.firstIndex].children[this.secondIndex].children.push(info)
                            } else {
                                cascaderOptions[this.firstIndex].children[this.secondIndex]['children'] = [
                                    info
                                ]
                            }
                        } else {
                            if (cascaderOptions[this.firstIndex].children[this.secondIndex].children[this
                                    .thirdIndex].children) {
                                cascaderOptions[this.firstIndex].children[this.secondIndex].children[this
                                    .thirdIndex].children.push(info)
                            } else {
                                cascaderOptions[this.firstIndex].children[this.secondIndex].children[this
                                    .thirdIndex]['children'] = [info]
                            }
                        }
                    }
                }
                this.setLevelInfo(cascaderOptions)
            },
            // 确定保存options
            setOptions() {
                if (this.currentItem.labelType == 'Radio' || this.currentItem.labelType == 'Checkbox' || this
                    .currentItem.labelType == 'Select') {
                    if (this.optionsList.findIndex(f => !f.label) > -1) {
                        this.$message.warning('选项中有还未填写的信息')
                        return
                    }
                    this.currentItem.options = this.optionsList
                } else if (this.currentItem.labelType == 'Cascader') {
                    if (this.isEmptyOption(this.cascaderList)) {
                        this.$message.warning('选项中有还未填写的信息')
                        return
                    }
                    this.currentItem.options = this.cascaderList
                }
                this.close();
            },
            cancleSetOption() {
                if (this.currentItem.labelType == 'Radio' || this.currentItem.labelType == 'Checkbox' || this
                    .currentItem.labelType == 'Select') {
                    this.optionsList = [...this.currentItem.options]
                } else if (this.currentItem.labelType == 'Cascader') {
                    this.cascaderList = [...this.currentItem.options]
                    this.firstIndex = -1;
                    this.secondIndex = -1;
                    this.thirdIndex = -1;
                    this.fourIndex = -1;
                    this.setLevelInfo(this.cascaderList)
                }
                this.close();
            },
            isEmptyOption(options) {
                if (options.findIndex(f => !f.label) > -1) {
                    return true
                } else {
                    let isEmpty = false
                    options.forEach(item => {
                        if (item.children && item.children.length) {
                            isEmpty = this.isEmptyOption(item.children)
                        }
                    })
                    return isEmpty
                }
            },
            deleteOptionItem(item, index, level) {
                let cascaderOptions = this.cascaderList
                if (level == 1) {
                    if (this.deleTips(cascaderOptions[index])) {
                        if (index <= this.firstIndex) {
                            this.firstIndex--;
                        }
                        cascaderOptions.splice(index, 1);
                    } else {
                        this.$confirm(`该选项下面还有子级内容，确定要删除该选项吗?`, '提示', {
                            type: 'warning'
                        }).then(() => {
                            if (index <= this.firstIndex) {
                                this.firstIndex--;
                            }
                            cascaderOptions.splice(index, 1);
                            this.secondIndex = -1;
                            this.thirdIndex = -1;
                            this.fourIndex = -1;
                            this.cascaderList = cascaderOptions
                            this.setLevelInfo(cascaderOptions)
                        }).catch(error => {})
                    }
                } else if (level == 2) {
                    if (this.deleTips(cascaderOptions[this.firstIndex].children[index])) {
                        if (index <= this.secondIndex) {
                            this.secondIndex--;
                        }
                        cascaderOptions[this.firstIndex].children.splice(index, 1);
                        if (index == 0) {
                            cascaderOptions[this.firstIndex].children = null
                        }
                    } else {
                        this.$confirm(`该选项下面还有子级内容，确定要删除该选项吗?`, '提示', {
                            type: 'warning'
                        }).then(() => {
                            if (index <= this.secondIndex) {
                                this.secondIndex--;
                            }
                            cascaderOptions[this.firstIndex].children.splice(index, 1);
                            if (index == 0) {
                                cascaderOptions[this.firstIndex].children = null
                            }
                            this.thirdIndex = -1;
                            this.fourIndex = -1;
                            this.cascaderList = cascaderOptions
                            this.setLevelInfo(cascaderOptions)
                        }).catch(error => {})
                    }
                } else if (level == 3) {
                    if (this.deleTips(cascaderOptions[this.firstIndex].children[this.secondIndex].children[index])) {
                        if (index <= this.thirdIndex) {
                            this.thirdIndex--;
                        }
                        cascaderOptions[this.firstIndex].children[this.secondIndex].children.splice(index, 1);
                        if (index == 0) {
                            cascaderOptions[this.firstIndex].children[this.secondIndex].children = null
                        }
                    } else {
                        this.$confirm(`该选项下面还有子级内容，确定要删除该选项吗?`, '提示', {
                            type: 'warning'
                        }).then(() => {
                            if (index <= this.thirdIndex) {
                                this.thirdIndex--;
                            }
                            cascaderOptions[this.firstIndex].children[this.secondIndex].children.splice(index,
                                1);
                            if (index == 0) {
                                cascaderOptions[this.firstIndex].children[this.secondIndex].children = null
                            }
                            this.fourIndex = -1;
                            this.cascaderList = cascaderOptions
                            this.setLevelInfo(cascaderOptions)
                        }).catch(error => {})
                    }
                } else if (level == 4) {
                    if (index <= this.fourIndex) {
                        this.fourIndex--;
                    }
                    cascaderOptions[this.firstIndex].children[this.secondIndex].children[this.thirdIndex]
                        .children.splice(index, 1);
                    if (index == 0) {
                        cascaderOptions[this.firstIndex].children[this.secondIndex].children[this.thirdIndex]
                            .children = null
                    }
                }
                this.cascaderList = cascaderOptions
                this.setLevelInfo(cascaderOptions)
            },
            deleTips(optionList) {
                if (optionList && optionList.children && optionList.children.length) {
                    return false
                } else {
                    return true
                }
            },
            // 
            addOthetTagOptions() {
                let currentOptions = this.optionsList
                // if (currentOptions.findIndex(f => !f.label) > -1) {
                //     this.$message.warning('有选项还没有填写内容')
                //     return
                // }
                let currentId = Math.random().toString(36).substring(2, 5) + currentOptions.length
                let info = {
                    value: currentId,
                    label: '',
                    disabled: false,
                }
                this.optionsList.push(info)
            },
            // 
            deleteOthetTagOptionItem(index) {
                this.optionsList.splice(index, 1)
            },
            // 
            checkedOtherTagItem() {},
            // 选项说明
            disposeExplainDialog(event) {
                this.editCurrentOptionsItem = event
                this.disposeExplainTips = event.disposeExplain
                this.isShowDisposeExplain = true
            },
            setDisposeExplain() {
                this.currentItem.options.forEach(item => {
                    if (item.value == this.editCurrentOptionsItem.value) {
                        item['disposeExplain'] = this.disposeExplainTips
                    }
                })
                this.editCurrentOptionsItem = undefined;
                this.isShowDisposeExplain = false;
            },
            cancleDisposeExplain() {
                this.disposeExplainTips = '';
                this.isShowDisposeExplain = false;
            },
            // 设置多选的 互斥 条件
            showMutualExclusionDialog(event) {
                if (this.optionsList.findIndex(f => !f.label) > -1) {
                    this.$message.warning('有选项还没有填写内容')
                    return
                }
                this.mutualExclusionVisible = true;
                this.editCurrentOptionsItem = event;
                this.currentOptions = this.optionsList.filter(f => f.value != event.value)
                let mutualExclusion = [];
                if(event.mutualExclusion&&event.mutualExclusion.length){
                    event.mutualExclusion.forEach(item=>{
                        if(this.currentOptions.findIndex(f=>f.value==item)>-1){
                            mutualExclusion.push(item)
                        }
                    })
                }
                this.checkedMutualExclusion = mutualExclusion
            },
            // 选择选项内容
            mutualExclusionChange(event) {

            },
            setMutualExclusion() {
                this.currentItem.options.forEach(item => {
                    if (item.value == this.editCurrentOptionsItem.value) {
                        item['mutualExclusion'] = this.checkedMutualExclusion
                    }
                })
                this.checkedMutualExclusion = [];
                this.mutualExclusionVisible = false;
            },
            cancleSetMutualInfo() {
                this.checkedMutualExclusion = [];
                this.mutualExclusionVisible = false;
            },
            /* ******************* table 表格 start ********************* */
            // 新增列
            addTablecolumn() {
                const codeStr = Math.random().toString(36).substring(2, 5)
                this.currentItem.data.forEach(item => {
                    item['列' + codeStr] = {
                        value: '',
                        type: 'text'
                    }
                })
                this.tableHdeaders = Object.keys(this.currentItem.data[0])
            },
            // 新增行
            addTableRow() {
                let length = this.currentItem.data.length
                let newItem = JSON.parse(JSON.stringify(this.currentItem.data[length - 1]))
                Object.keys(newItem).forEach(item => {
                    newItem[item].value = '';
                })
                this.currentItem.data.push(newItem)
            },
            // 列的操作 左移右移 
            tableHeaderToolChange(event, index) {
                this.tableHeaderTool = 'default';
                if (event == 'left' || event == 'right') {
                    if (event == 'left') {
                        this.tableHdeaders[index] = this.tableHdeaders.splice(index - 1, 1, this.tableHdeaders[index])[
                            0];
                    } else if (event == 'right') {
                        this.tableHdeaders[index] = this.tableHdeaders.splice(index + 1, 1, this.tableHdeaders[index])[
                            0];
                    }
                    let currentItemData = JSON.parse(JSON.stringify(this.currentItem.data))
                    currentItemData.forEach(item => {
                        let newItem = {};
                        this.tableHdeaders.forEach((headerItem, headerIndex) => {
                            newItem[this.tableHdeaders[headerIndex]] = item[this.tableHdeaders[
                                headerIndex]]
                        })
                        item = newItem
                    })
                }
            },
            // 删除列
            deleteTableColumn(event) {
                this.currentItem.data.forEach(item => {
                    delete item[event]
                })
                this.tableHdeaders = this.tableHdeaders.filter(f => f != event);
                console.log('this.currentItem.data', this.currentItem.data, this.tableHdeaders)
            },
            // 删除行
            deleteTableRow(index) {
                this.currentItem.data.splice(index, 1)
            },
            // 上移
            upTableRow(index) {
                let currentItemData = this.currentItem.data
                currentItemData[index] = currentItemData.splice(index - 1, 1, currentItemData[index])[0];
            },
            // 下移
            downTableRow(index) {
                let currentItemData = this.currentItem.data
                currentItemData[index] = currentItemData.splice(index + 1, 1, currentItemData[index])[0];

            },
            // 列的类型切换
            tableColumnTypeChange(event, propertyName) {
                this.currentItem.data.forEach(item => {
                    item[propertyName].type = event;
                    item[propertyName].value = ''
                })
                this.currentItem.renderKey = +new Date()
            },
            /******************** table 表格 end ********************* */
        }
    }
</script>
<style lang="scss" scoped>
    ::v-deep .el-dialog__body {
        height: 70vh;
        overflow-y: auto;
    }

    .current_container {
        width: 100%;
        height: 100%;
        display: flex;
        border: 1px solid #dde5ed;

        .grid-content {
            height: 100%;
            flex: 1;
            border-right: 1px solid #dde5ed;
            padding: 10px;

            &:last-child {
                border-right: none;
            }

            .drawing-itemInfo {
                height: auto;
                max-height: calc(100% - 70px);
                overflow-y: auto;
            }

            .el-tag {
                width: 100%;
                text-align: center;
            }

            .optionsItem {
                cursor: move;
                padding: 5px;
                position: relative;
                margin-top: 5px;

                &.activeOption {
                    background: #f6f7ff;
                }

                .item_order {
                    width: 20px;
                    height: 40px;
                    text-align: center;
                    line-height: 36px;
                }

                ::v-deep .el-input__inner {
                    border-color: transparent;
                    padding: 0 5px 0;
                    background: transparent;

                    &:hover,
                    &:active {
                        border-color: #dde5ed;
                    }
                }

                .items_name {
                    flex-shrink: 0;
                }

                .delete_btn {
                    padding: 5px;
                    position: absolute;
                    right: 0;
                    top: -5px;
                    z-index: 100;
                    cursor: pointer;
                    color: #f56c6c;
                    font-size: 18px;
                }

            }

        }
    }

    .current_other_container {
        width: 100%;
        height: 100%;

        .drawing-itemInfo {
            height: auto;
            overflow-y: auto;
        }

        .other_tag_item {
            display: flex;
            margin-bottom: 10px;
            align-items: center;
            cursor: move;

            .options_item_info {
                display: flex;

                .items_name {
                    flex-shrink: 0;
                }

                .el-input {
                    max-width: 180px;
                }

                .el-input-number {
                    max-width: 120px;
                }
            }

            .delete_btn {
                font-size: 18px;
                color: #f56c6c;
                cursor: pointer;
            }

            .margin_mini_left {
                margin-left: 10px;
            }

            .options_typeList {
                width: 110px;
            }
        }
    }

    .display-flex {
        display: flex;
        align-items: center;
    }

    .current_table_container {

        .header_icon {
            margin: 0 3px;
            cursor: pointer;
        }

        .table_headers {
            display: flex;
            align-items: center;

            .header_icons {
                width: 128px;
                overflow: hidden;

                ::v-deep .el-input__inner {
                    padding: 0 20px 0 5px;
                }

                ::v-deep .el-input__suffix {
                    right: 0;
                }
            }

            .deleteBtn {
                font-size: 16px;
                margin-left: 5px;
                padding: 5px;
            }
        }

        .deleteBtn {
            color: #f56c6c;
        }

        .header_text {
            flex-shrink: 0;
        }

        .column_content {
            display: flex;
            align-items: center;
        }
    }

    .mutual_exclusion_dialog {
        ::v-deep .el-dialog__body {
            height: 40vh;
        }

        .dialog_title {
            line-height: 30px;
            color: #333;
            font-weight: bold;
        }

        .el-checkbox-group {
            padding: 5px 10px 20px;
        }
    }

    .footer_content {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
</style>
